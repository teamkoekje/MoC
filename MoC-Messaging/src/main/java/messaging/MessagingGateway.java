/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Astrid
 */
public class MessagingGateway {

    private MessageProducer producer;
    private MessageConsumer consumer;
    private Destination senderDestination;
    private Destination receiverDestination;
    private Session session;
    private Connection connection;

    public MessagingGateway(String senderName, String receiverName) throws Exception {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        props.put(("queue." + receiverName), receiverName);
        props.put(("queue." + senderName), senderName);
        Context jndiContext = new InitialContext(props);

        ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.senderDestination = (Destination) jndiContext.lookup(senderName);
        this.producer = session.createProducer(senderDestination);
        this.receiverDestination = (Destination) jndiContext.lookup(receiverName);
        this.consumer = session.createConsumer(receiverDestination);
    }

    public MessagingGateway(String destinationName, GatewayType type) throws Exception {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        props.put(("queue." + destinationName), destinationName);
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

    public ObjectMessage createMessage(Serializable s) throws JMSException {
        return session.createObjectMessage(s);
    }

    public void openConnection() {
        try {
            connection.start();
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
