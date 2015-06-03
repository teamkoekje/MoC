package messaging;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import java.io.Serializable;
import java.util.HashMap;
import javax.jms.Message;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

// </editor-fold>
/**
 * This class is used for sending requests and receiving replies in asynchronous
 * communication.This class inherits the MessagingGateway, i.e., it has access
 * to a MessageSender and MessageReceiver.
 *
 * @param <REQUEST> is the domain class for requests
 * @param <REPLY> is the domain class for replies
 *
 * @author TeamKoekje
 */
public class AsynchronousRequestor<REQUEST, REPLY> {

    // <editor-fold defaultstate="collapsed" desc="Private classes" >
    /**
     * Used to make it possible to store pairs of REQUEST, ReplyListener in a
     * hashtable!
     */
    private class Pair {

        private final IReplyListener<REQUEST, REPLY> listener;
        private final REQUEST request;

        private Pair(IReplyListener<REQUEST, REPLY> listener, REQUEST request) {
            this.listener = listener;
            this.request = request;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    /**
     * For sending and receiving messages
     */
    private final MessagingGateway gateway;

    /**
     * contains registered reply listeners for each sent request
     */
    private final Map<String, Pair> listeners;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    /**
     * The only constructor. This constructor does the following: 1. creates the
     * serializer and listener. 2. registers itself as the listener on the
     * MessageReceiver (method onReply)
     *
     * @param requestSenderQueue
     * @param replyReceiverQueue
     * @throws javax.naming.NamingException
     */
    public AsynchronousRequestor(String requestSenderQueue, String replyReceiverQueue) throws NamingException, JMSException {
        super();
        this.listeners = new HashMap<>();

        gateway = new MessagingGateway(requestSenderQueue, DestinationType.QUEUE, replyReceiverQueue, DestinationType.QUEUE);
        gateway.setReceivedMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                onReply((ObjectMessage) message);
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Opens JMS connection in order to be able to send messages and to start
     * receiving messages.
     */
    public void start() {
        gateway.openConnection();
    }

    /**
     * Sends a request to the set gateway, where the JMSReplyTo is the receiver
     * destination of the set gateway. Also saves the request and the listener
     * method to call the proper listener when a reply is received.
     *
     * @param request is the request object (a domain class) to be sent
     * @param listener is the listener that will be notified when the reply
     * arrives for this request
     */
    public synchronized void sendRequest(REQUEST request, IReplyListener<REQUEST, REPLY> listener) {

        try {
            Message msg = gateway.createObjectMessage((Serializable) request);
            msg.setJMSReplyTo(gateway.getReceiverDestination());
            gateway.sendMessage(msg);
            String requestID = msg.getJMSMessageID();
            Pair p = new Pair(listener, request);
            listeners.put(requestID, p);

        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
        }

    }

    /**
     * Callback for when a reply is received. Uses the correlation ID of the
     * message to retrieve the correct listener and calls the onReply of that
     * listener.
     *
     * @param message the reply message
     */
    private synchronized void onReply(ObjectMessage message) {
        try {
            String correlationId = message.getJMSCorrelationID();
            Pair p = listeners.get(correlationId);
            REPLY reply = (REPLY) message.getObject();
            p.listener.onReply(p.request, reply);
            listeners.remove(correlationId);
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
        }

    }
    // </editor-fold>
}
