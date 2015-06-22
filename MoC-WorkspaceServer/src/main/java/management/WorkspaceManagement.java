package management;

// <editor-fold defaultstate="collapsed" desc="imports" >
import controllers.PathController;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.cli.CommandLineException;
// </editor-fold>

/**
 * Singleton class used to keep track, create, remove, update, build and test
 * workspaces
 *
 * @author TeamKoekje
 */
public class WorkspaceManagement {

    // <editor-fold defaultstate="collapsed" desc="variables" >
    private final PathController pathInstance;
    /**
     * Key: Competition name Value: ArrayList<String> with team names
     */
    private final Map<String, List<String>> competitions = new HashMap<>();
    private static final File MAVEN_HOME = new File("C:\\Program Files\\apache-maven-3.2.5");
    private static final Invoker MAVEN_INVOKER = new DefaultInvoker();
    private static InvocationRequest request;
    private StringBuilder invocationOutput = new StringBuilder();
    private String serverId;
    private boolean firstError;
    private boolean firstTest;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    private static WorkspaceManagement instance;

    /**
     * Creates a new instance of the WorkspaceManagement singleton.
     */
    protected WorkspaceManagement() {
        pathInstance = PathController.getInstance();
        //load teams           
        File rootFolder = new File(pathInstance.getDefaultPath());
        if (!rootFolder.exists()) {
            rootFolder.mkdir();
            File competitionFolder = new File(pathInstance.getCompetitionsPath());
            if (!competitionFolder.exists()) {
                competitionFolder.mkdir();
            }
            for (File f : competitionFolder.listFiles()) {
                if (f.isDirectory()) {
                    String competitionName = f.getName();
                    if (!competitions.containsKey(competitionName)) {
                        competitions.put(competitionName, new ArrayList());
                    }
                    List<String> tempList = competitions.get(competitionName);
                    for (File f2 : f.listFiles()) {
                        tempList.add(f2.getName());
                    }
                    competitions.put(competitionName, (ArrayList<String>) tempList);
                }
            }
        }

        MAVEN_INVOKER.setMavenHome(MAVEN_HOME);
        MAVEN_INVOKER.setOutputHandler(new InvocationOutputHandler() {
            @Override
            public void consumeLine(String string) {
                if(string.startsWith("Tests run:") && firstTest){
                    invocationOutput.append(string);
                    invocationOutput.append("\n------------------------------------------------------\n");
                    firstTest = false;
                }
                if(string.startsWith("[ERROR]") && string.contains("MoC/Competitions")){
                    if(firstError){
                        invocationOutput.append("Build failed");
                        invocationOutput.append("\n------------------------------------------------------\n");
                        firstError = false;
                    }
                    invocationOutput.append("[ERROR] " + string.substring(string.lastIndexOf("/") + 1));
                    invocationOutput.append("\n");
                }
            }
        });
    }

    /**
     * Gets the instance of the singleton WorkspaceManagement class.
     *
     * @return A WorkspaceManagement object containing the singleton instance of
     * the class.
     */
    public static WorkspaceManagement getInstance() {
        if (instance == null) {
            instance = new WorkspaceManagement();
        }
        return instance;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getters & setters" >
    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public int getTeamsCount() {
        int temp = 0;
        Iterator it = competitions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ArrayList<String>> pair = (Map.Entry) it.next();
            temp += pair.getValue().size();
        }
        return temp;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    // <editor-fold defaultstate="collapsed" desc="create, remove & edit" >
    /**
     * Creates a workspace for the specified team
     *
     * @param competitionId Id of the competition
     * @param teamName Name of the team
     * @return A String indicating the success or failure the workspace creation
     */
    public String createWorkspace(String competitionId, String teamName) {
        try {
            File teamFolder = new File(pathInstance.teamPath(competitionId, teamName));
            teamFolder.mkdirs();
            List<String> tempList = competitions.get(competitionId);
            if (tempList == null) {
                tempList = new ArrayList<>();
            }
            tempList.add(teamName);
            competitions.put(competitionId, tempList);
            return "Created workspace for team: " + teamName;
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            return "Error creating workspace for team: " + teamName + "\n" + ex.getLocalizedMessage();
        }
    }

    /**
     * Removes the workspace of the specified team
     *
     * @param competitionId The competition name where the team needs to be
     * removed from
     * @param teamName The workspace name to remove
     * @return A String indicating whether the workspace has been removed or
     * not.
     */
    public String removeWorkspace(String competitionId, String teamName) {
        File teamFolder = new File(pathInstance.teamPath(competitionId, teamName));
        if (deleteDirectory(teamFolder)) {
            List<String> tempList = competitions.get(competitionId);
            tempList.remove(teamName);
            competitions.put(competitionId, tempList);
            return "Workspace succesfully deleted";
        } else {
            return "Error while deleting workspace";
        }
    }

    private static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return path.delete();
    }

