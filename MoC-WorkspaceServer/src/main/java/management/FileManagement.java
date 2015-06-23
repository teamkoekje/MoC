package management;

// <editor-fold defaultstate="collapsed" desc="imports" >
import annotations.*;
import controllers.PathController;
import java.io.File;
import java.io.IOException;
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
import org.scannotation.AnnotationDB;
import org.testng.annotations.Test;
// </editor-fold>

/**
 *
 * @author TeamKoekje
 */
public class FileManagement {

    // <editor-fold defaultstate="collapsed" desc="variables" >
    private List<String> visibleClasses;
    private AnnotationDB db;
    private Map<String, Set<String>> annotationIndex;
    private List<String> editables;
    private final List<String> userTests;
    private final List<String> systemTests;
    private final List<String> ambivalentTests;
    private final String jarPath;
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="constructor(s)" >
    private static final List<FileManagement> FILEMANAGERS = new ArrayList<>();
    private static final PathController PATHINSTANCE = PathController.getInstance();

    private FileManagement(String jarPath) {
        //init variables
        userTests = new ArrayList<>();
        systemTests = new ArrayList<>();
        ambivalentTests = new ArrayList<>();
        visibleClasses = new ArrayList<>();
        editables = new ArrayList<>();
        db = new AnnotationDB();
        this.jarPath = jarPath;

        try {
            System.out.println(jarPath);
            URL jarUrl = new URL("file:///" + jarPath);
            db.scanArchives(jarUrl);
            annotationIndex = db.getAnnotationIndex();

            addToVisibleClasses(annotationIndex.get(Editable.class.getName()));
            addToVisibleClasses(annotationIndex.get(ReadOnly.class.getName()));
            addToVisibleClasses(annotationIndex.get(Hint.class.getName()));

            for (String s : annotationIndex.get(Editable.class.getName())) {
                String[] parts = s.split("\\.");
                String lastPart = parts[parts.length - 1];
                editables.add(lastPart);
            }

            scanTests();
        } catch (IOException ex) {
            Logger.getLogger(FileManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void scanTests() {
        String testJarPath = jarPath.substring(0, jarPath.length() - 4);
        testJarPath += "-tests.jar";
        try {
            JarFile jarFile = new JarFile(testJarPath);
            Enumeration e = jarFile.entries();
            URL[] testJarUrl = {new URL("jar:file:" + testJarPath + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(testJarUrl, Thread.currentThread().getContextClassLoader());
            while (e.hasMoreElements()) {
                JarEntry je = (JarEntry) e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                Class c = cl.loadClass(className);
                Test t = (Test) c.getAnnotation(Test.class);
                if (t.groups().length == 2) {
                    ambivalentTests.add(t.testName());
                } else {
                    switch (t.groups()[0]) {
                        case "user":
                            userTests.add(t.testName());
                            break;
                        case "system":
                            systemTests.add(t.testName());
                            break;
                        default:
                            throw new AssertionError("unexpected value: " + t.groups()[0]);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get an instance of FileManagement
     *
     * @param competitionId
     * @param challengeName
     * @return A FileManagement instance
     */
    public static FileManagement getInstance(Long competitionId, String challengeName) {
        String jarPath = PATHINSTANCE.challengesPath(Long.toString(competitionId)) + File.separator + challengeName + ".jar";

        for (FileManagement f : FILEMANAGERS) {
            if (f.jarPath.equals(jarPath)) {
                return f;
            }
        }

        FileManagement f = new FileManagement(jarPath);
        FILEMANAGERS.add(f);
        return f;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter & Setters" >
    public List<String> getAvailableTests() {
        List toReturn = new ArrayList<>();
        toReturn.addAll(userTests);
        toReturn.addAll(ambivalentTests);
        return toReturn;
    }

    public String getFolderStructureJSON(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory() || (folder.isDirectory() && "target".equals(folder.getName()))) {
            return null;
        }
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        return listFolderJSON(folder, jsonArrayBuilder).build().toString();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    private Boolean isFileEditable(String filePath) {
        File f = new File(filePath);
        String filename = f.getName().substring(0, f.getName().length() - 5);
        return editables.contains(filename);
    }

    public String getFileContentJSON(String filePath) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        try {
            Path file = Paths.get(filePath);
            String filecontent = new String(Files.readAllBytes(file));
            jsonObjectBuilder.add("filecontent", filecontent);
            jsonObjectBuilder.add("filename", file.getFileName().toString());
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

    private JsonArrayBuilder listFolderJSON(File file, JsonArrayBuilder jsonArrayBuilder) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                if (!"target".equals(f.getName())) {
                    listFolderJSON(f, jsonArrayBuilder);
                }
            } else if (isVisible(f.getName()) || f.getName().endsWith("Participants.html")) {
                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("filename", f.getName());
                job.add("filepath", f.getAbsolutePath());
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

    public String getAvailableTestsJSON() {
        JsonArrayBuilder testsJson = Json.createArrayBuilder();
        for (String s : userTests) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("name", s);
            testsJson.add(job);
        }
        for (String s : ambivalentTests) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("name", s);
            testsJson.add(job);
        }
        return testsJson.build().toString();
    }
    //</editor-fold>
}
