package workspace;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import messaging.MessagingGateway;

/**
 * Gateway used to handle the registration proces of workspace servers to the router.
 *
 * @author TeamKoekje
 */
public class InitGateway {

    private WorkspaceSenderRouter sender;

    public InitGateway(String brokerRequestQueue, String brokerReplyQueue) {
        try {
            sender = new WorkspaceSenderRouter();
        } catch (Exception ex) {
            Logger.getLogger(InitGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void addWorkspace(Request request) {
        try {
            MessagingGateway gtw = sender.getServerWithLeastWorkspaces();
            ObjectMessage msg = gtw.createObjectMessage((Serializable) request);

            gtw.sendMessage(msg);
        } catch (JMSException ex) {
            Logger.getLogger(InitGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addWorkspaceServer() {
        sender.addWorkspaceServer();
    }

    public void sendMessage() {
    }

    void start() {
        sender.openConnection();
    }
}
