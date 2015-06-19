package api;

import domain.Challenge;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import service.ChallengeService;

/**
 *
 * @author Astrid
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
    @Produces("application/xml,application/json")
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
    @Produces("application/xml,application/json")
    @Path("/{challengeId}")
    public Challenge getChallengeById(@PathParam("challengeId") long challengeId) {
        return challengeService.findById(challengeId);
    }

    /**
     * Create a new challenge
     * @param file The file as a base64 encoded string
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createChallenge(String file) {
        try {
            byte[] data = Base64.decodeBase64(file.getBytes());
            try (OutputStream stream = new FileOutputStream("C:\\Luc\\yourFile2.zip")) {
                stream.write(data);
            }
        } catch (IOException ex) {
            Logger.getLogger(ChallengeResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        Challenge c = new Challenge("name");
        challengeService.create(c);
    }

    /**
     * Updates a challenge
     *
     * @param challenge challenge with the updated information
     */
    @POST
    @Consumes("application/xml,application/json")
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