    /**
     * Updates the specified file for the specified team, with the specified
     * content
     *
     * @param competitionId The id of the competition
     * @param teamName The name of the team
     * @param filePath The path to the file (from workspace home)
     * @param fileContent The new content of the file
     * @return A String indicating the success of the update
     */
    public String updateFile(String competitionId, String teamName, String filePath, String fileContent) {
        //variables
        File originalPath = new File(filePath);
        File tempPath = new File(originalPath + ".temp");
        String deleteTempFileError = "Error while deleting temp file: ";
        //if the file exists, backup
        if (originalPath.exists()) {
            if (originalPath.renameTo(tempPath)) {
                System.out.println("backup made from: " + originalPath + " to: " + tempPath);
            } else {
                System.out.println("Error while creating backup");
                return "Error attempting to backup: " + originalPath + " to: " + tempPath;
            }
        } else {
            return "file not found: " + originalPath;
        }
        //write the new file
        try {
            try (PrintWriter writer = new PrintWriter(originalPath, "UTF-8")) {
                writer.printf(fileContent);
            }
        } //If fail to write the new file, restore the backup
        catch (FileNotFoundException ex) {
            tempPath.renameTo(originalPath);
            try {
                Files.delete(tempPath.toPath());
            } catch (IOException ex1) {
                System.err.println(deleteTempFileError + ex1);
            }
            return "Error File not found: " + ex;
        } catch (UnsupportedEncodingException ex) {
            tempPath.renameTo(originalPath);
            try {
                Files.delete(tempPath.toPath());
            } catch (IOException ex1) {
                System.err.println(deleteTempFileError + ex1);
            }
            return "Error Unsupported Encoding: " + ex;
        }
        try {
            Files.delete(tempPath.toPath());
        } catch (IOException ex1) {
            System.err.println(deleteTempFileError + ex1);
            return "File succesfully Updated but temp file not deleted";
        }
        return "File succesfully Updated";
    }
    // </editor-fold>

