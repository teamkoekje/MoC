package Messaging;

import Management.WorkspaceManagement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import messaging.AsynchronousReplier;
import messaging.DestinationType;
import messaging.IRequestListener;
import messaging.JMSSettings;
import messaging.MessagingGateway;
import workspace.Reply;
import workspace.Request;

/**
 * Connects with the router, listens for requests, handles them and then replies
 * to them.
 *
 * @author TeamKoekje
 */
@Singleton
@Startup
//@Stateless
public class BrokerGateway implements IRequestListener<Request> {   

    private MessagingGateway registerGateway;
    private String initMsgId;
    private final WorkspaceManagement wm = WorkspaceManagement.getInstance();

    private AsynchronousReplier<Request, Reply> replier;

    @PostConstruct
    private void init() {
        try {
            registerGateway = new MessagingGateway(JMSSettings.BROKER_INIT_REQUEST, DestinationType.QUEUE, JMSSettings.WORKSPACE_INIT_REPLY, DestinationType.TOPIC);
            registerGateway.setReceivedMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                    onRegisterReply(msg);
                }
            });
            registerGateway.openConnection();
            register();
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void register() throws JMSException {
        System.out.println("registering");
        Message msg = registerGateway.createTextMessage("HELLO SERVER");
        registerGateway.sendMessage(msg);
        initMsgId = msg.getJMSMessageID();
        System.out.println("Init request send: " + initMsgId);
    }

    private void onRegisterReply(Message msg) {
        System.out.println("on register reply");
        try {
            System.out.println("Init reply received: " + msg.getJMSCorrelationID());
            if (msg.getJMSCorrelationID().equals(initMsgId)) {
                String id = ((TextMessage) msg).getText();
                replier = new AsynchronousReplier<>(JMSSettings.WORKSPACE_REQUEST + "_" + id);
                replier.setRequestListener(this);
                replier.start();
                System.out.println("Server id: " + id);
                registerGateway.closeConnection();
                registerGateway = null;
            }
        } catch (JMSException | NamingException ex) {
            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public synchronized void receivedRequest(Request request) {
        System.out.println("Request received on workspace server: " + request.getAction());
        Reply reply = new Reply(wm.processRequest(request));
        //TODO:
        //if (threads available in pool)
        //  create new correct thread and run it
        //  confirm message with MessagingGateway.confirmMessage(true)
        //else
        //  rollback message using MessagingGateway.confirmMessage(false)
        replier.sendReply(request, reply);
    }
}
