package Messaging;

import Management.FileManagement;
import Management.WorkspaceManagement;
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
import messaging.DestinationType;
import messaging.IRequestListener;
import messaging.JMSSettings;
import messaging.MessagingGateway;
import workspace.BroadcastReply;
import workspace.CompileRequest;
import workspace.CreateRequest;
import workspace.DeleteRequest;
import workspace.FileRequest;
import workspace.FolderStructureRequest;
import workspace.NormalReply;
import workspace.PushRequest;
import workspace.Reply;
import workspace.Request;
import workspace.TestAllRequest;
import workspace.TestRequest;
import workspace.UpdateRequest;

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

    @Override
    public synchronized void receivedRequest(Request request) {
        System.out.println("Request received on workspace server: " + request.getAction());
        Reply reply = getReply(request);
        //TODO:
        //if (threads available in pool)
        //  create new correct thread and run it
        //  confirm message with MessagingGateway.confirmMessage(true)
        //else
        //  rollback message using MessagingGateway.confirmMessage(false)
        replier.sendReply(request, reply);
    }
    
    private Reply getReply(Request r){
        Reply reply;
        switch (r.getAction()) {
            case COMPILE:
                CompileRequest compileRequest = (CompileRequest) r;
                return new NormalReply(wm.buildWorkspace(compileRequest.getCompetition(), compileRequest.getTeamName(), compileRequest.getChallengeName()));
            case TEST:
                TestRequest testRequest = (TestRequest) r;
                return new NormalReply(wm.test(testRequest.getCompetition(), testRequest.getTeamName(), testRequest.getChallengeName(), testRequest.getTestFile(), testRequest.getTestName()));
            case TESTALL:
                TestAllRequest testAllRequest = (TestAllRequest) r;
                return new NormalReply(wm.testAll(testAllRequest.getCompetition(), testAllRequest.getTeamName(), testAllRequest.getChallengeName()));
            case UPDATE:
                UpdateRequest updateRequest = (UpdateRequest) r;
                return new NormalReply(wm.updateFile(updateRequest.getCompetition(), updateRequest.getTeamName(), updateRequest.getFilePath(), updateRequest.getFileContent()));
            case CREATE:
                CreateRequest createRequest = (CreateRequest) r;
                return new NormalReply(wm.createWorkspace(createRequest.getCompetition(), createRequest.getTeamName()));
            case DELETE:
                DeleteRequest deleteRequest = (DeleteRequest) r;
                return new NormalReply(wm.removeWorkspace(deleteRequest.getCompetition(), deleteRequest.getTeamName()));
            case PUSH_CHALLENGE:
                PushRequest pushRequest = (PushRequest) r;
                return new NormalReply(wm.extractChallengeToTeam(pushRequest.getData(), pushRequest.getChallengeName(), pushRequest.getCompetition()));
            case FOLDER_STRUCTURE:
                FolderStructureRequest folderStructureRequest = (FolderStructureRequest) r;
                String folderPath
                        = wm.getDefaultPath()
                        + "Competitions"
                        + File.separator
                        + folderStructureRequest.getCompetition()
                        + File.separator
                        + "Teams"
                        + File.separator
                        + folderStructureRequest.getTeamName()
                        + File.separator
                        + folderStructureRequest.getChallengeName();
                String jarPathForFolder
                        = folderPath
                        + File.separator
                        + folderStructureRequest.getChallengeName() + ".jar";
                System.out.println(jarPathForFolder);
                return new NormalReply(FileManagement.getInstance(jarPathForFolder).getFolderJSON(folderPath));
            case FILE:
                FileRequest fileRequest = (FileRequest) r;
                String jarPathForFile
                        = wm.getDefaultPath()
                        + File.separator
                        + "Competitions"
                        + File.separator
                        + fileRequest.getCompetition()
                        + File.separator
                        + "Teams"
                        + File.separator
                        + fileRequest.getTeamName()
                        + File.separator
                        + fileRequest.getChallangeName()
                        + File.separator
                        + fileRequest.getChallangeName() + ".jar";
                return new NormalReply(FileManagement.getInstance(jarPathForFile).getFileJSON(fileRequest.getFilepath()));
            case SYSINFO:
                return new BroadcastReply(wm.systemInformation());
            // private final String defaultJar = defaultPath + "/annotionframework/annotatedProject-1.0.jar"
            default:
                return new NormalReply("error, unknown action: " + r.getAction().name());
        }
    }
}
