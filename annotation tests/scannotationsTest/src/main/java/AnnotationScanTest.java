
import annotations.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.scannotation.*;

/**
 * @author Casper
 */
public class AnnotationScanTest {

    private static List<String> visibleClasses;

    public static void main(String[] args) {
        try {
            //the jar path
            URL testJarUrl = new URL("file:///" + System.getProperty("user.dir") + "\\src\\main\\java\\resources\\annotatedProject-1.0.jar");
            System.out.println("Using the following jar to determine the annotations:\n" + testJarUrl.getPath());
            //setup scanner & scan
            AnnotationDB db = new AnnotationDB();
            db.scanArchives(testJarUrl);
            Map<String, Set<String>> annotationIndex = db.getAnnotationIndex();
            visibleClasses = new ArrayList<>();
            //iterate over results
            processResults("Challenge", annotationIndex.get(Challenge.class.getName()));
            processResults("Editable", annotationIndex.get(Editable.class.getName()));
            processResults("ReadOnly", annotationIndex.get(ReadOnly.class.getName()));
            processResults("Hint", annotationIndex.get(Hint.class.getName()));
            //lets print out how an example project (with some annotated classes) would look like to a particiapant
            String exampleToPrintPath = System.getProperty("user.dir") + "\\src\\main\\example to print";
            System.out.println("\nPrinting folder structure as a participant would see it:\n" + exampleToPrintPath + "\n");
            listVisibleStructureForParticipant(1, new File(exampleToPrintPath));
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
}
