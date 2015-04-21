package messaging;

import javax.jms.Message;
import javax.jms.MessageListener;
import workspace.Reply;
import workspace.Request;

/**
 * //TODO: class description, what does this class do
 * @author TeamKoekje
 */
public class BrokerGateway implements IReplyListener<Request, Reply>, MessageListener {

    MessagingGateway gtw;
    AsynchronousReplier<Request, Reply> replier;

    public BrokerGateway(String requestReceiverQueue) throws Exception {
        replier = new AsynchronousReplier<>(requestReceiverQueue);
        gtw = new MessagingGateway(JMSSettings.BROKER_INIT_MESSAGE, GatewayType.SENDER);
        gtw.setReceivedMessageListener(this);
        sendInitMessage();
    }

    private void sendInitMessage() throws Exception {
        Message msg = gtw.createTextMessage("HALLO SERVER");
        //TODO - Idk hoe we aan deze destination komen
        msg.setJMSReplyTo(null);
        gtw.sendMessage(msg);
    }

    @Override
    public void onReply(Request request, Reply reply) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void start() {
        replier.start();
    }

    @Override
    public void onMessage(Message msg) {
        //YAY DOE IETS
    }

}
