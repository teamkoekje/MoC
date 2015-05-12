package messaging;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import main.WorkspaceManagement;
import workspace.Reply;
import workspace.Request;

/**
 * Connects with the router, listens for requests, handles them and then replies
 * to them.
 *
 * @author TeamKoekje
 */
public class BrokerGateway implements IRequestListener<Request> {

    private MessagingGateway initGtw;
    private String initMsgId;
    private final WorkspaceManagement wm = WorkspaceManagement.getInstance();

    private AsynchronousReplier<Request, Reply> replier;

    @SuppressWarnings("LeakingThisInConstructor")
    public BrokerGateway() throws NamingException, JMSException {
        initGtw = new MessagingGateway(JMSSettings.BROKER_INIT_REQUEST, DestinationType.QUEUE, JMSSettings.WORKSPACE_INIT_REPLY, DestinationType.TOPIC);
        initGtw.setReceivedMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                onInitReply(msg);
            }
        });
        initGtw.openConnection();
        sendInitMessage();
    }

    private void sendInitMessage() throws JMSException {
        Message msg = initGtw.createTextMessage("HELLO SERVER");
        initGtw.sendMessage(msg);
        initMsgId = msg.getJMSMessageID();
        System.out.println("Init request send: " + initMsgId);
    }

    public void onInitReply(Message msg) {
        try {
            System.out.println("Init reply received: " + msg.getJMSCorrelationID());
            if (msg.getJMSCorrelationID().equals(initMsgId)) {
                String id = ((TextMessage) msg).getText();
                initReplier(id);
                System.out.println("Server id: " + id);
                initGtw.closeConnection();
                initGtw = null;
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
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void receivedRequest(Request request) {
        System.out.println("Request received: " + request.getAction());

        String replyMessage = "hoi";
        
        Reply reply = new Reply(wm.processRequest(request));
        //Reply reply = new Reply(replyMessage);
        replier.sendReply(request, reply);
    }

}