    /**
     * Extracts the specified challenge to all workspaces
     *
     * @param competitionId The id of the competition
     * @param challengeName The name of the challenge to extract
     * @param data The data to extract
     * @return A string indicating the success of the extraction
     */
    public String extractChallenge(String competitionId, String challengeName, byte[] data) {
        File challengePath = new File(pathInstance.challengesPath(competitionId));
        challengePath.mkdirs();
        File challengeZip = new File(challengePath.getAbsoluteFile() + File.separator + challengeName + ".zip");
        //write to server        
        writeZipToCompetition(data, challengeZip.getAbsolutePath());
        try {
            extractZip(challengeZip);
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        //for all teams
        for (String teamName : competitions.get(competitionId)) {
            try {
                copyChallengeToTeam(challengeName, teamName, competitionId);
            } catch (Exception ex) {
                Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
                return "Error extracting: " + ex.getLocalizedMessage();
            }
        }
        return "Extracting successfull";
    }

    /**
     * Converts the specified byte array to a zip file on the specified path.
     *
     * @param data The data to convert to a zip file
     * @param zipPath The path to write the zip file to
     */
    private void writeZipToCompetition(byte[] data, String zipPath) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(zipPath);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Extracts a challenge zip file and moves the jar file out of the folder
     *
     * @param challengeZip The zip file of the challenge
     * @throws IOException
     */
    private void extractZip(File challengeZip) throws IOException {
        ZipFile zip = new ZipFile(challengeZip);
        Enumeration zipFileEntries = zip.entries();
        String outputPath = challengeZip.getParent() + File.separator + FilenameUtils.removeExtension(challengeZip.getName());

        //loop through the zip
        while (zipFileEntries.hasMoreElements()) {
            //create the file/folder
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(outputPath, currentEntry);
            File destinationParent = destFile.getParentFile();
            destinationParent.mkdirs();
            //if the entry is a file, write the contents to it
            if (!entry.isDirectory()) {
                writeZipEntry(zip, entry, destFile);
                if (destFile.getName().endsWith("jar") && !destFile.getName().startsWith("MoCFramework")) {
                    File f = new File(challengeZip.getParent() + File.separator + destFile.getName());
                    if(f.exists()){
                        f.delete();
                    }
                    destFile.renameTo(f);
                }
            }
        }
    }

    /**
     * Copies the specified challenge to the workspace of the specified team
     *
     * @param challengeName Name of the challenge
     * @param teamName Name of the team
     * @param competitionId Id of the competition
     */
    private void copyChallengeToTeam(String challengeName, String teamName, String competitionId) {

        //TODO: DO IETS MET EXTRA JAR FILE IDK VRAAG AAN CASPER OF ROBIN
        //TODO: EN TEST HET PUSHEN MET ONDERSTAANDE CODE EN KIJK OF COMPILEN DAN NOG GOED GAAT
        
//        File file = new File(pathInstance.challengesPath(competitionId) + File.separator + challengeName);
//        String[] directories = file.list(new FilenameFilter() {
//            @Override
//            public boolean accept(File current, String name) {
//                return new File(current, name).isDirectory();
//            }
//        });
//        File srcFolder = new File(pathInstance.challengesPath(competitionId) + File.separator + challengeName + File.separator + directories[0]);

        File srcFolder = new File(pathInstance.challengesPath(competitionId) + File.separator + challengeName);
        File destFolder = new File(pathInstance.teamPath(competitionId, teamName));

        if (srcFolder.exists()) {
            try {
                copyFolder(srcFolder, destFolder);
            } catch (IOException ex) {
                Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Recursive copy of a folder/file
     *
     * @param src The source directory or file to copy
     * @param dest The destination directory or file
     * @throws IOException
     */
    private void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            // If directory not exists, create it
            if (!dest.exists()) {
                dest.mkdir();
            }
            // List all the directory contents
            String files[] = src.list();
            for (String file : files) {
                // Construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // Recursive copy
                copyFolder(srcFile, destFile);
            }
        } else {
            // If file, then copy it
            // Use bytes stream to support all file types
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            // Copy the file content in bytes 
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }

    /**
     * Actually writes the data from the zip to the destination file.
     *
     * @param zip The zip to extract
     * @param entry The entry within the zip to extract
     * @param destFile The file to extract the entry to
     * @throws IOException Thrown when a read/write error occurs in either the
     * original zip or the destination file.
     */
    private void writeZipEntry(ZipFile zip, ZipEntry entry, File destFile) throws IOException {
        int bufferSize = 2048;
        try (BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry))) {
            int currentByte;
            byte[] data = new byte[bufferSize];

            FileOutputStream fos = new FileOutputStream(destFile);
            try (BufferedOutputStream dest = new BufferedOutputStream(fos, bufferSize)) {
                while ((currentByte = is.read(data, 0, bufferSize)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="build, test (maven invoker)" >
    /**
     * Attempts to build the specified challenge in the specified workspace.
     *
     * @param competitionId The id of the competition
     * @param teamName The name of the team within the competition
     * @param challengeName The name of the challenge to build
     * @return A String with the build output.
     */
    public String buildWorkspace(String competitionId, String teamName, String challengeName) {
        try {
            beforeMavenInvocation(competitionId, teamName, challengeName);
            request.setGoals(Arrays.asList("install"));
            request.setProperties(new Properties());
            
            firstError = true;
            firstTest = true;
            MAVEN_INVOKER.execute(request);
            
            return getInvocationResult();
        } catch (IOException | MavenInvocationException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
            return "Exception while building (NOT compilation error): " + ex.getLocalizedMessage();
        }
    }

    /**
     * Attempts to test all tests for the specified challenge in the specified
     * workspace.
     *
     * @param competitionId The id of the competition
     * @param teamName The name of the team within the competition
     * @param challengeName The name of the challenge to test
     * @return A String indicating the test report
     */
    public String testAll(String competitionId, String teamName, String challengeName) {
        try {
            beforeMavenInvocation(competitionId, teamName, challengeName);
            request.setGoals(Arrays.asList("test"));
            request.setProperties(new Properties());

            firstError = true;
            firstTest = true;
            MAVEN_INVOKER.execute(request);

            return getInvocationResult();
        } catch (IOException | MavenInvocationException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
            return "Exception while testing: " + ex.getLocalizedMessage();
        }
    }

    /**
     * Attempts to test a specific test for the specified challenge in the
     * specified workspace.
     *
     * @param competitionId The id of the competition
     * @param teamName The name of the team within the competition
     * @param challengeName The name of the challenge to test
     * @param testName The name of the test to test
     * @return A String indicating the result of the test
     */
    public String test(String competitionId, String teamName, String challengeName, String testName) {
        try {
            beforeMavenInvocation(competitionId, teamName, challengeName);
            request.setGoals(Arrays.asList("test"));
            Properties p = new Properties();
            p.setProperty("test", testName);
            request.setProperties(p);

            firstError = true;
            firstTest = true;
            MAVEN_INVOKER.execute(request);

            return getInvocationResult();
        } catch (IOException | MavenInvocationException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
            return "Exception while testing: " + ex.getLocalizedMessage();
        }
    }

    /**
     * Should always be called before using a maven invocation. makes sure that
     * the target folder exists, resets the invocation request and sets the pom
     * file correctly.
     *
     * @param competitionId The id of the competition
     * @param teamName The name of the team within the competition
     * @param challengeName The name of the challenge to be invoked.
     * @throws IOException Thrown when an IOException occurs.
     */
    private void beforeMavenInvocation(String competitionId, String teamName, String challengeName) throws IOException {
        //create target dir
        File projectDir = new File(pathInstance.teamChallengePath(competitionId, teamName, challengeName));
        File targetFolder = new File(projectDir.getAbsoluteFile() + File.separator + "target");
        if (!targetFolder.exists()) {
            targetFolder.mkdir();
        }
        //setup the request
        request = new DefaultInvocationRequest();
        request.setPomFile(new File(projectDir + "/pom.xml"));
    }

    /**
     * Gets the current result saved in the StringBuilder that keeps track of
     * invocation results. Clears the StringBuilder aswell.
     *
     * @return String indicating the result of the invocation result(s)
     */
    private String getInvocationResult() {
        String result = invocationOutput.toString();
        invocationOutput = new StringBuilder();
        return result.replaceAll("\n", "<br />");
    }
    // </editor-fold>
    // </editor-fold>    
}
