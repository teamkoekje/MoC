package main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import workspace.Request;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
public class WorkspaceManagement {

    public static final String defaultPath = "C:\\MoC\\";
    public List<String> teams;
    private static final File mavenHome = new File("C:\\apache-maven-3.2.5");
    private static final String basePath = "C:\\MoC\\workspaces\\";
    private static final Invoker invoker = new DefaultInvoker();

    private static InvocationRequest request;

    private static StringBuilder sb;

    private static WorkspaceManagement instance;

    /**
     * Creates a new instance of the WorkspaceManagement singleton.
     */
    protected WorkspaceManagement() {
        teams = new ArrayList<>();
        getWorkspaceFolders();
        sb = new StringBuilder();
        invoker.setMavenHome(mavenHome);
        invoker.setOutputHandler(new InvocationOutputHandler() {
            @Override
            public void consumeLine(String string) {
                sb.append(string);
                sb.append("\n");
            }
        });
    }

    /**
     * 
     * @return 
     */
    public static WorkspaceManagement getInstance() {
        if (instance == null) {
            instance = new WorkspaceManagement();
        }
        return instance;
    }

    /**
     *
     * @param teamName
     * @return
     */
    private String createWorkspace(String teamName) {
        try {
            // Create team directory
            new File(defaultPath + teamName).mkdirs();
            return "Created workspace for team: " + teamName;
        } catch (Exception e) {//Catch exception if any         
            return "Error creating workspace for team: " + teamName + "\n" + e.getLocalizedMessage();
        }
    }

    /**
     *
     * @param teamName
     * @return
     */
    private String removeWorkspace(String teamName) {
        File path = new File(defaultPath + teamName);

        if (deleteDirectory(path)) {
            return "Workspace succesfully deleted";
        } else {
            return "Error while deleting workspace";
        }
    }

    /**
     *
     * @param path
     * @return
     */
    private boolean deleteDirectory(File path) {
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
        return (path.delete());
    }

