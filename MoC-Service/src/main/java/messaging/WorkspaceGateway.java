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

    public synchronized String sendRequestToTeam(TeamRequest request) {
        WorkspaceServer ws = router.getServerByWorkspaceName(request.getTeamName());
        if (ws != null) {
            return sendRequest(request, ws.getSender());
        } else {
            Logger.logMsg(Logger.INFO, "Workspace not found on available servers");
            return null;
        }
    }

    public synchronized int broadcast(Request request) {
        List<WorkspaceServer> servers = router.getAllServers();
        for (WorkspaceServer server : servers) {
            sendRequest(request, server.getSender());
        }
        return servers.size();
    }

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

    public abstract void onWorkspaceMessageReceived(Message message);
    // </editor-fold>
}
