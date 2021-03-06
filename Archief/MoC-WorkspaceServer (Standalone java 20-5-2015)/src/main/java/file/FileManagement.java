/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import annotations.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import org.scannotation.AnnotationDB;

/**
 *
 * @author Luc
 */
//singleton
public class FileManagement {

    private List<String> visibleClasses;
    private AnnotationDB db;
    private Map<String, Set<String>> annotationIndex;
    private ArrayList<String> editables;
    
    private static final Map<String, FileManagement> fileManagers = new HashMap<>();
    /**
     * Get an instance of FileManagement
     *
     * @param filepath Path to the .jar of the challange
     * @return A FileManagement instance
     */
    public static FileManagement getInstance(String filepath){
        if(fileManagers.get(filepath) != null){
            return fileManagers.get(filepath);
        } else {
            fileManagers.put(filepath, new FileManagement(filepath));
            return getInstance(filepath);
        }
    }

    private FileManagement(String filepath) {
        try {
            db = new AnnotationDB();
            URL testJarUrl = new URL("file:///" + filepath);
            db.scanArchives(testJarUrl);
            annotationIndex = db.getAnnotationIndex();

            visibleClasses = new ArrayList<>();
            addToVisibleClasses(annotationIndex.get(Challenge.class.getName()));
            addToVisibleClasses(annotationIndex.get(Editable.class.getName()));
            addToVisibleClasses(annotationIndex.get(ReadOnly.class.getName()));
            addToVisibleClasses(annotationIndex.get(Hint.class.getName()));

            editables = new ArrayList<>();
            for (String s : annotationIndex.get(Editable.class.getName())) {
                String[] parts = s.split("\\.");
                String lastPart = parts[parts.length - 1];
                editables.add(lastPart);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getFolderJSON(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            return null;
        }
        return listStructureJSON(folder).build().toString();
    }

    private Boolean isFileEditable(String filePath) {
        File f = new File(filePath);
        String filename = f.getName().substring(0, f.getName().length() - 5);
        return editables.contains(filename);
    }

    public String getFileJSON(String filePath) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        try {
            Path file = Paths.get(filePath);
            String filecontent = new String(Files.readAllBytes(file));

            jsonObjectBuilder.add("filecontent", filecontent);
            jsonObjectBuilder.add("editable", isFileEditable(filePath));
        } catch (IOException ex) {
            Logger.getLogger(FileManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonObjectBuilder.build().toString();
    }

    private void addToVisibleClasses(Set<String> results) {
        if (results != null) {
            for (String s : results) {
                String[] split = s.split("\\.");
                visibleClasses.add(split[split.length - 1]);
            }
        }
    }

    private JsonObjectBuilder listStructureJSON(File folderToShow) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        String temp = folderToShow.getName();
        if ((folderToShow.isFile() && isVisible(temp) || folderToShow.isDirectory())) {
            jsonObjectBuilder.add(temp, listFileJSON(folderToShow));
        }
        return jsonObjectBuilder;
    }

    private JsonArrayBuilder listFileJSON(File file) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                jsonArrayBuilder.add(listStructureJSON(f));
            } else if (isVisible(f.getName())) {
                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("filename", f.getName());
                job.add("editable", isFileEditable(f.getName()));
                jsonArrayBuilder.add(job);
            }
        }
        return jsonArrayBuilder;
    }

    private boolean isVisible(String classFileName) {
        for (String s : visibleClasses) {
            if (classFileName.startsWith(s)) {
                return true;
            }
        }
        return false;
    }
}
