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
    private static final File mavenHome = new File("C:\\Program Files\\apache-maven-3.2.5");
    private static final String basePath = "C:\\Users\\Astrid\\Desktop\\git\\MoC\\MavenInvokerTest\\MoC\\workspaces\\";
    private static final Invoker invoker = new DefaultInvoker();

    private static FileWriter outputWriter;
    private static BufferedWriter bufferedWriter;
    private static InvocationRequest request;

    private static StringBuilder sb;

    public WorkspaceManagement() {
        teams = new ArrayList<>();
        getWorkspaceFolders();
        sb = new StringBuilder();
        invoker.setMavenHome(mavenHome);
        invoker.setOutputHandler(new InvocationOutputHandler() {
            @Override
            public void consumeLine(String string) {
                sb.append(string + "/n");

                /*
                 try {
                 System.out.println(string);
                 bufferedWriter.write(string + "\n");
                    
                 } catch (IOException ex) {
                 System.err.println(ex.getLocalizedMessage());
                 }*/
            }
        });
    }

    public String createWorkspace(String teamName) {
        try {
            // Create team directory
            new File(defaultPath + teamName).mkdirs();
            return "Created workspace for team: " + teamName;
        } catch (Exception e) {//Catch exception if any         
            return "Error creating workspace for team: " + teamName + "\n" + e.getLocalizedMessage();
        }
    }

    public String removeWorkspace(String teamName) {
        File path = new File(defaultPath + teamName);

        if (deleteDirectory(path)) {
            return "Workspace succesfully deleted";
        } else {
            return "Error while deleting workspace";
        }
    }

    private boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    private void getWorkspaceFolders() {
        File folder = new File(defaultPath);
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                teams.add(f.getName());
            }
        }
    }

    public String extractChallenge(String challengeName) {

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

                // Process each entry
                while (zipFileEntries.hasMoreElements()) {
                    // grab a zip file entry
                    ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                    String currentEntry = entry.getName();

                    File destFile = new File(newPath, currentEntry);
                    //destFile = new File(newPath, destFile.getName());
                    File destinationParent = destFile.getParentFile();

                    // create the parent directory structure if needed
                    destinationParent.mkdirs();

                    if (!entry.isDirectory()) {
                        BufferedInputStream is = new BufferedInputStream(zip
                                .getInputStream(entry));
                        int currentByte;
                        // establish buffer for writing file
                        byte data[] = new byte[BUFFER];

                        // write the current file to disk
                        FileOutputStream fos = new FileOutputStream(destFile);
                        BufferedOutputStream dest = new BufferedOutputStream(fos,
                                BUFFER);

                        // read and write until last byte is encountered
                        while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, currentByte);
                        }
                        dest.flush();
                        dest.close();
                        is.close();
                    }

                }

            } catch (Exception e) {
                return "error";
            }
        }

        return "succes";
    }

    /**
     * Attempts to build the specified challenge in the specified workspace.
     * Output is found in:
     * *\\MoC\\workspaces\\workspaceName\\challengeName\\output.txt
     *
     * @param workspaceName The name of the workspace which should be used
     * @param challengeName The name of the challenge to be build
     * @throws IOException Thrown when an I/O exception occurs.
     * @throws MavenInvocationException Thrown if the maven invoker is called
     * wrongly.
     */
    public static String buildWorkspace(String workspaceName, String challengeName) {
        try {

            beforeAction(workspaceName, challengeName);

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
     * @throws IOException Thrown when an I/O exception occurs.
     * @throws MavenInvocationException Thrown if the maven invoker is called
     * wrongly.
     */
    public static String testAll(String workspaceName, String challengeName) {
        try {
            beforeAction(workspaceName, challengeName);

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
     * @throws IOException Thrown when an I/O exception occurs.
     * @throws MavenInvocationException Thrown if the maven invoker is called
     * wrongly.
     */
    public static String test(String workspaceName, String challengeName, String testFile, String testName) {
        try {
            beforeAction(workspaceName, challengeName);

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
        return null;
    }

    private static void beforeAction(String workspaceName, String challengeName) throws IOException {
        //create target dir
        String projectDir = basePath + workspaceName + "\\" + challengeName;
        File targetFolder = new File(projectDir + "\\target");
        if (!targetFolder.exists()) {
            targetFolder.mkdir();
        }
        //init writer
        outputWriter = new FileWriter(targetFolder + "\\output.txt", false);
        bufferedWriter = new BufferedWriter(outputWriter);
        //setup the request
        request = new DefaultInvocationRequest();
        request.setPomFile(new File(projectDir + "\\pom.xml"));
    }

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

    private static String updateFile(String teamName, String filePath, String fileContent) {
        File oldName = new File(defaultPath + "/" + teamName + "/" + filePath);
        File newName = new File(oldName + ".temp");
        if (oldName.exists()) {
            if (oldName.renameTo(newName)) {
                System.out.println("renamed: " + oldName + " to: " + newName);
            } else {
                System.out.println("Error while renaming");
                return "error while renaming";
            }
        }else {
            return "file not found";
        }

        try {
            PrintWriter writer = new PrintWriter(oldName, "UTF-8");
            writer.printf(fileContent);
            writer.close();
        } catch (FileNotFoundException ex) {
            newName.renameTo(oldName);
            return ("Error File not found: " + ex);
        } catch (UnsupportedEncodingException ex) {
            newName.renameTo(oldName);
            return ("Error Unsupported Encoding: " + ex);
        }

        return "File succesful Updated";
    }
}
