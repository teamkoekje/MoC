package api;

import domain.Challenge;
import domain.CodeFile;
import domain.Competition;
import domain.Team;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import service.CompetitionService;
import service.WorkspaceService;

/**
 *
 * @author Astrid
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
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/{teamName}/create")
    public void create(@PathParam("competitionId") long competitionId, @PathParam("teamName") String teamName) {
        String messageId = workspaceService.create(competitionId, teamName);
        workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/{teamName}/delete")
    public void delete(@PathParam("competitionId") long competitionId, @PathParam("teamName") String teamName) {
        String messageId = workspaceService.delete(competitionId, teamName);
        workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/update")
    public Response update(@PathParam("competitionId") long competitionId, CodeFile file) {
        System.out.println("Updating file: " + file.getFilePath());
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

    @POST
    @Consumes("application/xml,application/json")
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

    @POST
    @Consumes("application/xml,application/json")
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

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/test/{testFile}/{testname}")
    public Response test(@PathParam("competitionId") long competitionId, @PathParam("testFile") String testFile, @PathParam("testName") String testName, CodeFile file) {
        Competition competition = competitionService.findById(competitionId);
        if (competition != null && competition.getCurrentRound() != null) {
            Team team = competition.getTeamByUsername(request.getRemoteUser());
            Challenge challenge = competition.getCurrentRound().getChallenge();
            if (team != null) {
                String messageId = workspaceService.test(competitionId, team.getName(), challenge.getName(), testFile, testName, file.getFilePath(), file.getFileContent());
                workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
                return Response.ok("Test is being executed").build();
            } else {
                return Response.serverError().entity("Authenticated user isn't a participant in this competition").build();
            }
        } else {
            return Response.serverError().entity("The competition doesn't exist or isn't active at the moment").build();
        }
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/{challengeName}/push")
    public String pushChallenge(@PathParam("competitionId") long competitionId, @PathParam("challengeName") String challengeName) {
        workspaceService.push(competitionId, challengeName);
        return null;
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/sysinfo")
    public void sysInfo() {
        workspaceService.sysInfo(request.getUserPrincipal().getName());
    }
    
    @POST
    @Consumes("application/xml,application/json")
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
    
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/availableTests")
    public Response availableTests(@PathParam("competitionId") long competitionId){
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
    
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/file")
    public Response file(@PathParam("competitionId") long competitionId, String filePath) {
        Competition competition = competitionService.findById(competitionId);
        if (competition != null && competition.getCurrentRound() != null) {
            Team team = competition.getTeamByUsername(request.getRemoteUser());
            Challenge challenge = competition.getCurrentRound().getChallenge();
            if (team != null) {
                String messageId = workspaceService.file(competitionId, challenge.getName(), team.getName(), filePath);
                workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
                return Response.ok("File content is being request being generated").build();
            } else {
                return Response.serverError().entity("Authenticated user isn't a participant in this competition").build();
            }
        } else {
            return Response.serverError().entity("The competition doesn't exist or isn't active at the moment").build();
        }
    }
    
    
    @GET
    @Produces("application/xml,application/json")
    @Path("/sysinfo")
    public void Sysinfo()
    {
        workspaceService.sysInfo(request.getRemoteUser());
    }
    //</editor-fold>
}
