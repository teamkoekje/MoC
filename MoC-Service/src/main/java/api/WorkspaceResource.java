package api;

import java.io.File;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import service.WorkspaceService;

/**
 *
 * @author Astrid
 */
@Path("workspace")
public class WorkspaceResource {

    @Inject
    private WorkspaceService workspaceService;

    @Context
    private HttpServletRequest request;

    //<editor-fold defaultstate="collapsed" desc="Workspace">
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/create")
    public void create(@PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName) {
        String messageId = workspaceService.create(competitionName, teamName);
        workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/delete")
    public void delete(@PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName) {
        String messageId = workspaceService.delete(competitionName, teamName);
        workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/update")
    public void update(String filePath, @PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName) {
        String messageId = workspaceService.update(competitionName, teamName, filePath, "");
        workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/{challengeName}/compile")
    public void compile(@PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName, @PathParam("challengeName") String challengeName) {
        String messageId = workspaceService.compile(competitionName, teamName, challengeName);
        workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/{challengeName}/test")
    public void testAll(@PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName, @PathParam("challengeName") String challengeName) {
        String messageId = workspaceService.testAll(competitionName, teamName, challengeName);
        workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/{challengeName}/test/{testFile}/{testName}")
    public void test(@PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName, @PathParam("challengeName") String challengeName, @PathParam("testFile") String testFile, @PathParam("testName") String testName) {
        String messageId = workspaceService.test(competitionName, teamName, challengeName, testFile, testName);
        workspaceService.storeRequestMessage(messageId, request.getUserPrincipal().getName());
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{challengeName}/push")
    public String pushChallenge(@PathParam("competitionName") String competitionName, @PathParam("challengeName") String challengeName) {
        workspaceService.push(competitionName, challengeName);
        return null;
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/sysinfo")
    public void sysInfo() {
        workspaceService.sysInfo(request.getUserPrincipal().getName());
    }

    //</editor-fold>
}
