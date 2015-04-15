package api;

import java.io.File;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    @Produces("{teamId}/update")
    public void update(File file, @PathParam("teamId") long teamId) {
        workspaceService.update(file, teamId);
    }

    @POST
    @Consumes("application/xml,application/json")
    @Produces("{teamId}/compile")
    public boolean compile(String artifactName, @PathParam("teamId") long teamId) {
        workspaceService.compile(artifactName, teamId);
        return false;
    }

    @POST
    @Consumes("application/xml,application/json")
    @Produces("{teamId}/test")
    public String testAll(String artifactName, @PathParam("teamId") long teamId) {
        workspaceService.testAll(artifactName, teamId);
        return null;
    }

    @POST
    @Consumes("application/xml,application/json")
    @Produces("{teamId}/test/{testName}")
    public String test(String artifactName, @PathParam("teamId") long teamId, @PathParam("testName") String testName) {
        workspaceService.test(artifactName, teamId, testName);
        return null;
    }
    //</editor-fold>
}
