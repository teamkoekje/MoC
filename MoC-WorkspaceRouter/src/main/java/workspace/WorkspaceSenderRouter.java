package workspace;

import domain.Workspace;
import java.util.*;
import messaging.JMSSettings;
import messaging.MessagingGateway;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
class WorkspaceSenderRouter {

    private final List<WorkspaceServer> workspaceServers;
    private Long lastServerId;

    public WorkspaceSenderRouter() throws Exception {
        super();
        workspaceServers = new ArrayList<>();
        lastServerId = 0L;
    }
    
    public long getNextServerId(){
        return this.lastServerId++;
    }

    public Long addWorkspaceServer() {
        try {
            lastServerId++;
            WorkspaceServer ws = new WorkspaceServer(lastServerId, JMSSettings.WORKSPACE_REQUEST + "_" + lastServerId);
            workspaceServers.add(ws);
            return lastServerId;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public Workspace addWorkspace() {
        //Workspace w = new Workspace();
        //ws.addWorkspace(w);
        //return w;
        return null;
    }

    public WorkspaceServer getServerById(Long workspaceId) {
        for (WorkspaceServer s : workspaceServers) {
        }
        return null;
    }

    public MessagingGateway getServerWithLeastWorkspaces() {
        WorkspaceServer workspaceServer = null;
        for (WorkspaceServer w : workspaceServers) {
            if (workspaceServer == null || w.getNumberOfWorkspaces() < workspaceServer.getNumberOfWorkspaces()) {
                workspaceServer = w;
            }
        }
        return workspaceServer.getSender();
    }

    void openConnection() {
        for (WorkspaceServer workspace : workspaceServers) {
            workspace.getSender().openConnection();
        }
    }
}
