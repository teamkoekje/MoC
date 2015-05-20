package workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import messaging.DestinationType;
import messaging.GatewayType;
import messaging.JMSSettings;
import messaging.MessagingGateway;
import org.apache.activemq.BlobMessage;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
public abstract class WorkspaceGateway {

    private WorkspaceSenderRouter router;
    private MessagingGateway initGtw;
    private MessagingGateway receiverGtw;

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
                        System.err.println(ex.getMessage());
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
            System.err.println(ex.getMessage());
        }

    }

    private void sendRequest(Request request, MessagingGateway gtw) {
        try {
            ObjectMessage msg = gtw.createObjectMessage((Serializable) request);
            msg.setJMSReplyTo(receiverGtw.getReceiverDestination());
            
            //TEST
            //BlobMessage bm = initGtw.createBlobMessage("D:\\School\\Proftaak\\MoC\\MoC-WorkspaceRouter\\hin.txt");
            //bm.setJMSReplyTo(receiverGtw.getReceiverDestination());
            //gtw.sendMessage(bm);
            //ENDTEST
            
            gtw.sendMessage(msg);
            System.out.println("Message sent");
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public synchronized void sendRequest(Request request) {
        WorkspaceServer ws = router.getServerByWorkspaceName(request.getTeamName());
        if (ws != null) {
            sendRequest(request, ws.getSender());
        } else {
            System.out.println("Workspace not found on available servers");
        }
    }

    public synchronized void addWorkspace(Request request) {
        WorkspaceServer ws = router.getServerWithLeastWorkspaces();
        if (ws != null) {
            ws.addWorkspace(request.getTeamName());
            sendRequest(request, ws.getSender());
        } else {
            System.out.println("No servers available");
        }
    }

    void start() {
        router.openConnection();
    }

    abstract void onWorkspaceMessageReceived(Message message);
}
