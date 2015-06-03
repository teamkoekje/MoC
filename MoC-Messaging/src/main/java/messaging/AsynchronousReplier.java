package messaging;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import java.io.Serializable;
import javax.jms.Message;
import java.util.HashMap;
import java.util.Map;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

// </editor-fold>
/**
 * Replier for the Request-Reply pattern between MoC-Service and
 * MoC-WorkspaceServer.
 *
 * @param <REQUEST>
 * @param <REPLY>
 *
 * @author TeamKoekje
 */
public class AsynchronousReplier<REQUEST, REPLY> {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    /**
     * For sending and receiving messages
     */
    private final MessagingGateway gateway;
    /**
     * For each request, we register the message that brought request. We need
     * the message later, to get the RetournAddress to which we will send the
     * reply.
     */
    private Map<REQUEST, Message> activeRequests = null;
    /**
     * The listener that will be informed when each request arrives.
     */
    private IRequestListener<REQUEST> requestListener = null;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    /**
     * This constructor: 1. intitiates the serializer, receiver and
     * activeRequests 2. registeres a message listener for the MessagingGateway
     * (method onMessage)
     *
     * @param requestReceiverQueue is the name of the JMS queue from which the
     * requests will be received.
     * @throws javax.naming.NamingException
     * @throws javax.jms.JMSException
     */
    public AsynchronousReplier(String requestReceiverQueue) throws NamingException, JMSException {
        super();
        gateway = new MessagingGateway(requestReceiverQueue, DestinationType.QUEUE, GatewayType.RECEIVER);
        gateway.setReceivedMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                if (message instanceof BytesMessage) {

                } else {
                    onRequest((ObjectMessage) message);
                }
            }
        });
        this.activeRequests = new HashMap<>();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter & Setters" >
    /**
     * sets the listener that will be notified when each request arrives
     *
     * @param requestListener
     */
    public void setRequestListener(IRequestListener<REQUEST> requestListener) {
        this.requestListener = requestListener;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Opens the JMS connection of the Messaging Gateway in order to start
     * sending/receiving requests.
     */
    public void start() {
        gateway.openConnection();
    }

    /**
     * Callback for every time a new request arrives
     *
     * @param message the incoming message containing the request
     */
    private synchronized void onRequest(ObjectMessage message) {
        try {
            REQUEST request = (REQUEST) message.getObject();
            activeRequests.put(request, message);
            requestListener.receivedRequest(request);
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Sends the reply for a specific request.
     *
     * 1. get the requestMessage registered for the request from activeRequests
     * 2. serialize the reply and create the replyMessage for it 3. set the
     * JMSCorrelationID of the replyMessage to be equal to the JMSMessageID of
     * the requestMessage 4. get the getJMSReplyTo destination of the
     * requestMessage 5. send the replyMessage to this Destination
     *
     * @param request to which this reply belongs
     * @param reply to the request
     * @return true if the reply is sent succefully; false if sending reply
     * fails
     */
    public synchronized boolean sendReply(REQUEST request, REPLY reply) {
        try {
            Message requestMsg = activeRequests.get(request);
            Message replyMsg = gateway.createObjectMessage((Serializable) reply);
            String messageID = requestMsg.getJMSMessageID();
            replyMsg.setJMSCorrelationID(messageID);
            return gateway.sendMessage(replyMsg, requestMsg.getJMSReplyTo());
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }
    // </editor-fold>
}
