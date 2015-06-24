package api;

import annotations.Hint;
import domain.Challenge;
import domain.Competition;
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
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import service.ChallengeService;
import service.CompetitionService;

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

    @Inject
    private CompetitionService competitionService;

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
     * @param challengeName
     * @return a challenge
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{challengeName}")
    public String getChallengeById(@PathParam("challengeName") String challengeName) {
        challengeName = challengeName.trim();
        String folder;
        String osName = System.getProperty("os.name");
        if ("linux".equalsIgnoreCase(osName)) {
            folder = "MoC" + File.separator + "Challenges" + File.separator;
        } else {
            folder = "C:" + File.separator + "MoC" + File.separator + "Challenges" + File.separator;
        }
        annotations.Challenge chal = scanChallenge(folder + challengeName + File.separator + challengeName + ".jar");
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("challengeName", chal.name());
        job.add("difficulty", chal.difficulty().toString());
        JsonObjectBuilder description = Json.createObjectBuilder();
        description.add("participant", chal.descriptionParticipants());
        description.add("spectator", chal.descriptionPublic());
        job.add("description", description);
        JsonArrayBuilder hints = Json.createArrayBuilder();
        for (Hint hint : chal.hints()) {
            JsonObjectBuilder hintBuilder = Json.createObjectBuilder();
            hintBuilder.add("delay", hint.delay());
            hintBuilder.add("content", hint.content());
            hints.add(hintBuilder);
        }
        job.add("hints", hints);
        JsonObjectBuilder authorBuilder = Json.createObjectBuilder();
        authorBuilder.add("name", chal.author());
        authorBuilder.add("organisation", chal.organisation());
        authorBuilder.add("website", chal.weblink());
        authorBuilder.add("logo", chal.logoUrl());
        job.add("author", authorBuilder);
        return job.build().toString();
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
            byte[] data = Base64.decodeBase64(fileContent.getBytes());
            String folder;
            String osName = System.getProperty("os.name");
            if ("linux".equalsIgnoreCase(osName)) {
                folder = "MoC" + File.separator + "Challenges" + File.separator;
            } else {
                folder = "C:" + File.separator + "MoC" + File.separator + "Challenges" + File.separator;
            }

            String jarFileName = fileName.substring(0, fileName.length() - 8);
            folder += jarFileName + File.separator;

            new File(folder).mkdirs();
            try (FileOutputStream fos = new FileOutputStream(folder + fileName)) {
                fos.write(data);
            }

            ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(data));
            for (ZipEntry zipEntry; (zipEntry = zipInputStream.getNextEntry()) != null;) {
                if (zipEntry.getName().endsWith(jarFileName + ".jar")) {
                    try (BufferedInputStream is = new BufferedInputStream(zipInputStream)) {
                        int currentByte;
                        byte[] data2 = new byte[2024];
                        FileOutputStream fos2 = new FileOutputStream(folder + jarFileName + ".jar");
                        try (BufferedOutputStream dest = new BufferedOutputStream(fos2, 2024)) {
                            while ((currentByte = is.read(data2, 0, 2024)) != -1) {
                                dest.write(data2, 0, currentByte);
                            }
                            dest.flush();
                        }
                        scanChallenge(folder + jarFileName + ".jar");
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
        new File(folder).mkdirs();
        for (File f : new File(folder).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        })) {
            jab.add(f.getName());
        }
        return jab.build().toString();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{competitionID}")
    public Response addToCompetition(
            @PathParam("competitionID") Long competitionId,
            domain.Challenge challenge
    ) {
        Competition c = competitionService.findById(competitionId);
        c.addChallenge(challenge, 100);
        competitionService.edit(c);
        competitionService.replaceFutureCompetition(c);
        return Response.ok(challenge).build();
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
