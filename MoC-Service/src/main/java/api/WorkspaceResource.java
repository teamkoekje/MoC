package api;

import com.sun.media.jfxmedia.logging.Logger;
import domain.Challenge;
import messaging.CodeFile;
import domain.Competition;
import domain.Team;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import service.CompetitionService;
import service.WorkspaceService;

/**
 *
 * @author TeamKoekje
 */
@Path("workspace")
public class WorkspaceResource {

    @Inject
    private WorkspaceService workspaceService;

    @Inject
    private CompetitionService competitionService;

    @Context
    private HttpServletRequest request;

    //<editor-fold defaultstate="collapsed" desc="Workspace">
    /**
     * Send a request to create a workspace TODO: send a reply
     *
     * @param competitionId The Id of the competition
     * @param teamName The name of the team
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{competitionId}/{teamName}/create")
    public void create(@PathParam("competitionId") long competitionId, @PathParam("teamName") String teamName) {
        String messageId = workspaceService.create(competitionId, teamName);
        workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
    }

    /**
     * Sends a request to delete a workspace. TODO: send a reply
     *
     * @param competitionId The Id of the competition
     * @param teamName The name of the team
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{competitionId}/{teamName}/delete")
    public void delete(@PathParam("competitionId") long competitionId, @PathParam("teamName") String teamName) {
        String messageId = workspaceService.delete(competitionId, teamName);
        workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
    }

    /**
     * Sends a request to update a file.
     *
     * @param competitionId The Id of the Competition.
     * @param file Data to update.
     * @return A Response indicating whether the request was send, or an error
     * occurred.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{competitionId}/update")
    public Response update(@PathParam("competitionId") long competitionId, CodeFile file) {
        Logger.logMsg(Logger.INFO, "Updating file: " + file.getFilePath());
        Competition competition = competitionService.findById(competitionId);
        if (competition != null) {
            Team team = competition.getTeamByUsername(request.getRemoteUser());
            if (team != null) {
                String messageId = workspaceService.update(competitionId, team.getName(), file.getFilePath(), file.getFileContent());
                workspaceService.storeRequestMessage(messageId, request.getRemoteUser());
                return Response.ok().build();
            } else {
                return Response.serverError().entity("Authenticated user isn't a participant in this competition").build();
            }
        } else {
            return Response.serverError().entity("The competition doesn't exist").build();
        }
    }

    /**
     * Sends a request to compile
     *
     * @param competitionId The Id of the competition
     * @param file Data to update before the code is build.
     * @return A Response indicating whether the request was send, or an error
     * occurred.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{competitionId}/compile")
    public Response compile(@PathParam("competitionId") long competitionId, CodeFile file) {
        Competition competition = competitionService.findById(competitionId);
        if (competition != null && competition.getCurrentRound() != null) {
            Team team = competition.getTeamByUsername(request.getRemoteUser());
            Challenge challenge = competition.getCurrentRound().getChallenge();
            if (team != null) {
                String messageId = workspaceService.compile(competitionId, team.getName(), challenge.getName(), file.getFilePath(), file.getFileContent());
                workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
                return Response.ok("Files compiled").build();
            } else {
                return Response.serverError().entity("Authenticated user isn't a participant in this competition").build();
            }
        } else {
            return Response.serverError().entity("The competition doesn't exist or isn't active at the moment").build();
        }
    }

    /**
     * Sends a request to submit, which first builds & tests, and if it
     * succeeds, submits the team.
     *
     * @param competitionId The Id of the competition.
     * @param file Data to update before the code is build.
     * @return A Response indicating whether the request was send, or an error
     * occurred.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{competitionId}/submit")
    public Response submit(@PathParam("competitionId") long competitionId, CodeFile file) {
        Competition competition = competitionService.findById(competitionId);
        if (competition != null && competition.getCurrentRound() != null) {
            Team team = competition.getTeamByUsername(request.getRemoteUser());
            Challenge challenge = competition.getCurrentRound().getChallenge();
            if (team != null) {
                String messageId = workspaceService.submit(competitionId, team.getName(), challenge.getName(), file.getFilePath(), file.getFileContent());
                workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
                return Response.ok("Attempting to submit...").build();
            } else {
                return Response.serverError().entity("Authenticated user isn't a participant in this competition").build();
            }
        } else {
            return Response.serverError().entity("The competition doesn't exist or isn't active at the moment").build();
        }
    }

    /**
     * Sends a request to test all tests.
     *
     * @param competitionId The Id of the competition
     * @param file Data to update before the tests are run
     * @return A Response indicating whether the request was send, or an error
     * occurred.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{competitionId}/test")
    public Response testAll(@PathParam("competitionId") long competitionId, CodeFile file) {
        Competition competition = competitionService.findById(competitionId);
        if (competition != null && competition.getCurrentRound() != null) {
            Team team = competition.getTeamByUsername(request.getRemoteUser());
            Challenge challenge = competition.getCurrentRound().getChallenge();
            if (team != null) {
                String messageId = workspaceService.testAll(competitionId, team.getName(), challenge.getName(), file.getFilePath(), file.getFileContent());
                workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
                return Response.ok("Tests are being executed").build();
            } else {
                return Response.serverError().entity("Authenticated user isn't a participant in this competition").build();
            }
        } else {
            return Response.serverError().entity("The competition doesn't exist or isn't active at the moment").build();
        }
    }

    /**
     * Sends a request to test a specific test.
     *
     * @param competitionId The Id of the competition
     * @param testName The name of the test to run
     * @param file Data to update before the test is run
     * @return A Response indicating whether the request was send, or an error
     * occurred.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{competitionId}/test/{testName}")
    public Response test(@PathParam("competitionId") long competitionId, @PathParam("testName") String testName, CodeFile file) {
        Competition competition = competitionService.findById(competitionId);
        System.out.println("TESTING: " + testName);
        if (competition != null && competition.getCurrentRound() != null) {
            Team team = competition.getTeamByUsername(request.getRemoteUser());
            Challenge challenge = competition.getCurrentRound().getChallenge();
            if (team != null) {
                String messageId = workspaceService.test(competitionId, team.getName(), challenge.getName(), testName, file.getFilePath(), file.getFileContent());
                workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
                return Response.ok("Test is being executed").build();
            } else {
                return Response.serverError().entity("Authenticated user isn't a participant in this competition").build();
            }
        } else {
            return Response.serverError().entity("The competition doesn't exist or isn't active at the moment").build();
        }
    }

    /**
     * Sends a request to push the specified challenge to the specified
     * competition.
     *
     * @param competitionId The Id of the competition
     * @param challengeName The name of the challenge
     * @return null TODO: send a proper Response object.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{competitionId}/{challengeName}/push")
    public String pushChallenge(@PathParam("competitionId") long competitionId, @PathParam("challengeName") String challengeName) {
        workspaceService.push(competitionId, challengeName);
        return null;
    }

    /**
     * Sends a request for the System information of the workspace servers.
     *
     * @return A Response indicating whether the requests were send properly.
     * TODO: it is assumed this always goes o.k.
     */
    @POST
    @Path("/sysinfo")
    public Response sysInfo() {
        workspaceService.sysInfo(request.getUserPrincipal().getName());
        return Response.ok().build();
    }

