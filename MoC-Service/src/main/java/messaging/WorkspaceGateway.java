package messaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import workspace.Reply;
import workspace.Request;
import workspace.WorkspaceSenderRouter;
import workspace.WorkspaceServer;
import workspace.ZipRequest;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
public abstract class WorkspaceGateway {

    private WorkspaceSenderRouter router;
    private MessagingGateway initGtw;
    private MessagingGateway receiverGtw;

    public WorkspaceGateway() {
        try {
            router = new WorkspaceSenderRouter();
            initGtw = new MessagingGateway(JMSSettings.WORKSPACE_INIT_REPLY, DestinationType.TOPIC, JMSSettings.BROKER_INIT_REQUEST, DestinationType.QUEUE);
            initGtw.setReceivedMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message msg) {
                    processInitMessage(msg);
                }
            });

            receiverGtw = new MessagingGateway(JMSSettings.BROKER_REPLY, DestinationType.QUEUE, GatewayType.RECEIVER);
            receiverGtw.setReceivedMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message message) {
                    if (message instanceof ObjectMessage) {
                        try {
                            ObjectMessage objMsg = (ObjectMessage) message;
                            Reply reply = (Reply) objMsg.getObject();
                            System.out.println("Message received: " + reply.getMessage());
                        } catch (JMSException ex) {
                            Logger.getLogger(WorkspaceGateway.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    onWorkspaceMessageReceived(message);
                }
            });

            receiverGtw.openConnection();
            initGtw.openConnection();
            router.openConnection();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void closeConnection() {
        initGtw.closeConnection();
        receiverGtw.closeConnection();
        router.closeConnection();
    }

    private void processInitMessage(Message msg) {
        try {
            System.out.println("Init message received: " + msg.getJMSMessageID());
            long id = router.addWorkspaceServer();
            Message replyMsg = initGtw.createTextMessage(id + "");
            replyMsg.setJMSCorrelationID(msg.getJMSMessageID());
            initGtw.sendMessage(replyMsg);
            System.out.println("Init reply send: " + replyMsg.getJMSCorrelationID());
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
        }

    }

    private void sendRequest(Request request, MessagingGateway gtw) {
        try {
            ObjectMessage msg = gtw.createObjectMessage((Serializable) request);
            msg.setJMSReplyTo(receiverGtw.getReceiverDestination());

            gtw.sendMessage(msg);
            System.out.println("Message sent");
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public synchronized void sendRequest(Request request) {
        WorkspaceServer ws = router.getServerByWorkspaceName(request.getTeamName());
        if (ws != null) {
            sendRequest(request, ws.getSender());
        } else {
            System.out.println("Workspace not found on available servers");
        }
    }

    public synchronized void broadcast(Request request) {
        List<WorkspaceServer> servers = router.getAllServers();
        for (WorkspaceServer server : servers) {
            sendRequest(request, server.getSender());
        }
    }

    public synchronized void addWorkspace(Request request) {
        WorkspaceServer ws = router.getServerWithLeastWorkspaces();
        if (ws != null) {
            ws.addWorkspace(request.getTeamName());
            sendRequest(request, ws.getSender());
        } else {
            System.out.println("No servers available");
        }
    }

    public void sendZipFile(String zipPath) {

        BytesMessage bm = null;
        try (FileInputStream inputStream = new FileInputStream(zipPath)) {
            long total = new File(zipPath).length();
            long current = 0;
            byte[] buffer = new byte[4096];
            int read;
            bm = receiverGtw.createBytesMessage();
            while ((read = inputStream.read(buffer)) != -1) {
                bm.writeBytes(buffer);
                current += read;
                System.out.println("Reading from zip: " + current + "/" + total);
            }
            bm.reset();
        } catch (FileNotFoundException | JMSException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        List<WorkspaceServer> servers = router.getAllServers();
        for (WorkspaceServer server : servers) {
            server.getSender().sendMessage(bm);
        }
        
        Request request = new ZipRequest(buffer);

    }

    void start() {
        router.openConnection();
    }

    public abstract void onWorkspaceMessageReceived(Message message);
}
