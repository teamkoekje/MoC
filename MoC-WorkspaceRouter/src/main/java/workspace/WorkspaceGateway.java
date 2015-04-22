package workspace;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import messaging.DestinationType;
import messaging.JMSSettings;
import messaging.MessagingGateway;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
public class WorkspaceGateway {

    private WorkspaceSenderRouter router;
    private MessagingGateway initGtw;

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
            MessagingGateway gtw = router.getServerWithLeastWorkspaces();
            ObjectMessage msg = gtw.createObjectMessage((Serializable) request);

            gtw.sendMessage(msg);
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
}