    /**
     * Sends a request for the folder structure of the current round in the
     * specified competition, in a JSON format.
     *
     * @param competitionId The Id of the competition
     * @return A Response indicating whether the request was sent, or an error
     * occurred,
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{competitionId}/folderStructure")
    public Response folderStructure(@PathParam("competitionId") long competitionId) {
        Competition competition = competitionService.findById(competitionId);
        if (competition != null && competition.getCurrentRound() != null) {
            Team team = competition.getTeamByUsername(request.getRemoteUser());
            Challenge challenge = competition.getCurrentRound().getChallenge();
            if (team != null) {
                String messageId = workspaceService.folderStructure(competitionId, challenge.getName(), team.getName());
                workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
                return Response.ok("Folder structure is being generated").build();
            } else {
                return Response.serverError().entity("Authenticated user isn't a participant in this competition").build();
            }
        } else {
            return Response.serverError().entity("The competition doesn't exist or isn't active at the moment").build();
        }
    }

    /**
     * Sends a request for the tests that are available to the participant, in a
     * JSON format.
     *
     * @param competitionId The Id of the Competition.
     * @return A Response indicating whether the request was send, or an error
     * occurred
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{competitionId}/availableTests")
    public Response availableTests(@PathParam("competitionId") long competitionId) {
        Competition competition = competitionService.findById(competitionId);
        if (competition != null && competition.getCurrentRound() != null) {
            Team team = competition.getTeamByUsername(request.getRemoteUser());
            Challenge challenge = competition.getCurrentRound().getChallenge();
            if (team != null) {
                String messageId = workspaceService.availableTests(competitionId, challenge.getName(), team.getName());
                workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
                return Response.ok("Available tests are being retrieved").build();
            } else {
                return Response.serverError().entity("Authenticated user isn't a participant in this competition").build();
            }
        } else {
            return Response.serverError().entity("The competition doesn't exist or isn't active at the moment").build();
        }

    }

    /**
     * Sends a request to retrieve the contents of the specified file in the
     * specified competition, in a JSON format.
     *
     * @param competitionId The Id of the competition
     * @param filePath The path to the file, relative of the project root.
     * @return A response whether the request was send, or if an error occurred.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{competitionId}/file")
    public Response file(@PathParam("competitionId") long competitionId, String filePath) {
        Competition competition = competitionService.findById(competitionId);
        if (competition != null && competition.getCurrentRound() != null) {
            Team team = competition.getTeamByUsername(request.getRemoteUser());
            Challenge challenge = competition.getCurrentRound().getChallenge();
            if (team != null) {
                String messageId = workspaceService.file(competitionId, challenge.getName(), team.getName(), filePath);
                workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
                return Response.ok("File content is being requested").build();
            } else {
                return Response.serverError().entity("Authenticated user isn't a participant in this competition").build();
            }
        } else {
            return Response.serverError().entity("The competition doesn't exist or isn't active at the moment").build();
        }
    }

    //</editor-fold>
}
