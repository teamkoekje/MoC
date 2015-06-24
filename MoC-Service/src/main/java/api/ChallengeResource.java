package api;

import domain.Challenge;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import service.ChallengeService;

/**
 *
 * @author TeamKoekje
 */
@Stateless
@Path("challenge")
@DeclareRoles({"Admin"})
public class ChallengeResource {

    @Inject
    private ChallengeService challengeService;

    //<editor-fold defaultstate="collapsed" desc="Challenge">
    /**
     * Gets all challenges
     *
     * @return list with challenges
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Challenge> getChallenges() {
        return challengeService.findAll();
    }

    /**
     * Gets a challenge with a certain id
     *
     * @param challengeId id of the challenge
     * @return a challenge
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{challengeId}")
    public Challenge getChallengeById(@PathParam("challengeId") long challengeId) {
        return challengeService.findById(challengeId);
    }

    /**
     * Create a new challenge
     *
     * @param fileContent The file as a base64 encoded string
     * @param fileName The name of the file
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void createChallenge(
            @FormDataParam("fileContent") String fileContent,
            @FormDataParam("fileName") String fileName
    ) {
        try {
            System.out.println(fileContent);
            System.out.println(fileName);

            byte[] data = Base64.decodeBase64(fileContent.getBytes());
            String folder;
            String osName = System.getProperty("os.name");
            if ("linux".equalsIgnoreCase(osName)) {
                folder = "MoC" + File.separator + "Challenges" + File.separator;
            } else {
                folder = "C:" + File.separator + "MoC" + File.separator + "Challenges" + File.separator;
            }
            new File(folder).mkdirs();
            try (FileOutputStream fos = new FileOutputStream(folder + File.separator + fileName)) {
                fos.write(data);
            }

            String jarFileName = fileName.substring(0, fileName.length() - 8) + ".jar";
            ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(data));
            for (ZipEntry zipEntry; (zipEntry = zipInputStream.getNextEntry()) != null;) {
                System.out.println(zipEntry.getName());
                if (zipEntry.getName().endsWith(jarFileName)) {
                    try (BufferedInputStream is = new BufferedInputStream(zipInputStream)) {
                        int currentByte;
                        byte[] data2 = new byte[2024];
                        FileOutputStream fos2 = new FileOutputStream(folder + File.separator + jarFileName);
                        try (BufferedOutputStream dest = new BufferedOutputStream(fos2, 2024)) {
                            while ((currentByte = is.read(data2, 0, 2024)) != -1) {
                                dest.write(data2, 0, currentByte);
                            }
                            dest.flush();
                        }
                        scanChallenge(folder + "/" + jarFileName);
                        return;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ChallengeResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/available")
    public String getAvailableChallenges() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        String folder;
        String osName = System.getProperty("os.name");
        if ("linux".equalsIgnoreCase(osName)) {
            folder = "MoC" + File.separator + "Challenges" + File.separator;
        } else {
            folder = "C:" + File.separator + "MoC" + File.separator + "Challenges" + File.separator;
        }
        for (File f : new File(folder).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".zip");
            }
        })) {
            jab.add(f.getName().substring(0, f.getName().length() - 4));
        }
        return jab.build().toString();
    }

    private annotations.Challenge scanChallenge(String jarPath) {
        try {
            System.out.println(jarPath);
            JarFile jarFile = new JarFile(jarPath);
            Enumeration e = jarFile.entries();
            URL[] testJarUrl = {new URL("jar:file:" + jarPath + "!/")};
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
                System.out.println(c.getCanonicalName());
                annotations.Challenge chalAnno = (annotations.Challenge) c.getAnnotation(annotations.Challenge.class);
                if (chalAnno != null) {
                    return chalAnno;
                }
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ChallengeResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Updates a challenge
     *
     * @param challenge challenge with the updated information
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/update")
    public void editChallenge(Challenge challenge) {
        challengeService.edit(challenge);
    }

    /**
     * Deletes a team
     *
     * @param challengeId id of the challenge that should be deleted
     */
    @DELETE
    @Path("/{challengeId}")
    public void removeChallenge(@PathParam("challengeId") long challengeId) {
        challengeService.remove(challengeId);
    }
    //</editor-fold>
}
