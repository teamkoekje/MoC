/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Management;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import workspace.CreateRequest;
import workspace.FolderStructureRequest;
import workspace.PushRequest;

/**
 *
 * @author Robin
 */
public class TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WorkspaceManagement wm = WorkspaceManagement.getInstance();
        CreateRequest cr = new CreateRequest("competition1", "team1");
        wm.processRequest(cr);

        Path path = Paths.get("C:/challenge1.zip");
        byte[] data = null;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException ex) {
            Logger.getLogger(TestMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        PushRequest pr = new PushRequest("competition1", "challenge1", data);
        wm.processRequest(pr);

        FolderStructureRequest fsr = new FolderStructureRequest("competition1", "challenge1", "team1");
        System.out.println(wm.processRequest(fsr));
    }

}
