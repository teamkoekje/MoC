package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;
import workspace.Action;
import workspace.Request;

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
        WorkspaceManagement instance = new WorkspaceManagement();
        System.out.println("creating workspace");
        //create
        Request createRequest = new Request(Action.CREATE, teamName);
        instance.processRequest(createRequest);
        //confirm
        File f = new File(WorkspaceManagement.defaultPath + teamName);
        assertTrue(f.exists());
        assertTrue(f.isDirectory());
        assertEquals(f.listFiles().length, 0);
        //remove
        System.out.println("removing workspace");
        Request removeRequest = new Request(Action.DELETE, teamName);
        instance.processRequest(removeRequest);
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
    @Test
    public void testExtractChallenge() {
        //init
        System.out.println("extracting challenge");
        WorkspaceManagement instance = new WorkspaceManagement();
        instance.createWorkspace("team a");
        instance.createWorkspace("team b");
        //extract (push)
        Request extractRequest = new Request(Action.PUSH_CHALLENGE, "");
        extractRequest.setChallengeName("test challenge");
        instance.processRequest(extractRequest);
        //confirm
        File teamAFile1 = new File(WorkspaceManagement.defaultPath + "team a\\test challenge\\some text.txt");
        File teamAFile2 = new File(WorkspaceManagement.defaultPath + "team a\\test challenge\\a sub folder\\pizza.java");
        File teamBFile1 = new File(WorkspaceManagement.defaultPath + "team b\\test challenge\\some text.txt");
        File teamBFile2 = new File(WorkspaceManagement.defaultPath + "team b\\test challenge\\a sub folder\\pizza.java");
        assertTrue(teamAFile1.exists());
        assertTrue(teamAFile2.exists());
        assertTrue(teamBFile1.exists());
        assertTrue(teamBFile2.exists());
        //cleanup
        instance.removeWorkspace("team a");
        instance.removeWorkspace("team b");
    }

    @Test
    public void testUpdateFile() {
        System.out.println("updating file");
        try {
            //init
            String originalContent = "this is test text";
            String newContent = "new content";
            WorkspaceManagement instance = new WorkspaceManagement();
            instance.createWorkspace("team c");
            File f = new File(WorkspaceManagement.defaultPath + "team c\\test file.txt");
            f.createNewFile();
            FileWriter fw = new FileWriter(f);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(originalContent);
            }
            //confirm initial data
            String temp = new String(Files.readAllBytes(Paths.get(f.getPath())));
            assertEquals(temp, originalContent);
            //update
            Request updateRequest = new Request(Action.UPDATE, "team c");
            updateRequest.setFilePath("test file.txt");
            updateRequest.setFileContent(newContent);
            instance.processRequest(updateRequest);
            //confirm new data
            String temp2 = new String(Files.readAllBytes(Paths.get(f.getPath())));
            assertEquals(newContent, temp2);
            //cleanup
            instance.removeWorkspace("team c");
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
