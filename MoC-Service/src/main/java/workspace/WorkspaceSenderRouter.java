package workspace;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import java.util.*;
import javax.jms.JMSException;
import javax.naming.NamingException;
import messaging.JMSSettings;
// </editor-fold>

/**
 * Keeps track of the connected Workspace Servers.
 *
 * @author TeamKoekje
 */
public class WorkspaceSenderRouter {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    private final List<WorkspaceServer> workspaceServers;
    private Long lastServerId;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors" >
    /**
     * Creates a new instance of the WorkspaceSenderRouter. Used to keep track
     * of the connected Workspace Servers.
     */
    public WorkspaceSenderRouter() {
        workspaceServers = new ArrayList<>();
        lastServerId = 0L;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter & Setters" >
    /**
     * Gets the WorkspaceServer with the specified name.
     *
     * @param workspaceName The name of the WorkspaceServer to get.
     * @return The found WorkspaceServer, or null if the WorkspaceServer is not
     * found.
     */
    public WorkspaceServer getServerByWorkspaceName(String workspaceName) {
        for (WorkspaceServer s : workspaceServers) {
            if (s.containsWorkspace(workspaceName)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Gets the WorkspaceServer with the least workspaces on it.
     *
     * @return The WorkspaceServer with the least workspaces on it, or null if
     * there are no WorkspaceServers connected.
     */
    public WorkspaceServer getServerWithLeastWorkspaces() {
        WorkspaceServer workspaceServer = null;
        for (WorkspaceServer w : workspaceServers) {
            if (workspaceServer == null || w.getWorkspaceCount() < workspaceServer.getWorkspaceCount()) {
                workspaceServer = w;
            }
        }
        return workspaceServer;
    }

    /**
     * Gets all connected WorkspaceServers.
     *
     * @return An ArrayList containing all connected WorkspaceServers.
     */
    public List<WorkspaceServer> getAllServers() {
        return workspaceServers;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Adds a new WorkspaceServer. An Id is automatically generated and the
     * Queue name associated with the WorkspaceServer will be
     * JMSSettings.WORKSPACE_REQUEST_lastServerId.
     *
     * @return The Id of the new WorkspaceServer
     */
    public Long addWorkspaceServer() {
        try {
            lastServerId++;
            WorkspaceServer ws = new WorkspaceServer(lastServerId, JMSSettings.WORKSPACE_REQUEST + "_" + lastServerId);
            workspaceServers.add(ws);
            return lastServerId;
        } catch (NamingException | JMSException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    /**
     * Opens the connection of all WorkspaceServers.
     */
    public void openConnection() {
        for (WorkspaceServer server : workspaceServers) {
            server.getSender().openConnection();
        }
    }

    /**
     * Closes the connection of all WorkspaceServers.
     */
    public void closeConnection() {
        for (WorkspaceServer workspace : workspaceServers) {
            workspace.getSender().closeConnection();
        }
    }
    // </editor-fold>
}
