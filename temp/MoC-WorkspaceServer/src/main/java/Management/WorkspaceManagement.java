package Management;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.maven.shared.invoker.*;
import static workspace.Action.COMPILE;
import static workspace.Action.CREATE;
import static workspace.Action.DELETE;
import static workspace.Action.FILE;
import static workspace.Action.FOLDER_STRUCTURE;
import static workspace.Action.PUSH_CHALLENGE;
import static workspace.Action.TEST;
import static workspace.Action.TESTALL;
import static workspace.Action.UPDATE;
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
    private final List<String> teams = new ArrayList<>();
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
            defaultPath = "C:/MoC/";
        }
        //load teams
        File rootFolder = new File(defaultPath);
        if (!rootFolder.exists()) {
            rootFolder.mkdir();
        }

        for (File f : rootFolder.listFiles()) {
            if (f.isDirectory()) {
                teams.add(f.getName());
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
                return buildWorkspace(compileRequest.getTeamName(), compileRequest.getChallengeName());
            case TEST:
                TestRequest testRequest = (TestRequest) r;
                return test(testRequest.getTeamName(), testRequest.getChallengeName(), testRequest.getTestName(), testRequest.getTestName());
            case TESTALL:
                TestAllRequest testAllRequest = (TestAllRequest) r;
                return testAll(testAllRequest.getTeamName(), testAllRequest.getChallengeName());
            case UPDATE:
                UpdateRequest updateRequest = (UpdateRequest) r;
                return updateFile(updateRequest.getTeamName(), updateRequest.getFilePath(), updateRequest.getFileContent());
            case CREATE:
                CreateRequest createRequest = (CreateRequest) r;
                return createWorkspace(createRequest.getTeamName());
            case DELETE:
                DeleteRequest deleteRequest = (DeleteRequest) r;
                return removeWorkspace(deleteRequest.getTeamName());
            case PUSH_CHALLENGE:
                PushRequest pushRequest = (PushRequest) r;
                return extractChallenge(pushRequest.getChallengeName(), pushRequest.getData());
            case FOLDER_STRUCTURE:
                FolderStructureRequest folderStructureRequest = (FolderStructureRequest) r;
                String folderPath
                        = defaultPath
                        + folderStructureRequest.getCompetition() + File.pathSeparator
                        + folderStructureRequest.getChallengeName() + File.pathSeparator
                        + folderStructureRequest.getTeamName();
                return FileManagement.getInstance("some jar").getFolderJSON(folderPath);
            case FILE:
                FileRequest fileRequest = (FileRequest) r;
                return FileManagement.getInstance("some jar").getFileJSON(fileRequest.getFilepath());
            // private final String defaultJar = defaultPath + "/annotionframework/annotatedProject-1.0.jar"
            default:
                return "error, unknown action: " + r.getAction().name();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="create, remove & edit" >
    /**
     * Creates a workspace for the specified team
     *
     * @param teamName Name of the team
     * @return A String indicating the success or failure the workspace creation
     */
    protected String createWorkspace(String teamName) {
        try {
            new File(defaultPath + teamName).mkdirs();
            teams.add(teamName);
            return "Created workspace for team: " + teamName;
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            return "Error creating workspace for team: " + teamName + "\n" + ex.getLocalizedMessage();
        }
    }

    /**
     * Removes the workspace of the specified team
     *
     * @param teamName The workspace name to remove
     * @return A String indicating whether the workspace has been removed or
     * not.
     */
    protected String removeWorkspace(String teamName) {
        File path = new File(defaultPath + teamName);
        if (deleteDirectory(path)) {
            teams.remove(teamName);
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
    protected String updateFile(String teamName, String filePath, String fileContent) {
        //variables
        File originalPath = new File(defaultPath + teamName + "/" + filePath);
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
     * @param challengeName The challenge to extract
     * @return A string indicating the success of the extraction
     */
    protected String extractChallenge(String challengeName, byte[] data) {

        String challengePath = defaultPath + challengeName + ".zip";

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(challengePath);
            fos.write(data);
        } catch (Exception ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (Exception ex) {
                Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        File challengeZip = new File(challengePath);
        //for all teams
        for (String teamName : teams) {
            try {
                extractChallengeToTeam(challengeZip, teamName);
            } catch (Exception ex) {
                Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
                return "Error extracting: " + ex.getLocalizedMessage();
            }
        }
        return "Extracting successfull";
    }

    private void extractChallengeToTeam(File challengeZip, String teamName) throws IOException {
        //zip
        ZipFile zip = new ZipFile(challengeZip);
        Enumeration zipFileEntries = zip.entries();
        //output directory
        String outputPath = defaultPath + teamName;
        new File(outputPath).mkdir();
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
        InputStream in = zip.getInputStream(entry);
        BufferedInputStream is = new BufferedInputStream(in);
        int currentByte;
            byte[] data = new byte[bufferSize];

            try (FileOutputStream fos = new FileOutputStream(destFile); BufferedOutputStream dest = new BufferedOutputStream(fos, bufferSize)) {
                while ((currentByte = is.read(data, 0, bufferSize)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
            }
        is.close();
        in.close();
    }

    // <editor-fold defaultstate="collapsed" desc="build, test (maven invoker)" >
    /**
     * Attempts to build the specified challenge in the specified workspace.
     *
     * @param workspaceName The name of the workspace which should be used
     * @param challengeName The name of the challenge to be build
     * @return A String with the build output.
     */
    protected String buildWorkspace(String workspaceName, String challengeName) {
        try {
            beforeMavenInvocation(workspaceName, challengeName);
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
    protected String testAll(String workspaceName, String challengeName) {
        try {
            beforeMavenInvocation(workspaceName, challengeName);
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
     * @param workspaceName The name of the workspace which should be used
     * @param challengeName The name of the challenge to be tested
     * @param testFile The file to test
     * @param testName The name of the test to test
     * @return
     */
    protected String test(String workspaceName, String challengeName, String testFile, String testName) {
        try {
            beforeMavenInvocation(workspaceName, challengeName);
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

    private void beforeMavenInvocation(String workspaceName, String challengeName) throws IOException {
        //create target dir
        String projectDir = defaultPath + workspaceName + "/" + challengeName;
        File targetFolder = new File(projectDir + "/target");
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
