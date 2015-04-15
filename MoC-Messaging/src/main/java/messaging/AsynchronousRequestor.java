package messaging;

import java.io.Serializable;
import java.util.HashMap;
import javax.jms.Message;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * This class is used for sending requests and receiving replies in asynchronous
 * communication.This class inherits ythe MessagingGateway, i.e., it has access
 * to a MessageSender and MessageReceiver.
 *
 * @param <REQUEST> is the domain class for requests
 * @param <REPLY> is the domain class for replies
 * @author Maja Pesic
 */
public class AsynchronousRequestor<REQUEST, REPLY> {

    /**
     * Class Pair is just used to make it possible to store pairs of REQUEST,
     * ReplyListener in a hashtable!
     */
    private class Pair {

        private final IReplyListener<REQUEST, REPLY> listener;
        private final REQUEST request;

        private Pair(IReplyListener<REQUEST, REPLY> listener, REQUEST request) {
            this.listener = listener;
            this.request = request;
        }
    }

    /**
     * For sending and receiving messages
     */
    private final MessagingGateway gateway;

    /**
     * contains registered reply listeners for each sent request
     */
    private final Map<String, Pair> listeners;

    /**
     * The only constructor. This constructor does the following: 1. creates the
     * serializer and listener. 2. registeres itself as the listener on the
     * MessageReceiver (method onReply)
     *
     * @param requestSenderQueue
     * @param replyReceiverQueue
     * @throws java.lang.Exception
     */
    public AsynchronousRequestor(String requestSenderQueue, String replyReceiverQueue) throws Exception {
        super();
        this.listeners = new HashMap<>();

        gateway = new MessagingGateway(requestSenderQueue, replyReceiverQueue);
        gateway.setReceivedMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                onReply((ObjectMessage) message);
            }
        });
    }

    /**
     * Opens JMS connection in order to be able to send messages and to start
     * receiving messages.
     */
    public void start() {
        gateway.openConnection();
    }

    /**
     * @todo implement this method! Sends one request. Immediately, a listener
     * is registered for this request. This listener will be notified when
     * (later) a reply for this request arrives. This method should: 1. create a
     * Message for the request (use serializer). 2. set the JMSReplyTo of the
     * Message to be the Destination of the gateway's receiver. 3. send the
     * Message 4. register the listener to belong to the JMSMessageID of the
     * request Message
     *
     * @param request is the request object (a domain class) to be sent
     * @param listener is the listener that will be notified when the reply
     * arrives for this request
     */
    public synchronized void sendRequest(REQUEST request, IReplyListener<REQUEST, REPLY> listener) {

        try {
            Message msg = gateway.createMessage((Serializable) request);
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
     * @todo implement this method! This method is invoked for processing of a
     * single reply when it arrives. This method should be registered on the
     * MessageReceiver. This method should: 1. get the registered listener fo
     * the JMSCorrelationID of the Message 2. de-serialize the REPLY from the
     * Message 3. notify the listener about the arrival of the REPLY 4.
     * unregister the listener
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
}
