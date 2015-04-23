package workspace;

import java.util.ArrayList;
import messaging.DestinationType;
import messaging.GatewayType;
import messaging.MessagingGateway;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
public class WorkspaceServer {

    private Long id;
    private String queue;
    private final ArrayList<String> workspaces;
    private final MessagingGateway sender;

    public WorkspaceServer(Long id, String queue) throws Exception {
        this.id = id;
        this.queue = queue;
        this.workspaces = new ArrayList<>();
        sender = new MessagingGateway(queue, DestinationType.QUEUE, GatewayType.SENDER);
    }
    
    public boolean containsWorkspace(String workspaceName){
        return this.workspaces.contains(workspaceName);
    }

    public MessagingGateway getSender() {
        return sender;
    }

    public ArrayList<String> getWorkspaces() {
        return workspaces;
    }

    public void addWorkspace(String w) {
        this.workspaces.add(w);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public int getNumberOfWorkspaces() {
        return workspaces.size();
    }
}
