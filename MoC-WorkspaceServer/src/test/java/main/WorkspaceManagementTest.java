package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;
import workspace.CreateRequest;
import workspace.DeleteRequest;
import workspace.PushRequest;
import workspace.UpdateRequest;

/**
 *
 * @author Casper
 */
public class WorkspaceManagementTest {

    public WorkspaceManagementTest() {
    }

    /**
     * Test of createWorkspace method, of class WorkspaceManagement.
     */
    @Test
    public void testCreateAndRemoveWorkspace() {
        //init
        String teamName = "testTeam";
        String competitionName = "testCompetition";
        WorkspaceManagement instance = new WorkspaceManagement();
        System.out.println("creating workspace");
        //create
        CreateRequest cr = new CreateRequest(competitionName, teamName);
        instance.processRequest(cr);
        //confirm
        File f = new File(instance.getDefaultPath()
                + File.separator
                + "Competitions"
                + File.separator
                + competitionName
                + File.separator
                + "Teams"
                + File.separator
                + teamName);
        assertTrue(f.exists());
        assertTrue(f.isDirectory());
        assertEquals(f.listFiles().length, 0);
        //remove
        System.out.println("removing workspace");
        DeleteRequest dr = new DeleteRequest(competitionName, teamName);
        instance.processRequest(dr);
        //confirm
        assertFalse(f.exists());
    }

    /**
     * Test of extractChallenge method, of class WorkspaceManagement. This test
     * assumes there exists an 'test challenge.zip' under the defaultPath of the
     * WorkspaceManagement. This zip should contain the following structure: -
     * test challenge the top folder - a sub folder second folder pizza.java
     * file in second folder some text.txt file in first folder
     */
    //@Test
    public void testExtractChallenge() {
        //init
        System.out.println("extracting challenge");
        String competitionName = "testCompetition";
        WorkspaceManagement instance = new WorkspaceManagement();
        CreateRequest cr = new CreateRequest(competitionName, "team 1");
        CreateRequest cr2 = new CreateRequest(competitionName, "team 2");
        instance.processRequest(cr);
        instance.processRequest(cr2);

        //extract (push)
        //byte[] data = null;
        PushRequest pr = new PushRequest(competitionName, "test challenge");
        instance.processRequest(pr);
        //confirm
        File f = new File(instance.getDefaultPath()
                + File.separator
                + "Competitions"
                + File.separator
                + competitionName
                + File.separator
                + "Teams"
                + File.separator);
        File teamAFile1 = new File(f.getAbsolutePath() + "/team a/test challenge/some text.txt");
        File teamAFile2 = new File(f.getAbsolutePath() + "/team a/test challenge/a sub folder/pizza.java");
        File teamBFile1 = new File(f.getAbsolutePath() + "/team b/test challenge/some text.txt");
        File teamBFile2 = new File(f.getAbsolutePath() + "/team b/test challenge/a sub folder/pizza.java");
        assertTrue(teamAFile1.exists());
        assertTrue(teamAFile2.exists());
        assertTrue(teamBFile1.exists());
        assertTrue(teamBFile2.exists());
        //cleanup
        instance.removeWorkspace(competitionName, "team a");
        instance.removeWorkspace(competitionName, "team b");
    }

    @Test
    public void testUpdateFile() {
        System.out.println("updating file");
        try {
            //init
            String competitionName = "testCompitition";
            String originalContent = "this is test text";
            String newContent = "new content";
            WorkspaceManagement instance = new WorkspaceManagement();
            CreateRequest cr = new CreateRequest(competitionName, "team c");
            instance.processRequest(cr);
            File f = new File(instance.getDefaultPath()
                    + File.separator
                    + "Competitions"
                    + File.separator
                    + competitionName
                    + File.separator
                    + "Teams"
                    + File.separator
                    + "team c"
                    + File.separator
                    + "test file.txt");
            assertTrue(f.createNewFile());
            FileWriter fw = new FileWriter(f);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(originalContent);
            }
            //confirm initial data
            String temp = new String(Files.readAllBytes(Paths.get(f.getPath())));
            assertEquals(temp, originalContent);
            //update
            UpdateRequest ur = new UpdateRequest(competitionName, "team c", "test file.txt", newContent);
            String result = instance.processRequest(ur);
            assertEquals(result, "File succesfully Updated");
            //confirm new data
            String temp2 = new String(Files.readAllBytes(Paths.get(f.getPath())));
            assertEquals(newContent, temp2);
            //cleanup
            instance.removeWorkspace(competitionName, "team c");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="The following tests have already been tested In the MavenInvokerTest project check there">
    /**
     * Test of buildWorkspace method, of class WorkspaceManagement.
     */
    //@Test
    public void testBuildWorkspace() {
    }

    /**
     * Test of testAll method, of class WorkspaceManagement.
     */
    //@Test
    public void testTestAll() {
    }

    /**
     * Test of test method, of class WorkspaceManagement.
     */
    //@Test
    public void testTest() {
    }

    // </editor-fold>
}
