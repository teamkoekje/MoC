package Messaging;

// <editor-fold defaultstate="collapsed" desc="imports" >
import workspace.Requests.UpdateRequest;
import workspace.Requests.FolderStructureRequest;
import workspace.Requests.DeleteRequest;
import workspace.Requests.CreateRequest;
import workspace.Requests.CompileRequest;
import workspace.Requests.Request;
import workspace.Requests.FileRequest;
import workspace.Requests.TestRequest;
import workspace.Requests.PushRequest;
import workspace.Requests.TestAllRequest;
import workspace.Replies.Reply;
import workspace.Replies.BroadcastReply;
import workspace.Replies.NormalReply;
import controllers.PathController;
import controllers.SystemInformation;
import management.FileManagement;
import management.WorkspaceManagement;
import java.io.File;
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
import messaging.MessagingConstants.DestinationType;
import messaging.IRequestListener;
import messaging.MessagingConstants.JMSSettings;
import messaging.MessagingGateway;
import workspace.Requests.AvailableTestsRequest;
//</editor-fold>

/**
 * Connects with the router, listens for requests, handles them and then replies
 * to them.
 *
 * @author TeamKoekje
 */
@Singleton
@Startup
public class BrokerGateway implements IRequestListener<Request> {

    // <editor-fold defaultstate="collapsed" desc="variables" >
    private MessagingGateway registerGateway;
    private String initMsgId;
    private final PathController pathInstance = PathController.getInstance();
    private final WorkspaceManagement wm = WorkspaceManagement.getInstance();

    private AsynchronousReplier<Request, Reply> requestReplier;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    /**
     * Called on the start. Registers to the service.
     */
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

    /**
     * Registers to the service by sending a "HELLO SERVER" message.
     *
     * @throws JMSException Thrown when something goes wrong in the JMS
     * communication.
     */
    private void register() throws JMSException {
        System.out.println("registering");
        Message msg = registerGateway.createTextMessage("HELLO SERVER");
        registerGateway.sendMessage(msg);
        initMsgId = msg.getJMSMessageID();
        System.out.println("Init request send: " + initMsgId);
    }

