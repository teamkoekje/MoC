/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workspace;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author Daan
 */
public class WorkspaceManagment {

    public final String defaultPath = "C:/MoC/";
    public List<String> teams;

    public WorkspaceManagment() {
        teams = new ArrayList<>();
        getWorkspaceFolders();
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

    public String extractChallenge(String challengeName){

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

    public String buildWorkspace(String teamName){
        return null;
    }
    
}
