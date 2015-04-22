package messaging;

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

/**
 * This class is responsible for actually sending messages from one point to
 * another.
 *
 * @author TeamKoekje
 */
public class MessagingGateway {

    private MessageProducer producer;
    private MessageConsumer consumer;
    private Destination senderDestination;
    private Destination receiverDestination;
    private Session session;
    private Connection connection;

    public MessagingGateway(String senderName, String senderType, String receiverName, String receiverType) throws Exception {
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

    public MessagingGateway(String destinationName, String destinationType, GatewayType type) throws Exception {
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
        }
    }

    public boolean sendMessage(Message msg) {
        try {
            producer.send(msg);
            return true;
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    public boolean sendMessage(Message msg, Destination n) {
        try {
            producer.send(n, msg);
            return true;
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    public Destination getSenderDestination() {
        return senderDestination;
    }

    public Destination getReceiverDestination() {
        return receiverDestination;
    }

    public void setReceiverdestination(Destination destination) {
        try {
            this.receiverDestination = destination;
            this.consumer = session.createConsumer(receiverDestination);
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());

        }
    }

    public TextMessage createTextMessage(String s) throws JMSException {
        return session.createTextMessage(s);
    }

    public ObjectMessage createObjectMessage(Serializable s) throws JMSException {
        return session.createObjectMessage(s);
    }

    public void openConnection() {
        try {
            connection.start();
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void setReceivedMessageListener(MessageListener listener) {
        try {
            consumer.setMessageListener(listener);
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