    /**
     *
     */
    private void getWorkspaceFolders() {
        File folder = new File(defaultPath);
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                teams.add(f.getName());
            }
        }
    }

    /**
     *
     * @param challengeName
     * @return
     */
    private String extractChallenge(String challengeName) {

        String challengeZip = defaultPath + challengeName + ".zip";

        for (String teamName : teams) {
            String OUTPUT_FOLDER = defaultPath + teamName;
            try {
                int BUFFER = 2048;
                File file = new File(challengeZip);

                ZipFile zip = new ZipFile(file);
                String newPath = OUTPUT_FOLDER;

                new File(newPath).mkdir();
                Enumeration zipFileEntries = zip.entries();

                while (zipFileEntries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                    String currentEntry = entry.getName();

                    File destFile = new File(newPath, currentEntry);
                    File destinationParent = destFile.getParentFile();

                    destinationParent.mkdirs();

                    if (!entry.isDirectory()) {
                        BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                        int currentByte;
                        byte data[] = new byte[BUFFER];

                        FileOutputStream fos = new FileOutputStream(destFile);
                        BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

                        while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, currentByte);
                        }
                        dest.flush();
                        dest.close();
                        is.close();
                    }
                }

            } catch (Exception e) {
                return "Error extracting: " + e.getLocalizedMessage();
            }
        }
        return "Extracting successfull";
    }

    /**
     * Attempts to build the specified challenge in the specified workspace.
     * Output is found in:
     * *\\MoC\\workspaces\\workspaceName\\challengeName\\output.txt
     *
     * @param workspaceName The name of the workspace which should be used
     * @param challengeName The name of the challenge to be build
     * @return
     */
    private static String buildWorkspace(String workspaceName, String challengeName) {
        try {

            beforeMavenInvocation(workspaceName, challengeName);

            request.setGoals(Arrays.asList("install"));
            request.setProperties(new Properties());

            invoker.execute(request);
            String outputString = sb.toString();
            sb = new StringBuilder();
            return outputString;
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MavenInvocationException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Attempts to test all tests for the specified challenge in the specified
     * workspace. Output is found in:
     * *\\MoC\\workspaces\\workspaceName\\challengeName\\output.txt
     *
     * @param workspaceName The name of the workspace which should be used
     * @param challengeName The name of the challenge to be tested
     * @return
     */
    private static String testAll(String workspaceName, String challengeName) {
        try {
            beforeMavenInvocation(workspaceName, challengeName);

            request.setGoals(Arrays.asList("test"));
            request.setProperties(new Properties());

            invoker.execute(request);
            String outputString = sb.toString();
            sb = new StringBuilder();
            return outputString;
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MavenInvocationException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Attempts to test a specific test for the specified challenge in the
     * specified workspace. Output is found in:
     * *\\MoC\\workspaces\\workspaceName\\challengeName\\output.txt
     *
     * @param workspaceName The name of the workspace which should be used
     * @param challengeName The name of the challenge to be tested
     * @param testFile
     * @param testName
     * @return
     */
    private static String test(String workspaceName, String challengeName, String testFile, String testName) {
        try {
            beforeMavenInvocation(workspaceName, challengeName);

            request.setGoals(Arrays.asList("test"));
            Properties p = new Properties();
            p.setProperty("test", (testFile + "#" + testName));
            request.setProperties(p);

            invoker.execute(request);
            String outputString = sb.toString();
            sb = new StringBuilder();
            return outputString;
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MavenInvocationException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Exception while testing";
    }

    private static void beforeMavenInvocation(String workspaceName, String challengeName) throws IOException {
        //create target dir
        String projectDir = basePath + workspaceName + "\\" + challengeName;
        File targetFolder = new File(projectDir + "\\target");
        if (!targetFolder.exists()) {
            targetFolder.mkdir();
        }
        //setup the request
        request = new DefaultInvocationRequest();
        request.setPomFile(new File(projectDir + "\\pom.xml"));
    }

    /**
     *
     * @param r
     * @return
     */
    public String processRequest(Request r) {
        switch (r.getAction()) {
            case COMPILE:
                return buildWorkspace(r.getTeamName(), r.getChallengeName());
            case TEST:
                return test(r.getTeamName(), r.getChallengeName(), r.getTestFile(), r.getTestName());
            case TESTALL:
                return testAll(r.getTeamName(), r.getChallengeName());
            case UPDATE:
                return updateFile(r.getTeamName(), r.getFilePath(), r.getFileContent());
            case CREATE:
                return createWorkspace(r.getTeamName());
            case DELETE:
                return removeWorkspace(r.getTeamName());
            case PUSH_CHALLENGE:
                return extractChallenge(r.getChallengeName());
            default:
                return ("error, unknown action: " + r.getAction().name());
        }
    }

    /**
     *
     * @param teamName
     * @param filePath
     * @param fileContent
     * @return
     */
    private static String updateFile(String teamName, String filePath, String fileContent) {

        File originalPath = new File(defaultPath + "/" + teamName + "/" + filePath);
        File tempPath = new File(originalPath + ".temp");
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

        try {
            PrintWriter writer = new PrintWriter(originalPath, "UTF-8");
            writer.printf(fileContent);
            writer.close();
        } catch (FileNotFoundException ex) {
            tempPath.renameTo(originalPath);
            try {
                Files.delete(tempPath.toPath());
            } catch (IOException ex1) {
                System.err.println("Error while deleting temp file: " + ex1);
            }
            return ("Error File not found: " + ex);
        } catch (UnsupportedEncodingException ex) {
            tempPath.renameTo(originalPath);
            try {
                Files.delete(tempPath.toPath());
            } catch (IOException ex1) {
                System.err.println("Error while deleting temp file: " + ex1);
            }
            return ("Error Unsupported Encoding: " + ex);
        }
        try {
            Files.delete(tempPath.toPath());
        } catch (IOException ex1) {
            System.err.println("Error while deleting temp file: " + ex1);
            return "File succesfully Updated but temp file not deleted";
        }
        return "File succesfully Updated";
    }
}
