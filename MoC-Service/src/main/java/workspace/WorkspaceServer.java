package workspace;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import java.util.ArrayList;
import javax.jms.JMSException;
import javax.naming.NamingException;
import messaging.DestinationType;
import messaging.GatewayType;
import messaging.MessagingGateway;
// </editor-fold>

/**
 * Simplified workspace server class, used by the router to keep track of the
 * connected workspace servers. Also contains the MessagingGateway used to send
 * messages to the server.
 *
 * @author TeamKoekje
 */
public class WorkspaceServer {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    private final Long id;
    private final String queue;
    private final ArrayList<String> workspaces;
    private final MessagingGateway sender;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    /**
     * Initiates a new instance of the WorkspaceServer class with the specified
     * Id and Queue name, which is used by the router to keep track of the
     * connected workspace servers. Also contains the MessagingGateway used to
     * send messages to the server.
     *
     * @param id The Id of the WorkspaceServer to create.
     * @param queue The name of the Queue associated with the WorkspaceServer to
     * create.
     * @throws javax.naming.NamingException Thrown when a NamingException occurs
     * while creating the MessagingGateway.
     * @throws javax.jms.JMSException Thrown when a JMSException occurs while
     * creating the MessagingGateway.
     */
    public WorkspaceServer(Long id, String queue) throws NamingException, JMSException {
        this.id = id;
        this.queue = queue;
        this.workspaces = new ArrayList<>();
        sender = new MessagingGateway(queue, DestinationType.QUEUE, GatewayType.SENDER);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter & Setters" >
    /**
     * Gets the Sender Gateway for this workspace server.
     *
     * @return The MessagingGateway of this workspace server.
     */
    public MessagingGateway getSender() {
        return sender;
    }

    /**
     * Gets the Id of this Workspace Server.
     *
     * @return A Long indicating the Id of this Workspace Server.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the Queue associated with this Workspace Server.
     *
     * @return The name of the Queue associated with this Workspace Server.
     */
    public String getQueue() {
        return queue;
    }

    /**
     * Gets the amount of workspaces in this Workspace Server.
     *
     * @return An Integer indicating the amount of workspaces on this Workspace
     * Server.
     */
    public int getWorkspaceCount() {
        return workspaces.size();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Checks whether this Workspace Server contains the specified Workspace.
     *
     * @param workspaceName The name of the workspace to check for.
     * @return True if this Workspace Server contains the specified Workspace,
     * otherwise False.
     */
    public boolean containsWorkspace(String workspaceName) {
        return this.workspaces.contains(workspaceName);
    }

    /**
     * Adds the specified Workspace to this Workspace Server.
     *
     * @param teamName The name of the Team (and thus, Workspace) to add.
     */
    public void addWorkspace(String teamName) {
        this.workspaces.add(teamName);
    }

    /**
     * Removes the specified Workspace (indicated by the team name) from this
     * Workspace Server.
     *
     * @param teamName The name of the Team (and thus, Workspace) to remove.
     */
    public void deleteWorkspace(String teamName) {
        this.workspaces.remove(teamName);
    }
    // </editor-fold>
}
