package messaging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import main.WorkspaceManagement;
import org.apache.activemq.BlobMessage;
import workspace.Reply;
import workspace.Request;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
public class BrokerGateway implements IRequestListener<Request>, MessageListener {

    private MessagingGateway initGtw;
    private String initMsgId;

    private WorkspaceManagement wsManagement;

    private AsynchronousReplier<Request, Reply> replier;

    public BrokerGateway() throws Exception {
        //wsManagement = new WorkspaceManagement();
        initGtw = new MessagingGateway(JMSSettings.BROKER_INIT_REQUEST, DestinationType.QUEUE, JMSSettings.WORKSPACE_INIT_REPLY, DestinationType.TOPIC);
        initGtw.setReceivedMessageListener(this);
        initGtw.openConnection();
        sendInitMessage();
    }

    private void sendInitMessage() throws Exception {
        Message msg = initGtw.createTextMessage("HELLO SERVER");
        initGtw.sendMessage(msg);
        initMsgId = msg.getJMSMessageID();
        System.out.println("Init request send: " + initMsgId);
    }

    @Override
    public void onMessage(Message msg) {
        try {

            if (msg instanceof BlobMessage) {
                //TEST
                /*BlobMessage bm = (BlobMessage) msg;
                InputStream is = bm.getInputStream();
                OutputStream os = new FileOutputStream(new File("D:\\hin.txt"));
                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = is.read(bytes)) != -1) {
                    os.write(bytes, 0, read);
                }
                os.close();
                is.close();*/
                //ENDTEST
            } else {

                System.out.println("Init reply received: " + msg.getJMSCorrelationID());
                if (msg.getJMSCorrelationID().equals(initMsgId)) {
                    String id = ((TextMessage) msg).getText();
                    initReplier(id);
                    System.out.println("Server id: " + id);
                    initGtw.closeConnection();
                    initGtw = null;
                }
            }
        } catch (JMSException ex) {
            System.err.println(ex.getMessage());
        }// catch (IOException ex) {
        //    Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }

    private void initReplier(String id) {
        try {
            replier = new AsynchronousReplier<>(JMSSettings.WORKSPACE_REQUEST + "_" + id);
            replier.setRequestListener(this);
            replier.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void receivedRequest(Request request) {
        System.out.println("Request received: " + request.getAction());

        // String replyMessage = wsManagement.processRequest(request);
        String replyMessage = "hoi";

        Reply reply = new Reply(replyMessage);
        replier.sendReply(request, reply);
    }

}
