package messaging;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import com.sun.media.jfxmedia.logging.Logger;
import messaging.messagingConstants.DestinationType;
import messaging.messagingConstants.GatewayType;
import messaging.messagingConstants.JMSSettings;
import java.io.Serializable;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import workspace.replies.Reply;
import workspace.requests.Request;
import workspace.requests.TeamRequest;
import workspace.WorkspaceSenderRouter;
import workspace.WorkspaceServer;

// </editor-fold>
/**
 * Application Gateway between MoC-Service and MessagingGateways. Contains the
 * WorkspaceSenderRouter and MessagingGateways for init messages and replies of
 * requests
 *
 * @author TeamKoekje
 */
public abstract class WorkspaceGateway {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    private WorkspaceSenderRouter router;
    private MessagingGateway initGtw;
    private MessagingGateway receiverGtw;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    /**
     * Creates a new instance of the WorkspaceGateway class and starts listening for requests.
     */
    public WorkspaceGateway() {
        try {
            router = new WorkspaceSenderRouter();
            initGtw = new MessagingGateway(JMSSettings.WORKSPACE_INIT_REPLY, DestinationType.TOPIC, JMSSettings.BROKER_INIT_REQUEST, DestinationType.QUEUE);
            initGtw.setReceivedMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                    processInitMessage(msg);
                }
            });

            receiverGtw = new MessagingGateway(JMSSettings.BROKER_REPLY, DestinationType.QUEUE, GatewayType.RECEIVER);
            receiverGtw.setReceivedMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof ObjectMessage) {
                        try {
                            ObjectMessage objMsg = (ObjectMessage) message;
                            Reply reply = (Reply) objMsg.getObject();
                            Logger.logMsg(Logger.INFO, reply.getMessage());
                        } catch (JMSException ex) {
                            Logger.logMsg(Logger.ERROR, ex.getMessage());
                        }
                    }
                    onWorkspaceMessageReceived(message);
                }
            });

            receiverGtw.openConnection();
            initGtw.openConnection();
            router.openConnection();
        } catch (NamingException | JMSException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Closes the init gateway, the receiver gateway and the router.
     */
    public void closeConnection() {
        initGtw.closeConnection();
        receiverGtw.closeConnection();
        router.closeConnection();
    }

    private void processInitMessage(Message msg) {
        try {
            Logger.logMsg(Logger.INFO, "Init message received: " + msg.getJMSMessageID());
            long id = router.addWorkspaceServer();
            Message replyMsg = initGtw.createTextMessage(id + "");
            replyMsg.setJMSCorrelationID(msg.getJMSMessageID());
            initGtw.sendMessage(replyMsg);
            Logger.logMsg(Logger.INFO, "Init reply send: " + replyMsg.getJMSCorrelationID());
        } catch (JMSException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
        }

    }

    private String sendRequest(Request request, MessagingGateway gtw) {
        try {
            ObjectMessage msg = gtw.createObjectMessage((Serializable) request);
            msg.setJMSReplyTo(receiverGtw.getReceiverDestination());
            gtw.sendMessage(msg);
            Logger.logMsg(Logger.INFO, "Message sent");
            return msg.getJMSMessageID();
        } catch (JMSException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
            return null;
        }
    }

    /**
     * Sends a request to a specified workspace server, which is found by
     * looking at the TeamName in the request. TODO: will most likely not work
     * if there are 2+ competitions, where both competitions have a team with
     * the same name.
     *
     * @param request The request to send
     * @return The Id of the message sent to the server, or null if the
     * workspace was not found on any of the connected servers.
     */
    public synchronized String sendRequestToTeam(TeamRequest request) {
        WorkspaceServer ws = router.getServerByWorkspaceName(request.getTeamName());
        if (ws != null) {
            return sendRequest(request, ws.getSender());
        } else {
            Logger.logMsg(Logger.INFO, "Workspace not found on available servers");
            return null;
        }
    }

    /**
     * Broadcasts the specified request to all servers.
     *
     * @param request The request to be send.
     * @return The amount of servers the request was sent to.
     */
    public synchronized int broadcast(Request request) {
        List<WorkspaceServer> servers = router.getAllServers();
        for (WorkspaceServer server : servers) {
            sendRequest(request, server.getSender());
        }
        return servers.size();
    }

    /**
     * Adds a workspace for the team on the specified workspace and sends a
     * message to the workspace server to add it there too.
     *
     * @param request The request that contains the information about the
     * workspace to be added.
     * @return The Id of the message send to the workspace server, or null if
     * there are no connected workspace servers.
     */
    public synchronized String addWorkspace(TeamRequest request) {
        WorkspaceServer ws = router.getServerWithLeastWorkspaces();
        if (ws != null) {
            ws.addWorkspace(request.getTeamName());
            return sendRequest(request, ws.getSender());
        } else {
            Logger.logMsg(Logger.INFO, "No servers available");
            return null;
        }
    }

    /**
     * Removes a workspace for the team on the specified workspace and sends a
     * message to the workspace server to remove it there too.
     *
     * @param request The request that contains the information about the
     * workspace to be removed.
     * @return The Id of the message send to the workspace server, or null if
     * the workspace to remove was not found.
     */
    public String deleteWorkspace(TeamRequest request) {
        WorkspaceServer ws = router.getServerByWorkspaceName(request.getTeamName());
        if (ws != null) {
            ws.deleteWorkspace(request.getTeamName());
            return sendRequest(request, ws.getSender());
        } else {
            Logger.logMsg(Logger.INFO, "Workspace not found");
            return null;
        }
    }

    /**
     * Callback method for when a message is received from the workspace server
     *
     * @param message The received message
     */
    public abstract void onWorkspaceMessageReceived(Message message);
    // </editor-fold>
}
