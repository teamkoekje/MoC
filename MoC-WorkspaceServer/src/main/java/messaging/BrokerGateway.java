package messaging;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import main.WorkspaceManagement;
import org.apache.activemq.BlobMessage;
import workspace.Reply;
import workspace.Request;

/**
 * Connects with the router, listens for requests, handles them and then replies
 * to them.
 *
 * @author TeamKoekje
 */
public class BrokerGateway implements IRequestListener<Request>, MessageListener {

    private MessagingGateway initGtw;
    private String initMsgId;

    private AsynchronousReplier<Request, Reply> replier;

    public BrokerGateway() throws Exception {
        initGtw = new MessagingGateway(JMSSettings.BROKER_INIT_REQUEST, DestinationType.QUEUE, JMSSettings.WORKSPACE_INIT_REPLY, DestinationType.TOPIC);
        initGtw.setReceivedMessageListener(this);
        initGtw.openConnection();
        sendInitMessage();
    }

    private void sendInitMessage() throws Exception {
        Message msg = initGtw.createTextMessage("HELLO SERVER");
        initGtw.sendMessage(msg);
        initMsgId = msg.getJMSMessageID();
        System.out.println("Init request send: " + initMsgId);
    }

    @Override
    public void onMessage(Message msg) {
        try {
            if (msg instanceof BlobMessage) {
            } else {
                System.out.println("Init reply received: " + msg.getJMSCorrelationID());
                if (msg.getJMSCorrelationID().equals(initMsgId)) {
                    String id = ((TextMessage) msg).getText();
                    initReplier(id);
                    System.out.println("Server id: " + id);
                    initGtw.closeConnection();
                    initGtw = null;
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
            System.err.println(ex.getMessage());
        }
    }

    private void initReplier(String id) {
        try {
            replier = new AsynchronousReplier<>(JMSSettings.WORKSPACE_REQUEST + "_" + id);
            replier.setRequestListener(this);
            replier.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void receivedRequest(Request request) {
        System.out.println("Request received: " + request.getAction());

        // String replyMessage = wsManagement.processRequest(request);
        String replyMessage = "hoi";

        Reply reply = new Reply(replyMessage);
        replier.sendReply(request, reply);
    }

}
