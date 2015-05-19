package main;

import file.FileManagement;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.maven.shared.invoker.*;
import workspace.CompileRequest;
import workspace.CreateRequest;
import workspace.DeleteRequest;
import workspace.FileRequest;
import workspace.FolderStructureRequest;
import workspace.PushRequest;
import workspace.Request;
import workspace.TestAllRequest;
import workspace.TestRequest;
import workspace.UpdateRequest;

/**
 * Singleton class used to keep track, create, remove, update, build and test
 * workspaces
 *
 * @author TeamKoekje
 */
public class WorkspaceManagement {

    // <editor-fold defaultstate="collapsed" desc="variables" >
    private final String defaultPath;
    /**
     * Key: Competition name Value: ArrayList<String> with team names
     */
    private final HashMap<String, ArrayList<String>> teams = new HashMap<>();
    private static final File MAVEN_HOME = new File("C:/apache-maven-3.3.1");
    private static final Invoker MAVEN_INVOKER = new DefaultInvoker();
    private static InvocationRequest request;
    private StringBuilder invocationOutput = new StringBuilder();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    private static WorkspaceManagement instance;

    /**
     * Creates a new instance of the WorkspaceManagement singleton.
     */
    protected WorkspaceManagement() {
        String osName = System.getProperty("os.name");
        if ("linux".equalsIgnoreCase(osName)) {
            defaultPath = "MoC" + File.pathSeparator;
        } else {
            defaultPath = "C:/MoC" + File.pathSeparator;
        }
        //load teams           
        File rootFolder = new File(defaultPath);
        if (!rootFolder.exists()) {
            rootFolder.mkdir();
            File competitionFolder = new File(defaultPath + File.pathSeparator + "Competitions");
            if (!competitionFolder.exists()) {
                competitionFolder.mkdir();
            }
            for (File f : competitionFolder.listFiles()) {
                if (f.isDirectory()) {
                    String competitionName = f.getName();
                    if (!teams.containsKey(competitionName)) {
                        teams.put(competitionName, new ArrayList());
                    }
                    ArrayList<String> tempList = teams.get(competitionName);
                    for (File f2 : f.listFiles()) {
                        tempList.add(f2.getName());
                    }
                    teams.put(competitionName, tempList);
                }
            }
        }

        MAVEN_INVOKER.setMavenHome(MAVEN_HOME);
        MAVEN_INVOKER.setOutputHandler(new InvocationOutputHandler() {
            @Override
            public void consumeLine(String string) {
                invocationOutput.append(string);
                invocationOutput.append("\n");
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
    public String getDefaultPath() {
        return defaultPath;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Processes the given request
     *
     * @param r The request to process
     * @return The result of the processed request
     */
    public String processRequest(Request r) {
        switch (r.getAction()) {
            case COMPILE:
                CompileRequest compileRequest = (CompileRequest) r;
                return buildWorkspace(compileRequest.getCompetition(), compileRequest.getTeamName(), compileRequest.getChallengeName());
            case TEST:
                TestRequest testRequest = (TestRequest) r;
                return test(testRequest.getCompetition(), testRequest.getTeamName(), testRequest.getChallengeName(), testRequest.getTestFile(), testRequest.getTestName());
            case TESTALL:
                TestAllRequest testAllRequest = (TestAllRequest) r;
                return testAll(testAllRequest.getCompetition(), testAllRequest.getTeamName(), testAllRequest.getChallengeName());
            case UPDATE:
                UpdateRequest updateRequest = (UpdateRequest) r;
                return updateFile(updateRequest.getCompetition(), updateRequest.getTeamName(), updateRequest.getFilePath(), updateRequest.getFileContent());
            case CREATE:
                CreateRequest createRequest = (CreateRequest) r;
                return createWorkspace(createRequest.getCompetition(), createRequest.getTeamName());
            case DELETE:
                DeleteRequest deleteRequest = (DeleteRequest) r;
                return removeWorkspace(deleteRequest.getCompetition(), deleteRequest.getTeamName());
            case PUSH_CHALLENGE:
                PushRequest pushRequest = (PushRequest) r;
                return extractChallengeToTeam(pushRequest.getChallengeName(), pushRequest.getCompetition());
            case FOLDER_STRUCTURE:
                FolderStructureRequest folderStructureRequest = (FolderStructureRequest) r;
                String folderPath
                        = defaultPath
                        + File.pathSeparator
                        + "Competitions"
                        + File.pathSeparator
                        + folderStructureRequest.getCompetition()
                        + File.pathSeparator
                        + "Teams"
                        + File.pathSeparator
                        + folderStructureRequest.getTeamName()
                        + File.pathSeparator
                        + folderStructureRequest.getChallengeName();
                String jarPathForFolder
                        = folderPath
                        + File.pathSeparator
                        + folderStructureRequest.getChallengeName() + ".jar";
                return FileManagement.getInstance(jarPathForFolder).getFolderJSON(folderPath);
            case FILE:
                FileRequest fileRequest = (FileRequest) r;
                String jarPathForFile
                        = defaultPath
                        + File.pathSeparator
                        + "Competitions"
                        + File.pathSeparator
                        + fileRequest.getCompetition()
                        + File.pathSeparator
                        + "Teams"
                        + File.pathSeparator
                        + fileRequest.getTeamName()
                        + File.pathSeparator
                        + fileRequest.getChallangeName()
                        + File.pathSeparator
                        + fileRequest.getChallangeName() + ".jar";
                return FileManagement.getInstance(jarPathForFile).getFileJSON(fileRequest.getFilepath());
            // private final String defaultJar = defaultPath + "/annotionframework/annotatedProject-1.0.jar"
            default:
                return "error, unknown action: " + r.getAction().name();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="create, remove & edit" >
    /**
     * Creates a workspace for the specified team
     *
     * @param competitionName Name of the competition
     * @param teamName Name of the team
     * @return A String indicating the success or failure the workspace creation
     */
    protected String createWorkspace(String competitionName, String teamName) {
        try {
            File teamFolder = new File(defaultPath
                    + File.pathSeparator
                    + "Competitions"
                    + File.pathSeparator
                    + competitionName
                    + File.pathSeparator
                    + "Teams"
                    + File.pathSeparator
                    + teamName);
            teamFolder.mkdirs();
            ArrayList<String> tempList = teams.get(competitionName);
            tempList.add(teamName);
            teams.put(competitionName, tempList);
            return "Created workspace for team: " + teamName;
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            return "Error creating workspace for team: " + teamName + "\n" + ex.getLocalizedMessage();
        }
    }

    /**
     * Removes the workspace of the specified team
     *
     * @param competitionName The competition name where the team needs to be
     * removed from
     * @param teamName The workspace name to remove
     * @return A String indicating whether the workspace has been removed or
     * not.
     */
    protected String removeWorkspace(String competitionName, String teamName) {
        File teamFolder = new File(defaultPath
                + File.pathSeparator
                + "Competitions"
                + File.pathSeparator
                + competitionName
                + File.pathSeparator
                + "Teams"
                + File.pathSeparator
                + teamName);
        if (deleteDirectory(teamFolder)) {
            ArrayList<String> tempList = teams.get(competitionName);
            tempList.remove(teamName);
            teams.put(competitionName, tempList);
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
     * @param teamName The name of the team
     * @param filePath The path to the file (from workspace home)
     * @param fileContent The new content of the file
     * @return A String indicating the success of the update
     */
    protected String updateFile(String competitionName, String teamName, String filePath, String fileContent) {
        //variables
        File teamFolder = new File(defaultPath
                + File.pathSeparator
                + "Competitions"
                + File.pathSeparator
                + competitionName
                + File.pathSeparator
                + "Teams"
                + File.pathSeparator
                + teamName);
        File originalPath = new File(teamFolder.getAbsolutePath() + File.pathSeparator + filePath);
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
     * @param competitionName The name of the competition
     * @param challengeName The challenge to extract
     * @return A string indicating the success of the extraction
     */
    protected String extractChallengeToTeam(String challengeName, String competitionName) {
        File challengeZip = new File(defaultPath
                + File.pathSeparator
                + "Competitions"
                + File.pathSeparator
                + competitionName
                + File.pathSeparator
                + "Challenges"
                + File.pathSeparator
                + challengeName + ".zip");
        //for all teams
        for (String teamName : teams.get(competitionName)) {
            try {
                extractChallenge(challengeZip, teamName);
            } catch (Exception ex) {
                Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
                return "Error extracting: " + ex.getLocalizedMessage();
            }
        }
        return "Extracting successfull";
    }

    private void extractChallenge(File challengeZip, String teamName) throws IOException {
        //zip
        ZipFile zip = new ZipFile(challengeZip);
        Enumeration zipFileEntries = zip.entries();
        //output directory
        String outputPath = challengeZip.getParent()
                + File.pathSeparator
                + "Teams"
                + File.pathSeparator
                + teamName
                + File.pathSeparator
                + challengeZip.getParentFile().getName();
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
            }
        }
    }

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
     * @param workspaceName The name of the workspace which should be used
     * @param challengeName The name of the challenge to be build
     * @return A String with the build output.
     */
    protected String buildWorkspace(String competitionName, String teamName, String challengeName) {
        try {
            beforeMavenInvocation(competitionName, teamName, challengeName);
            request.setGoals(Arrays.asList("install"));
            request.setProperties(new Properties());

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
     * @param workspaceName The name of the workspace which should be used
     * @param challengeName The name of the challenge to be tested
     * @return A String indicating the test report
     */
    protected String testAll(String competitionName, String teamName, String challengeName) {
        try {
            beforeMavenInvocation(competitionName, teamName, challengeName);
            request.setGoals(Arrays.asList("test"));
            request.setProperties(new Properties());

            MAVEN_INVOKER.execute(request);

            return getInvocationResult();
        } catch (IOException | MavenInvocationException ex) {
            ex.printStackTrace();
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
            return "Exception while testing: " + ex.getLocalizedMessage();
        }
    }

    /**
     * Attempts to test a specific test for the specified challenge in the
     * specified workspace.
     *
     * @param competitionName
     * @param challengeName The name of the challenge to be tested
     * @param teamName
     * @param testFile The file to test
     * @param testName The name of the test to test
     * @return
     */
    protected String test(String competitionName, String teamName, String challengeName, String testFile, String testName) {
        try {
            beforeMavenInvocation(competitionName, teamName, challengeName);
            request.setGoals(Arrays.asList("test"));
            Properties p = new Properties();
            p.setProperty("test", testFile + "#" + testName);
            request.setProperties(p);

            MAVEN_INVOKER.execute(request);

            return getInvocationResult();
        } catch (IOException | MavenInvocationException ex) {
            ex.printStackTrace();
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
            return "Exception while testing: " + ex.getLocalizedMessage();
        }
    }

    private void beforeMavenInvocation(String competitionName, String teamName, String challengeName) throws IOException {
        //create target dir
        File projectDir = new File(defaultPath
                + File.pathSeparator
                + "Competitions"
                + File.pathSeparator
                + competitionName
                + File.pathSeparator
                + "Teams"
                + File.pathSeparator
                + teamName
                + File.pathSeparator
                + challengeName);
        File targetFolder = new File(projectDir.getAbsoluteFile() + File.pathSeparator + "target");
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
        return result;
    }
    // </editor-fold>

    // </editor-fold>
}
