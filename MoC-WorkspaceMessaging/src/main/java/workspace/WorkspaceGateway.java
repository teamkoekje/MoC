package workspace;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import messaging.MessagingGateway;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
public class WorkspaceGateway {

    private WorkspaceSenderRouter sender;

    public WorkspaceGateway(String brokerRequestQueue, String brokerReplyQueue) {
        try {
            sender = new WorkspaceSenderRouter();
        } catch (Exception ex) {
            Logger.getLogger(WorkspaceGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void addWorkspace(Request request) {
        try {
            MessagingGateway gtw = sender.getServerWithLeastWorkspaces();
            ObjectMessage msg = gtw.createObjectMessage((Serializable) request);

            gtw.sendMessage(msg);
        } catch (JMSException ex) {
            Logger.getLogger(WorkspaceGateway.class.getName()).log(Level.SEVERE, null, ex);
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
