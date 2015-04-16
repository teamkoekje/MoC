
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.codehaus.plexus.util.cli.CommandLineException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daan
 */
public class barry2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(new File("D:\\NetBeansProjects\\GitProjects\\barry2\\pom.xml"));
            request.setGoals(Arrays.asList("test"));
            Properties p = new Properties();
            p.setProperty("test", "barry2Test#test2");
            request.setProperties(p);

            Invoker invoker = new DefaultInvoker();
            File f = new File("C:\\Users\\Daan\\Desktop\\apache-maven-3.2.5");
            invoker.setMavenHome(f);
            
            InvocationResult result = invoker.execute(request);
            
            System.out.println("exit code: " + result.getExitCode());
            System.out.println("execution exception: ");
            CommandLineException executionException = result.getExecutionException();
            if(executionException!= null){
                System.out.println(executionException.getLocalizedMessage());
                executionException.printStackTrace();
            }
                
        } catch (Exception ex) {
            System.err.println("message: " + ex.getLocalizedMessage());
            System.out.println("stack trace: ");ex.printStackTrace();
        }

        //System.console().readLine();
    }

}