    /**
     * Callback for when the service send a reply on the register request.
     * Starts the listening to requests made from the service.
     *
     * @param msg The message the service replied.
     */
    private void onRegisterReply(Message msg) {
        System.out.println("on register reply");
        try {
            System.out.println("Init reply received: " + msg.getJMSCorrelationID());
            if (msg.getJMSCorrelationID().equals(initMsgId)) {
                String id = ((TextMessage) msg).getText();
                requestReplier = new AsynchronousReplier<>(JMSSettings.WORKSPACE_REQUEST + "_" + id);
                requestReplier.setRequestListener(this);
                requestReplier.start();
                wm.setServerId(id);
                System.out.println("Server id: " + id);
                registerGateway.closeConnection();
                registerGateway = null;
            }
        } catch (JMSException | NamingException ex) {
            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
            System.err.println(ex.getMessage());
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods" >
    /**
     * Callback method for when a request is received. Handles the request and
     * sends the appropriate reply.
     *
     * @param request The request to handle
     */
    @Override
    public void receivedRequest(Request request) {
        System.out.println("Request received on workspace server: " + request.getAction());
        Reply reply = handleRequest(request);
        requestReplier.sendReply(request, reply);
    }

    /**
     * Handles the specified request by casting it to it's specific instance,
     * decided by the Request.getAction() method. After handling the request, a
     * respective Reply is returned.
     *
     * @param r The request to handle
     * @return A reply respective to the original request.
     */
    private Reply handleRequest(Request r) {
        String jarPath;
        String folderPath;
        switch (r.getAction()) {
            case COMPILE:
                CompileRequest compileRequest = (CompileRequest) r;
                wm.updateFile(Long.toString(compileRequest.getCompetitionId()), compileRequest.getTeamName(), compileRequest.getFilePath(), compileRequest.getFileContent());
                return new NormalReply("{\"type\":\"buildresult\",\"data\":\"" + wm.buildWorkspace(Long.toString(compileRequest.getCompetitionId()), compileRequest.getTeamName(), compileRequest.getChallengeName()) + "\"}");
            case TEST:
                TestRequest testRequest = (TestRequest) r;
                wm.updateFile(Long.toString(testRequest.getCompetitionId()), testRequest.getTeamName(), testRequest.getFilePath(), testRequest.getFileContent());

                jarPath = pathInstance.challengesPath(Long.toString(testRequest.getCompetitionId()))
                        + File.separator
                        + testRequest.getChallengeName()
                        + ".jar";
                FileManagement instance = FileManagement.getInstance(jarPath);
                if (instance.getAvailableTests().contains(testRequest.getTestName())) {
                    return new NormalReply("{\"type\":\"buildresult\",\"data\":\"" + wm.test(Long.toString(testRequest.getCompetitionId()), testRequest.getTeamName(), testRequest.getChallengeName(), testRequest.getTestName()) + "\"}");
                }else{
                    return new NormalReply("{\"type\":\"buildresult\",\"data\":\"Error: Specified test is not available.\"}");
                }
            case TESTALL:
                TestAllRequest testAllRequest = (TestAllRequest) r;
                wm.updateFile(Long.toString(testAllRequest.getCompetitionId()), testAllRequest.getTeamName(), testAllRequest.getFilePath(), testAllRequest.getFileContent());
                return new NormalReply("{\"type\":\"buildresult\",\"data\":\"" + wm.testAll(Long.toString(testAllRequest.getCompetitionId()), testAllRequest.getTeamName(), testAllRequest.getChallengeName()) + "\"}");
            case UPDATE:
                UpdateRequest updateRequest = (UpdateRequest) r;
                return new NormalReply(wm.updateFile(Long.toString(updateRequest.getCompetitionId()), updateRequest.getTeamName(), updateRequest.getFilePath(), updateRequest.getFileContent()));
            case CREATE:
                CreateRequest createRequest = (CreateRequest) r;
                return new NormalReply(wm.createWorkspace(Long.toString(createRequest.getCompetitionId()), createRequest.getTeamName()));
            case DELETE:
                DeleteRequest deleteRequest = (DeleteRequest) r;
                return new NormalReply(wm.removeWorkspace(Long.toString(deleteRequest.getCompetitionId()), deleteRequest.getTeamName()));
            case PUSH_CHALLENGE:
                PushRequest pushRequest = (PushRequest) r;
                return new NormalReply(wm.extractChallenge(Long.toString(pushRequest.getCompetitionId()), pushRequest.getChallengeName(), pushRequest.getData()));
            case FOLDER_STRUCTURE:
                FolderStructureRequest folderStructureRequest = (FolderStructureRequest) r;
                folderPath = pathInstance.teamChallengePath(
                        Long.toString(folderStructureRequest.getCompetitionId()),
                        folderStructureRequest.getTeamName(),
                        folderStructureRequest.getChallengeName());
                jarPath = pathInstance.challengesPath(Long.toString(folderStructureRequest.getCompetitionId()))
                        + File.separator
                        + folderStructureRequest.getChallengeName()
                        + ".jar";
                return new NormalReply("{\"type\":\"filestructure\",\"data\":" + FileManagement.getInstance(jarPath).getFolderStructureJSON(folderPath) + "}");
            case FILE:
                FileRequest fileRequest = (FileRequest) r;
                jarPath = pathInstance.challengesPath(Long.toString(fileRequest.getCompetitionId()))
                        + File.separator
                        + fileRequest.getChallengeName()
                        + ".jar";
                return new NormalReply("{\"type\":\"file\",\"data\":" + FileManagement.getInstance(jarPath).getFileContentJSON(fileRequest.getFilepath()) + "}");
            case SYSINFO:
                return new BroadcastReply(SystemInformation.getInfo());
            case AVAILABLE_TESTS:
                AvailableTestsRequest atRequest = (AvailableTestsRequest) r;

                jarPath = pathInstance.challengesPath(Long.toString(atRequest.getCompetitionId()))
                        + File.separator
                        + atRequest.getChallengeName()
                        + ".jar";
                
                return new NormalReply("{\"type\":\"availabletests\",\"data\":" + FileManagement.getInstance(jarPath).getAvailableTestsJSON() + "}");
            default:
                return new NormalReply("error, unknown action: " + r.getAction().name());
        }
    }
    // </editor-fold>
}
