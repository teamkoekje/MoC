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
    @Path("/{teamName}/create")
    public void create(@PathParam("teamName") String teamName) {
        workspaceService.create(teamName);
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{teamName}/update")
    public void update(File file, @PathParam("teamName") String teamName) {
        workspaceService.update(file, teamName);
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{teamName}/compile")
    public boolean compile(String artifactName, @PathParam("teamName") String teamName) {
        System.out.println("compiling");
        artifactName = "";
        workspaceService.compile(artifactName, teamName);
        return false;
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{teamName}/test")
    public String testAll(String artifactName, @PathParam("teamName") String teamName) {
        workspaceService.testAll(artifactName, teamName);
        return null;
    }

    @POST
    @Consumes("application/xml,application/json")
    @Path("/{teamName}/test/{testName}")
    public String test(String artifactName, @PathParam("teamName") String teamName, @PathParam("testName") String testName) {
        workspaceService.test(artifactName, teamName, testName);
        return null;
    }
    //</editor-fold>
}
