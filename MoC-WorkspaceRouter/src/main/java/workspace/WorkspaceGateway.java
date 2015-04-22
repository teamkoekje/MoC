package workspace;

import domain.Workspace;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import messaging.DestinationType;
import messaging.GatewayType;
import messaging.JMSSettings;
import messaging.MessagingGateway;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
public abstract class WorkspaceGateway {

    private WorkspaceSenderRouter router;
    private MessagingGateway initGtw;
    private MessagingGateway receiverGtw;
    private final String AGGREGATION = "aggregation";

    public WorkspaceGateway(String brokerRequestQueue, String brokerReplyQueue) {
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
                    try {
                        System.out.println("Message received " + message.getJMSCorrelationID());
                    } catch (JMSException ex) {
                        Logger.getLogger(WorkspaceGateway.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    onWorkspaceMessageReceived(message);
                }
            });
            
            receiverGtw.openConnection();
            initGtw.openConnection();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
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
            Logger.getLogger(WorkspaceGateway.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized void addWorkspace(Request request) {
        try {
            WorkspaceServer ws = router.getServerWithLeastWorkspaces();
            MessagingGateway gtw = ws.getSender();
            if (gtw != null) {
                ws.addWorkspace(request.getTeamName());
                ObjectMessage msg = gtw.createObjectMessage((Serializable) request);
                msg.setJMSReplyTo(receiverGtw.getReceiverDestination());
                gtw.sendMessage(msg);
                System.out.println("Message sent");
            } else {
                System.out.println("No servers available");
            }
        } catch (JMSException ex) {
            Logger.getLogger(WorkspaceGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addWorkspaceServer() {
        router.addWorkspaceServer();
    }

    public void sendMessage() {
    }

    void start() {
        router.openConnection();
    }
    
    abstract void onWorkspaceMessageReceived(Message message);
}
