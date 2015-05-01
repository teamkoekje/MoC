package api;

import java.io.File;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import service.WorkspaceService;

/**
 *
 * @author Astrid
 */
@Path("workspace")
public class WorkspaceResource {

    @Inject
    private WorkspaceService workspaceService;

    //<editor-fold defaultstate="collapsed" desc="Workspace">
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/create")
    public void create(@PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName) {
        workspaceService.create(competitionName, teamName);
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/delete")
    public void delete(@PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName) {
        workspaceService.delete(competitionName, teamName);
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/update")
    public void update(String filePath, @PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName) {
        workspaceService.update(competitionName, teamName, filePath, "");
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/{challangeName}/compile")
    public boolean compile(@PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName, @PathParam("challangeName") String challangeName) {
        workspaceService.compile(competitionName, teamName, challangeName);
        return false;
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/{challangeName}/test")
    public String testAll(@PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName, @PathParam("challangeName") String challangeName) {
        workspaceService.testAll(competitionName, teamName, challangeName);
        return null;
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{teamName}/{challangeName}/test/{testName}")
    public String test(@PathParam("competitionName") String competitionName, @PathParam("teamName") String teamName, @PathParam("challangeName") String challangeName, @PathParam("testName") String testName) {
        workspaceService.test(competitionName, teamName, challangeName, testName);
        return null;
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionName}/{challengeName}/push")
    public String pushChallenge(@PathParam("competitionName") String competitionName, @PathParam("challengeName") String challengeName) {
        workspaceService.push(competitionName, challengeName);
        return null;
    }

    //</editor-fold>
}
