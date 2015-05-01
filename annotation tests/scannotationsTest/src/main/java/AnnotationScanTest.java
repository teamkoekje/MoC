
import annotations.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import org.scannotation.*;

/**
 * @author Casper
 */
public class AnnotationScanTest {

    private static List<String> visibleClasses;
    private static AnnotationDB db = new AnnotationDB();
    
    private static List<String> editables;

    public static void main(String[] args) {
        try {
            //the jar path
            URL testJarUrl = new URL("file:///" + System.getProperty("user.dir") + "\\src\\main\\java\\resources\\annotatedProject-1.0.jar");
            System.out.println("Using the following jar to determine the annotations:\n" + testJarUrl.getPath());
            //setup scanner & scan

            db.scanArchives(testJarUrl);
            Map<String, Set<String>> annotationIndex = db.getAnnotationIndex();
            visibleClasses = new ArrayList<>();
            //iterate over results
            processResults("Challenge", annotationIndex.get(Challenge.class.getName()));
            processResults("Editable", annotationIndex.get(Editable.class.getName()));
            processResults("ReadOnly", annotationIndex.get(ReadOnly.class.getName()));
            processResults("Hint", annotationIndex.get(Hint.class.getName()));
            
            editables = new ArrayList<>();
            for(String s : annotationIndex.get(Editable.class.getName())){
                String[] parts = s.split("\\.");
                String lastPart = parts[parts.length - 1];
                editables.add(lastPart);
            }
            
            
            //lets print out how an example project (with some annotated classes) would look like to a particiapant
            String exampleToPrintPath = System.getProperty("user.dir") + "\\src\\main\\example to print";
            System.out.println("\nPrinting folder structure as a participant would see it:\n" + exampleToPrintPath + "\n");
            listVisibleStructureForParticipant(1, new File(exampleToPrintPath));
            System.out.println(listStructureJSON(new File(exampleToPrintPath)).build());
            
            System.out.println(getFileJSON(System.getProperty("user.dir") + "\\src\\main\\example to print\\src\\main\\java\\projectwithannotations\\annotatedproject\\challenge\\NonEditableClass.java"));
            System.out.println(getFileJSON(System.getProperty("user.dir") + "\\src\\main\\example to print\\src\\main\\java\\projectwithannotations\\annotatedproject\\challenge\\ChallengeImpl.java"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static void processResults(String classname, Set<String> results) {
        if (results != null) {
            System.out.println("Classes using the annotation " + classname + ":");
            for (String s : results) {
                String[] split = s.split("\\.");
                visibleClasses.add(split[split.length - 1]);
                System.out.println(s);
            }
            System.out.println();
        } else {
            System.out.println("No classes using the annotation: " + classname + " - If there should be, does the annotation have @Retention(RetentionPolicy.RUNTIME)?");
        }
    }

    private static JsonArrayBuilder listFileJSON(File file) {
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

    private static JsonObjectBuilder listStructureJSON(File folderToShow) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        String temp = folderToShow.getName();
        if ((folderToShow.isFile() && isVisible(temp) || folderToShow.isDirectory())) {
            jsonObjectBuilder.add(temp, listFileJSON(folderToShow));
        }
        return jsonObjectBuilder;
    }

    private static void listVisibleStructureForParticipant(int indent, File folderToShow) {
        String temp = folderToShow.getName();
        //if it is file visible to participants or it is a folder
        if ((folderToShow.isFile() && isVisible(temp) || folderToShow.isDirectory())) {
            //print a bunch of - and the name
            for (int i = 0; i < indent; i++) {
                System.out.print('-');
            }
            System.out.println(temp);

            //if it is a diretory, process the files below it
            if (folderToShow.isDirectory()) {
                File[] files = folderToShow.listFiles();
                for (File file : files) {
                    listVisibleStructureForParticipant(indent + 1, file);
                }
            }
        }
    }

    private static boolean isVisible(String classFileName) {
        for (String s : visibleClasses) {
            if (classFileName.startsWith(s)) {
                return true;
            }
        }
        return false;
    }
    
    private static Boolean isFileEditable(String filePath){
        File f = new File(filePath);
        String filename = f.getName().substring(0, f.getName().length() - 5);
        return editables.contains(filename);
    }

    public static String getFileJSON(String filePath) throws IOException {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        
        Path file = Paths.get(filePath);
        String filecontent = new String(Files.readAllBytes(file));

        jsonObjectBuilder.add("filecontent", filecontent);
        jsonObjectBuilder.add("editable", isFileEditable(filePath));
        return jsonObjectBuilder.build().toString();
    }
}
