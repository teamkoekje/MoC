package management;

import controllers.PathController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Assume;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Casper
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkspaceManagementTest {

    private final String compName = "testCompetition";
    private final String teamName = "testTeam";
    private final String challengeName = "testChallenge";
    private final String testFile = "test file.txt";

    public WorkspaceManagementTest() {
    }

    /**
     * Test of getInstance method, of class WorkspaceManagement.
     */
    @Test
    public void test1GetInstance() {
        WorkspaceManagement wm1 = WorkspaceManagement.getInstance();
        WorkspaceManagement wm2 = WorkspaceManagement.getInstance();
        assertTrue(wm1 != null);
        assertTrue(wm2 != null);
        assertEquals(wm1, wm2);
    }

    /**
     * Test of createWorkspace method, of class WorkspaceManagement.
     */
    @Test
    public void test2CreateWorkspace() {
        WorkspaceManagement wm = WorkspaceManagement.getInstance();
        PathController pc = PathController.getInstance();
        assertEquals("Created workspace for team: " + teamName, wm.createWorkspace(compName, teamName));
        File f = new File(pc.teamPath(compName, teamName));
        assertTrue(f.exists() && f.isDirectory());
    }

    /**
     * Test of removeWorkspace method, of class WorkspaceManagement.
     */
    @Test
    public void test3RemoveWorkspace() {
        WorkspaceManagement wm = WorkspaceManagement.getInstance();
        PathController pc = PathController.getInstance();
        wm.removeWorkspace(compName, teamName);
        File f = new File(pc.teamPath(compName, teamName));
        assertFalse(f.exists());
        assertTrue(wm.removeWorkspace(compName, teamName).startsWith("Error"));
    }

    /**
     * Test of getTeamsCount method, of class WorkspaceManagement.
     */
    @Test
    public void test4GetTeamsCount() {
        WorkspaceManagement wm = WorkspaceManagement.getInstance();
        assertEquals(0, wm.getTeamsCount());
        wm.createWorkspace(compName, teamName);
        assertEquals(1, wm.getTeamsCount());
        wm.createWorkspace(compName, "testTeam2");
        assertEquals(2, wm.getTeamsCount());
    }

    /**
     * Test of extractChallenge method, of class WorkspaceManagement.
     */
    @Test
    public void test5ExtractChallenge() {
        Assume.assumeTrue(System.getProperty("os.name").equalsIgnoreCase("windows"));
        Assume.assumeTrue(new File("C:\\MoC\\Challenges\\testChallenge.zip").exists());
        WorkspaceManagement wm = WorkspaceManagement.getInstance();
        PathController pc = PathController.getInstance();
        Path p = Paths.get(pc.getDefaultPath() + "/Challenges/" + challengeName + ".zip");
        try {
            byte[] b = Files.readAllBytes(p);
            wm.extractChallenge(compName, challengeName, b);
            File f = new File(pc.teamChallengePath(compName, teamName, challengeName) + "/" + testFile);
            assertTrue(f.exists() && f.isFile());

            BufferedReader br;
            try (FileReader fr = new FileReader(f)) {
                br = new BufferedReader(fr);
                assertEquals(br.readLine(), "empty");
            }
            br.close();

        } catch (IOException ex) {
            Logger.getLogger(WorkspaceManagementTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of updateFile method, of class WorkspaceManagement.
     */
    @Test
    public void test6UpdateFile() {
        Assume.assumeTrue(System.getProperty("os.name").equalsIgnoreCase("windows"));
        Assume.assumeTrue(new File("C:\\MoC\\Competitions\\testCompetition\\Teams\\testTeam\\testChallenge\\test file.txt").exists());
        
        WorkspaceManagement wm = WorkspaceManagement.getInstance();
        PathController pc = PathController.getInstance();
        String testFilePath = pc.teamChallengePath(compName, teamName, challengeName + "/" + testFile);
        
        wm.updateFile(compName, teamName, testFilePath, "something");

        File f = new File(testFilePath);
        BufferedReader br = null;
        try (FileReader fr = new FileReader(f)) {
            br = new BufferedReader(fr);
            assertEquals(br.readLine(), "something");
        } catch (Exception ex) {
            Logger.getLogger(WorkspaceManagementTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (br != null) {
            try {br.close();} catch (IOException ex) {}
        }
    }
}
