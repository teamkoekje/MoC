package messaging;

// <editor-fold defaultstate="collapsed" desc="imports" >
import workspace.requests2.UpdateRequest;
import workspace.requests2.FolderStructureRequest;
import workspace.requests2.DeleteRequest;
import workspace.requests2.CreateRequest;
import workspace.requests2.CompileRequest;
import workspace.requests2.Request;
import workspace.requests2.FileRequest;
import workspace.requests2.TestRequest;
import workspace.requests2.PushRequest;
import workspace.requests2.TestAllRequest;
import workspace.replies2.Reply;
import workspace.replies2.BroadcastReply;
import workspace.replies2.NormalReply;
import controllers.PathController;
import controllers.SystemInformation;
import management.FileManagement;
import management.WorkspaceManagement;
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
import messaging.messagingConstants2.DestinationType;
import messaging.messagingConstants2.JMSSettings;
import workspace.replies2.ReplyAction;
import workspace.requests2.AvailableTestsRequest;
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
        Reply reply;
        long competitionId;
        String challengeName;
        switch (r.getAction()) {
            case COMPILE:
                CompileRequest compileRequest = (CompileRequest) r;
                wm.updateFile(Long.toString(compileRequest.getCompetitionId()), compileRequest.getTeamName(), compileRequest.getFilePath(), compileRequest.getFileContent());
                reply = new NormalReply(wrapReturnData("submitresult", wm.buildWorkspace(Long.toString(compileRequest.getCompetitionId()), compileRequest.getTeamName(), compileRequest.getChallengeName())));
                if(compileRequest.isSubmitRequest()){
                    reply.setReplyAction(ReplyAction.SUBMIT);
                }
                break;
            case TEST:
                reply = runTest((TestRequest) r);
                break;
            case TESTALL:
                TestAllRequest testAllRequest = (TestAllRequest) r;
                wm.updateFile(Long.toString(testAllRequest.getCompetitionId()), testAllRequest.getTeamName(), testAllRequest.getFilePath(), testAllRequest.getFileContent());
                reply = new NormalReply(wrapReturnData("buildresult", wm.testAll(Long.toString(testAllRequest.getCompetitionId()), testAllRequest.getTeamName(), testAllRequest.getChallengeName())));
                break;
            case UPDATE:
                UpdateRequest updateRequest = (UpdateRequest) r;
                reply = new NormalReply(wm.updateFile(Long.toString(updateRequest.getCompetitionId()), updateRequest.getTeamName(), updateRequest.getFilePath(), updateRequest.getFileContent()));
                break;
            case CREATE:
                CreateRequest createRequest = (CreateRequest) r;
                reply = new NormalReply(wm.createWorkspace(Long.toString(createRequest.getCompetitionId()), createRequest.getTeamName()));
                break;
            case DELETE:
                DeleteRequest deleteRequest = (DeleteRequest) r;
                reply = new NormalReply(wm.removeWorkspace(Long.toString(deleteRequest.getCompetitionId()), deleteRequest.getTeamName()));
                break;
            case PUSH_CHALLENGE:
                PushRequest pushRequest = (PushRequest) r;
                reply = new NormalReply(wm.extractChallenge(Long.toString(pushRequest.getCompetitionId()), pushRequest.getChallengeName(), pushRequest.getData()));
                break;
            case FOLDER_STRUCTURE:
                reply = getFolderStructure((FolderStructureRequest) r);
                break;
            case FILE:
                FileRequest fileRequest = (FileRequest) r;
                competitionId = fileRequest.getCompetitionId();
                challengeName = fileRequest.getChallengeName();
                reply = new NormalReply(wrapReturnData("file", FileManagement.getInstance(competitionId, challengeName).getFileContentJSON(fileRequest.getFilepath())));
                break;
            case SYSINFO:
                reply = new BroadcastReply(SystemInformation.getInfo());
                break;
            case AVAILABLE_TESTS:
                AvailableTestsRequest atRequest = (AvailableTestsRequest) r;
                competitionId = atRequest.getCompetitionId();
                challengeName = atRequest.getChallengeName();
                reply = new NormalReply(wrapReturnData("availabletests", FileManagement.getInstance(competitionId, challengeName).getAvailableTestsJSON()));
                break;
            default:
                reply = new NormalReply("error, unknown action: " + r.getAction().name());
                break;
        }
        return reply;
    }

    /**
     * Runs the specified test
     *
     * @param testRequest
     * @return
     */
    private NormalReply runTest(TestRequest testRequest) {
        wm.updateFile(Long.toString(testRequest.getCompetitionId()), testRequest.getTeamName(), testRequest.getFilePath(), testRequest.getFileContent());

        FileManagement instance = FileManagement.getInstance(testRequest.getCompetitionId(), testRequest.getChallengeName());
        if (instance.getAvailableTests().contains(testRequest.getTestName())) {
            return new NormalReply(wrapReturnData("buildresult", wm.test(Long.toString(testRequest.getCompetitionId()), testRequest.getTeamName(), testRequest.getChallengeName(), testRequest.getTestName())));
        } else {
            return new NormalReply(wrapReturnData("buildresult", "\"Error: Specified test is not available.\""));
        }
    }

    /**
     * Returns the requested folder structure
     *
     * @param fsr
     * @return
     */
    private NormalReply getFolderStructure(FolderStructureRequest fsr) {
        String folderPath = pathInstance.teamChallengePath(
                Long.toString(fsr.getCompetitionId()),
                fsr.getTeamName(),
                fsr.getChallengeName());
        return new NormalReply(wrapReturnData("filestructure", FileManagement.getInstance(fsr.getCompetitionId(), fsr.getChallengeName()).getFolderStructureJSON(folderPath)));
    }

    private String wrapReturnData(String type, String data) {
        return "{\"type\":\"" + type + "\",\"data\":" + data + "}";
    }
    // </editor-fold>
}
