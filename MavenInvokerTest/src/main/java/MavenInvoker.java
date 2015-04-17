import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

/**
 * 
 * @author Casper
 */
public class MavenInvoker {

    private static final File mavenHome = new File("D:\\Software\\Programming\\Maven\\apache-maven-3.2.5");
    private static final String basePath = "D:\\College\\Software Course\\S6\\PTS6\\MoC\\MavenInvokerTest\\MoC\\workspaces";
    private static final Invoker invoker = new DefaultInvoker();

    private static FileWriter outputWriter;
    private static BufferedWriter bufferedWriter;
    private static InvocationRequest request;

    public static void main(String[] args) throws IOException {
        //init invoker
        invoker.setMavenHome(mavenHome);
        invoker.setOutputHandler(new InvocationOutputHandler() {
            @Override
            public void consumeLine(String string) {
                try {
                    System.out.println(string);
                    bufferedWriter.write(string + "\n");
                } catch (IOException ex) {
                    System.err.println(ex.getLocalizedMessage());
                }
            }
        });

        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.println("attempting to build: teamA (compilation error)");
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        try {
            build("teamA", "projectToTest");
        } catch (Exception ex) {
            System.err.println("message: " + ex.getLocalizedMessage());
            System.out.println("stack trace: ");
            ex.printStackTrace();
        } finally {
            bufferedWriter.close();
        }
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.println("teamA done");
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.println("attempting to run 2 tests: teamB (should all succeed)");
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        try {
            testAll("teamB", "projectToTest");
        } catch (Exception ex) {
            System.err.println("message: " + ex.getLocalizedMessage());
            System.out.println("stack trace: ");
            ex.printStackTrace();
        } finally {
            bufferedWriter.close();
        }
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.println("teamB done");
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.println("Attempting to run a specific test: teamC (should succeed)");
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        try {
            test("teamC", "projectToTest", "AnAmazingClassTest", "testAMethod");
        } catch (Exception ex) {
            System.err.println("message: " + ex.getLocalizedMessage());
            System.out.println("stack trace: ");
            ex.printStackTrace();
        } finally {
            bufferedWriter.close();
        }
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.println("teamC done");
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.println("Attempts to run 3 tests: teamD (third should fail)");
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        try {
            testAll("teamD", "projectToTest");
        } catch (Exception ex) {
            System.err.println("message: " + ex.getLocalizedMessage());
            System.out.println("stack trace: ");
            ex.printStackTrace();
        } finally {
            bufferedWriter.close();
        }
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.println("teamD done");
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.println("Attempts to run 4 tests: teamE (second & fourth should fail)");
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        try {
            testAll("teamE", "projectToTest");
        } catch (Exception ex) {
            System.err.println("message: " + ex.getLocalizedMessage());
            System.out.println("stack trace: ");
            ex.printStackTrace();
        } finally {
            bufferedWriter.close();
        }
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.println("teamA done");        
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
    }

    /**
     * Attempts to build the specified challenge in the specified workspace. 
     * Output is found in: *\\MoC\\workspaces\\workspaceName\\challengeName\\output.txt
     * @param workspaceName The name of the workspace which should be used
     * @param challengeName The name of the challenge to be build
     * @throws IOException Thrown when an I/O exception occurs.
     * @throws MavenInvocationException Thrown if the maven invoker is called wrongly.
     */
    public static void build(String workspaceName, String challengeName) throws IOException, MavenInvocationException {
        beforeAction(workspaceName, challengeName);

        request.setGoals(Arrays.asList("install"));
        request.setProperties(new Properties());

        invoker.execute(request);
    }
    
    /**
     * Attempts to test all tests for the specified challenge in the specified workspace. 
     * Output is found in: *\\MoC\\workspaces\\workspaceName\\challengeName\\output.txt
     * @param workspaceName The name of the workspace which should be used
     * @param challengeName The name of the challenge to be tested
     * @throws IOException Thrown when an I/O exception occurs.
     * @throws MavenInvocationException Thrown if the maven invoker is called wrongly.
     */
    public static void testAll(String workspaceName, String challengeName) throws IOException, MavenInvocationException {
        beforeAction(workspaceName, challengeName);

        request.setGoals(Arrays.asList("test"));
        request.setProperties(new Properties());

        invoker.execute(request);
    }
    
    /**
     * Attempts to test a specific test for the specified challenge in the specified workspace. 
     * Output is found in: *\\MoC\\workspaces\\workspaceName\\challengeName\\output.txt
     * @param workspaceName The name of the workspace which should be used
     * @param challengeName The name of the challenge to be tested
     * @throws IOException Thrown when an I/O exception occurs.
     * @throws MavenInvocationException Thrown if the maven invoker is called wrongly.
     */
    public static void test(String workspaceName, String challengeName, String testFile, String testName) throws IOException, MavenInvocationException {
        beforeAction(workspaceName, challengeName);

        request.setGoals(Arrays.asList("test"));
        Properties p = new Properties();
        p.setProperty("test", (testFile + "#" + testName));
        request.setProperties(p);

        invoker.execute(request);
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
}
