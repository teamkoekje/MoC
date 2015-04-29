package workspace;

import java.util.*;
import messaging.JMSSettings;

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

    public WorkspaceServer getServerByWorkspaceName(String workspaceName) {
        for (WorkspaceServer s : workspaceServers) {
            if (s.containsWorkspace(workspaceName)) {
                return s;
            }
        }
        return null;
    }

    public WorkspaceServer getServerWithLeastWorkspaces() {
        WorkspaceServer workspaceServer = null;
        for (WorkspaceServer w : workspaceServers) {
            if (workspaceServer == null || w.getNumberOfWorkspaces() < workspaceServer.getNumberOfWorkspaces()) {
                workspaceServer = w;
            }
        }
        return workspaceServer;
    }

    void openConnection() {
        for (WorkspaceServer workspace : workspaceServers) {
            workspace.getSender().openConnection();
        }
    }
}
