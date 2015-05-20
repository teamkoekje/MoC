package messaging;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import workspace.Reply;
import workspace.Request;
import workspace.TeamRequest;
import workspace.WorkspaceSenderRouter;
import workspace.WorkspaceServer;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
public abstract class WorkspaceGateway {

    private WorkspaceSenderRouter router;
    private MessagingGateway initGtw;
    private MessagingGateway receiverGtw;

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
                            Reply reply = (Reply)objMsg.getObject();
                            System.out.println("Message received: " + reply.getMessage());
                        } catch (JMSException ex) {
                            Logger.getLogger(WorkspaceGateway.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    onWorkspaceMessageReceived(message);
                }
            });

            receiverGtw.openConnection();
            initGtw.openConnection();
            router.openConnection();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void closeConnection() {
        initGtw.closeConnection();
        receiverGtw.closeConnection();
        router.closeConnection();
    }

    private void processInitMessage(Message msg) {
        try {
            System.out.println("Init message received: " + msg.getJMSMessageID());
            long id = router.addWorkspaceServer();
            Message replyMsg = initGtw.createTextMessage(id + "");
            replyMsg.setJMSCorrelationID(msg.getJMSMessageID());
            initGtw.sendMessage(replyMsg);
            System.out.println("Init reply send: " + replyMsg.getJMSCorrelationID());
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
        }

    }

    private String sendRequest(Request request, MessagingGateway gtw) {
        try {
            ObjectMessage msg = gtw.createObjectMessage((Serializable) request);
            msg.setJMSReplyTo(receiverGtw.getReceiverDestination());
            String s = msg.getJMSMessageID();
            msg.setJMSCorrelationID(s);
            gtw.sendMessage(msg);
            System.out.println("Message sent");
            return s;
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    public synchronized String sendRequestToTeam(TeamRequest request) {
        WorkspaceServer ws = router.getServerByWorkspaceName(request.getTeamName());
        if (ws != null) {
            return sendRequest(request, ws.getSender());
        } else {
            System.out.println("Workspace not found on available servers");
            return null;
        }
    }
    
    public synchronized void broadcast(Request request){
        List<WorkspaceServer> servers = router.getAllServers();
        for(WorkspaceServer server : servers){
            sendRequest(request, server.getSender());
        }
    }

    public synchronized String addWorkspace(TeamRequest request) {
        WorkspaceServer ws = router.getServerWithLeastWorkspaces();
        if (ws != null) {
            ws.addWorkspace(request.getTeamName());
            return sendRequest(request, ws.getSender());
        } else {
            System.out.println("No servers available");
            return null;
        }
    }

    void start() {
        router.openConnection();
    }

    public abstract void onWorkspaceMessageReceived(Message message);
}
