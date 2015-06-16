
import annotations.*;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import org.scannotation.*;
import org.testng.annotations.Test;
import projectwithannotations.annotatedproject.challenge.ChallengeExample;

/**
 * @author Casper
 */
public class AnnotationScanTest {

    private static List<String> visibleClasses;
    private static List<String> editableClasses;
    private static final AnnotationDB db = new AnnotationDB();

    private static final List<Challenge> challenges = new ArrayList<>();
    private static final List<Test> userTests = new ArrayList<>();
    private static final List<Test> systemTests = new ArrayList<>();
    private static final List<Test> ambivalentTests = new ArrayList<>();

    public static void main(String[] args) {
        try {
            //<editor-fold defaultstate="collapsed" desc="jar paths">
            String resourcePath = "file:///" + System.getProperty("user.dir") + "\\src\\main\\java\\resources\\";
            URL testJarUrl = new URL(resourcePath + "annotatedProject-1.0.jar");
            URL testJarUrl2 = new URL(resourcePath + "annotatedProject-1.0-tests.jar");
            URL[] urls = new URL[2];
            urls[0] = testJarUrl;
            urls[1] = testJarUrl2;
            System.out.println("Using the following jars to determine the annotated classes:\n" + testJarUrl.getPath() + "\n" + testJarUrl2.getPath());
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="scan">
            db.scanArchives(urls);
            Map<String, Set<String>> annotationIndex = db.getAnnotationIndex();
            visibleClasses = new ArrayList<>();
            //<editor-fold defaultstate="collapsed" desc="visible classes">
            processResults("Challenge", annotationIndex.get(Challenge.class.getName()));
            processResults("Editable", annotationIndex.get(Editable.class.getName()));
            processResults("ReadOnly", annotationIndex.get(ReadOnly.class.getName()));
            processResults("Hint", annotationIndex.get(Hint.class.getName()));
            // </editor-fold>
            //<editor-fold defaultstate="collapsed" desc="editable classes">
            editableClasses = new ArrayList<>();
            for (String s : annotationIndex.get(Editable.class.getName())) {
                String[] parts = s.split("\\.");
                String lastPart = parts[parts.length - 1];
                editableClasses.add(lastPart);
            }
            //</editor-fold>        
            //<editor-fold defaultstate="collapsed" desc="scan challenges">        
            System.out.println();
            System.out.println("______SCANNING FOR CHALLENGES______");
            String path = System.getProperty("user.dir") + "\\src\\main\\java\\resources\\annotatedProject-1.0.jar";
            Enumeration en = new JarFile(path).entries();

            URL[] jarUrl2 = {new URL("jar:file:" + path + "!/")};
            URLClassLoader cl2 = URLClassLoader.newInstance(jarUrl2);

            while (en.hasMoreElements()) {
                JarEntry je = (JarEntry) en.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                Class c = cl2.loadClass(className);
                System.out.println("__________________________________________");
                System.out.println("class name: " + c.getName());
                System.out.println("class simple name: " + c.getSimpleName());
                Challenge chal = (Challenge) c.getAnnotation(Challenge.class);
                if (chal != null) {
                    System.out.println("This class has a Challenge annotation! Values:");
                    System.out.println("Challenge name: " + chal.name());
                    System.out.println("Author: " + chal.author());
                    System.out.println("Organisation: " + chal.organisation());
                    System.out.println("Weblink: " + chal.weblink());
                    System.out.println("Difficulty: " + chal.difficulty().name());
                    int j = 1;
                    for (Hint h : chal.hints()) {
                        System.out.println("Hint" + j + ": delay: " + h.delay() + " : content: " + h.content());
                        j++;
                    }
                    challenges.add(chal);
                }
            }
            System.out.println("__________________________________________");
            System.out.println();
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="scan tests">   
            System.out.println();
            System.out.println("______SCANNING FOR TESTS______");
            String pathToJar = System.getProperty("user.dir") + "\\src\\main\\java\\resources\\annotatedProject-1.0-tests.jar";
            JarFile jarFile = new JarFile(pathToJar);
            Enumeration e = jarFile.entries();

            URL[] jarUrl = {new URL("jar:file:" + pathToJar + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(jarUrl);

            while (e.hasMoreElements()) {
                JarEntry je = (JarEntry) e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                Class c = cl.loadClass(className);
                System.out.println("__________________________________________");
                System.out.println("class name: " + c.getName());
                System.out.println("class simple name: " + c.getSimpleName());
                Test t = (Test) c.getAnnotation(Test.class);
                if (t.groups().length == 2) {
                    ambivalentTests.add(t);
                } else {
                    switch (t.groups()[0]) {
                        case "user":
                            userTests.add(t);
                            break;
                        case "system":
                            systemTests.add(t);
                            break;
                        default:
                            throw new AssertionError("unexpected value: " + t.groups()[0]);
                    }
                }
                System.out.println("test name: " + t.testName());
                System.out.println("test description: " + t.description());
                for (String s : t.groups()) {
                    System.out.println("-Group member: " + s);
                }
            }
            System.out.println("__________________________________________");
            // </editor-fold>
            // </editor-fold>
            //<editor-fold defaultstate="collapsed" desc="file structure and json return">
            //lets print out how an example project (with some annotated classes) would look like to a particiapant
            String exampleToPrintPath = System.getProperty("user.dir") + "\\src\\main\\example to print";
            System.out.println("\n_____FOLDER STRUCTURE AND JSON REPLY_____:\n" + exampleToPrintPath + "\n");
            listVisibleStructureForParticipant(1, new File(exampleToPrintPath));
            System.out.println(listStructureJSON(new File(exampleToPrintPath)).build());

            System.out.println(getFileJSON(System.getProperty("user.dir") + "\\src\\main\\example to print\\src\\main\\java\\projectwithannotations\\annotatedproject\\challenge\\NonEditableClass.java"));
            System.out.println(getFileJSON(System.getProperty("user.dir") + "\\src\\main\\example to print\\src\\main\\java\\projectwithannotations\\annotatedproject\\challenge\\ChallengeImpl.java"));
            //</editor-fold>
        } catch (Exception ex) {
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
        } else {
            System.out.println("No classes using the annotation: " + classname + " - If there should be, does the annotation have @Retention(RetentionPolicy.RUNTIME)?");
        }
        System.out.println();
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

    private static Boolean isFileEditable(String filePath) {
        File f = new File(filePath);
        String filename = f.getName().substring(0, f.getName().length() - 5);
        return editableClasses.contains(filename);
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
