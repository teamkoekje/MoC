package api;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import service.WorkspaceService;

/**
 * API used to manage workspaces
 *
 * @author Robin
 */
@Path("workspace")
public class WorkspaceResource {

    @Inject
    private WorkspaceService workspaceService;

    //<editor-fold defaultstate="collapsed" desc="Workspace">
    /**
     * Run specific test
     *
     * @return error messages
     */
    @GET
    @Produces("application/xml,application/json")
    @Path("/{teamId}/test/{testName}")
    public String runTest() {
        return "";
    }
    //</editor-fold>
}
