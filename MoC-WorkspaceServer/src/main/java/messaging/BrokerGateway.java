package messaging;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import workspace.Reply;
import workspace.Request;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
public class BrokerGateway implements IReplyListener<Request, Reply>, MessageListener {

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
    public void onReply(Request request, Reply reply) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMessage(Message msg) {
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
            System.err.println(ex.getMessage());
        }
    }

    private void initReplier(String id) {
        try {
            replier = new AsynchronousReplier<>(JMSSettings.BROKER_REQUEST + "_" + id);
            replier.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

}
