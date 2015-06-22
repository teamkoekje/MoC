package messaging;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import com.sun.media.jfxmedia.logging.Logger;
import messaging.messagingConstants.GatewayType;
import messaging.messagingConstants.JMSSettings;
import java.io.Serializable;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
// </editor-fold>

/**
 * Used to send messages from specified senders to receivers.
 *
 * @author TeamKoekje
 */
public class MessagingGateway {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    private MessageProducer producer;
    private MessageConsumer consumer;
    private Destination senderDestination;
    private Destination receiverDestination;
    private Session session;
    private Connection connection;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    /**
     * Creates a new MessagingGateway with the specified sender and receiver.
     * Used to send messages from the sender to the receiver.
     *
     * @param senderName The name of the sender.
     * @param senderType The type of the Sender, found in DestinationType.
     * @param receiverName The name of the receiver.
     * @param receiverType The type of the Receiver, found in DestinationType.
     * @throws NamingException Thrown if a NamingException occurs.
     * @throws JMSException Thrown is a JMSException occurs.
     */
    public MessagingGateway(String senderName, String senderType, String receiverName, String receiverType) throws NamingException, JMSException {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, JMSSettings.URL_ACTIVE_MQ);
        props.put((receiverType + receiverName), receiverName);
        props.put((senderType + senderName), senderName);
        Context jndiContext = new InitialContext(props);

        ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.senderDestination = (Destination) jndiContext.lookup(senderName);
        this.producer = session.createProducer(senderDestination);
        this.receiverDestination = (Destination) jndiContext.lookup(receiverName);
        this.consumer = session.createConsumer(receiverDestination);
    }

    /**
     * Creates a new instance of the MessagingGateway with the specified
     * destination and GatewayType.
     *
     * @param destinationName Name of the destination.
     * @param destinationType Type of the destination, found in DestinationType.
     * @param type The GatewayType of the MessagingGateway to create.
     * @throws NamingException Thrown if a NamingException occurs.
     * @throws JMSException Thrown if a JMSException occurs.
     */
    public MessagingGateway(String destinationName, String destinationType, GatewayType type) throws NamingException, JMSException {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, JMSSettings.URL_ACTIVE_MQ);
        props.put((destinationType + destinationName), destinationName);
        Context jndiContext = new InitialContext(props);

        ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        switch (type) {
            case SENDER:
                this.senderDestination = (Destination) jndiContext.lookup(destinationName);
                this.producer = session.createProducer(senderDestination);
                break;
            case RECEIVER:
                this.producer = session.createProducer(null);
                this.receiverDestination = (Destination) jndiContext.lookup(destinationName);
                this.consumer = session.createConsumer(receiverDestination);
                break;
            default:
                Logger.logMsg(Logger.ERROR, "ERROR Default switch is not supported. MessagingGateway");
                break;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter & Setters)" >
    /**
     * Gets the Destination of the sender.
     *
     * @return The Destination of the sender.
     */
    public Destination getSenderDestination() {
        return senderDestination;
    }

    /**
     * Gets the Destination of the receiver.
     *
     * @return The Destination of the receiver.
     */
    public Destination getReceiverDestination() {
        return receiverDestination;
    }

    /**
     * Sets the Destination of the receiver.
     *
     * @param destination The new Destination of the receiver.
     */
    public void setReceiverdestination(Destination destination) {
        try {
            this.receiverDestination = destination;
            this.consumer = session.createConsumer(receiverDestination);
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());

        }
    }

    /**
     * Sets the listener for when a message is received.
     *
     * @param listener the new MessageListener for when a message is received.
     */
    public void setReceivedMessageListener(MessageListener listener) {
        try {
            consumer.setMessageListener(listener);
        } catch (JMSException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Sends the specified message from the sender to the receiver. Make sure
     * the connection is opened before attempting to send messages.
     *
     * @param msg The message to send.
     * @return True if the message was successfully send, otherwise false.
     */
    public boolean sendMessage(Message msg) {
        try {
            producer.send(msg);
            return true;
        } catch (JMSException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
            return false;
        }
    }

    /**
     * Send the specified Message from the sender to the specified Destination.
     * Make sure the connection is opened before attempting to send messages.
     *
     * @param msg The Message to send.
     * @param d The Destination for the Message.
     * @return True if the Message was successfully send, otherwise false.
     */
    public boolean sendMessage(Message msg, Destination d) {
        try {
            producer.send(d, msg);
            return true;
        } catch (JMSException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
            return false;
        }
    }

    /**
     * Creates a TextMessage from the specified String.
     *
     * @param s The String to create a TextMessage from.
     * @return The created TextMessage.
     * @throws JMSException Thrown if a JMSException occurs.
     */
    public TextMessage createTextMessage(String s) throws JMSException {
        return session.createTextMessage(s);
    }

    /**
     * Creates an ObjectMessage from the specified Serializable Object.
     *
     * @param s The serializable Object to create an ObjectMessage from.
     * @return The created ObjectMessage.
     * @throws JMSException Thrown if a JMSException occurs.
     */
    public ObjectMessage createObjectMessage(Serializable s) throws JMSException {
        return session.createObjectMessage(s);
    }

    /**
     * Opens the connection
     */
    public void openConnection() {
        try {
            connection.start();
        } catch (JMSException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
        }
    }

    /**
     * Closes the connection.
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (JMSException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
        }
    }
    // </editor-fold>
}
