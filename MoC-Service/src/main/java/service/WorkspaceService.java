package service;

// <editor-fold defaultstate="collapsed" desc="Imports" >

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import messaging.IReplyListener;
import messaging.SysInfoAggregate;
import messaging.WorkspaceGateway;
import websocket.WebsocketEndpoint;
import workspace.Requests.Action;
import workspace.Requests.CompileRequest;
import workspace.Requests.CreateRequest;
import workspace.Requests.DeleteRequest;
import workspace.Requests.FileRequest;
import workspace.Requests.FolderStructureRequest;
import workspace.Requests.PushRequest;
import workspace.Replies.Reply;
import workspace.Requests.Request;
import workspace.Requests.SysInfoRequest;
import workspace.Requests.TestAllRequest;
import workspace.Requests.TestRequest;
import workspace.Requests.UpdateRequest;

// </editor-fold>

/**
 * Service class used to manage users
 *
 * @author TeamKoekje
 */
@Singleton
@Startup
public class WorkspaceService {

    private WorkspaceGateway gateway;

    private final HashMap<String, String> requests = new HashMap<>();
    
    private int numberOfBroadcastMessages;
    private SysInfoAggregate sia;

    @Inject
    private WebsocketEndpoint we;

    @PostConstruct
    private void init() {
        System.out.println("Workspace gateway created");
        gateway = new WorkspaceGateway() {

            @Override
            public void onWorkspaceMessageReceived(Message message) {
                System.out.println("OnWorkspaceMessageReceived");
                if (message instanceof ObjectMessage) {
                    try {
                        String username = requests.get(message.getJMSCorrelationID());
                        ObjectMessage objMsg = (ObjectMessage) message;
                        Reply reply = (Reply) objMsg.getObject();
                        if (reply.getAction() == Action.BROADCAST) {
                            sia.addReply(reply);
                        } else {
                            System.out.println("Message received from workspace: " + reply.getMessage());
                            System.out.println("Sending reply to user: " + username);
                            we.sendToUser(reply.getMessage(), username);
                            System.out.println("Message sent to client");
                        }
                    } catch (JMSException ex) {
                        Logger.getLogger(WorkspaceGateway.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
    }

    @PreDestroy
    private void preDestroy() {
        gateway.closeConnection();
    }

    public void storeRequestMessage(String messageId, String username) {
        if (username != null && messageId != null) {
            this.requests.put(messageId, username);
            System.out.println("Request message send with id: " + messageId + " from user: " + username);
        }
    }

    public String create(String competitionName, String teamName) {
        return gateway.addWorkspace(new CreateRequest(competitionName, teamName));
    }

    public String delete(String competitionName, String teamName) {
       return gateway.deleteWorkspace(new DeleteRequest(competitionName, teamName));
    }

    public String update(String competitionName, String teamName, String filePath, String fileContent) {
        return gateway.sendRequestToTeam(new UpdateRequest(competitionName, teamName, filePath, fileContent));
    }

    public String compile(String competition, String teamName, String challengeName) {
        return gateway.sendRequestToTeam(new CompileRequest(competition, teamName, challengeName));
    }

    public String testAll(String competition, String teamName, String challengeName) {
        return gateway.sendRequestToTeam(new TestAllRequest(competition, teamName, challengeName));
    }

    public String test(String competition, String teamName, String challengeName, String testFile, String testName) {
        return gateway.sendRequestToTeam(new TestRequest(competition, teamName, challengeName, testFile, testName));
    }

    public void push(String competitionName, String challengeName) {
        try {
            byte[] data = Files.readAllBytes(Paths.get("C:\\MoC\\Challenges\\test.zip"));
            System.out.println("pushing challenge");
            gateway.broadcast(new PushRequest(competitionName, challengeName, data));

        } catch (IOException ex) {
            Logger.getLogger(WorkspaceService.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void folderStructure(String competitionName, String challengeName, String teamName) {
        gateway.sendRequestToTeam(new FolderStructureRequest(competitionName, challengeName, teamName));
    }

    public void file(String competitionName, String teamName, String challengeName, String filePath) {
        gateway.sendRequestToTeam(new FileRequest(competitionName, teamName, challengeName, filePath));
    }

    public void sysInfo(String username) {
        SysInfoRequest sir = new SysInfoRequest(Action.SYSINFO);
        numberOfBroadcastMessages = gateway.broadcast(sir);
        sia = new SysInfoAggregate(sir, numberOfBroadcastMessages, username, new IReplyListener<Request, Reply>() {
            @Override
            public void onReply(Request request, Reply reply) {
                System.out.println("All servers responded to the broadcast. Message to send to user: ");
                System.out.println(reply.getMessage());
                we.sendToUser(reply.getMessage(), sia.getUsername());
                // TODO: Think of something in case a server drops out.
            }
        });
    }
}
