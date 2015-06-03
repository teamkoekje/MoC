package service;

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
import workspace.Action;
import workspace.CompileRequest;
import workspace.CreateRequest;
import workspace.DeleteRequest;
import workspace.FileRequest;
import workspace.FolderStructureRequest;
import workspace.PushRequest;
import workspace.Reply;
import workspace.Request;
import workspace.SysInfoRequest;
import workspace.TestAllRequest;
import workspace.TestRequest;
import workspace.UpdateRequest;

/**
 * Service class used to manage users
 *
 * @author Astrid
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

    public String create(long competitionId, String teamName) {
        return gateway.addWorkspace(new CreateRequest(competitionId, teamName));
    }

    public String delete(long competitionId, String teamName) {
       return gateway.deleteWorkspace(new DeleteRequest(competitionId, teamName));
    }

    public String update(long competitionId, String teamName, String filePath, String fileContent) {
        System.out.println("Updating file: " + filePath + " with content: " + fileContent);
                
        return gateway.sendRequestToTeam(new UpdateRequest(competitionId, teamName, filePath, fileContent));
    }

    public String compile(long competitionId, String teamName, String challengeName) {
        return gateway.sendRequestToTeam(new CompileRequest(competitionId, teamName, challengeName));
    }

    public String testAll(long competitionId, String teamName, String challengeName) {
        return gateway.sendRequestToTeam(new TestAllRequest(competitionId, teamName, challengeName));
    }

    public String test(long competitionId, String teamName, String challengeName, String testFile, String testName) {
        return gateway.sendRequestToTeam(new TestRequest(competitionId, teamName, challengeName, testFile, testName));
    }

    public void push(long competitionId, String challengeName) {
        try {
            byte[] data = Files.readAllBytes(Paths.get("C:\\MoC\\Challenges\\test.zip"));
            System.out.println("pushing challenge");
            gateway.broadcast(new PushRequest(competitionId, challengeName, data));

        } catch (IOException ex) {
            Logger.getLogger(WorkspaceService.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void folderStructure(long competitionId, String challengeName, String teamName) {
        gateway.sendRequestToTeam(new FolderStructureRequest(competitionId, challengeName, teamName));
    }

    public void file(long competitionId, String teamName, String challengeName, String filePath) {
        gateway.sendRequestToTeam(new FileRequest(competitionId, teamName, challengeName, filePath));
    }

    public void sysInfo(final String username) {
        SysInfoRequest sir = new SysInfoRequest(Action.SYSINFO);
        numberOfBroadcastMessages = gateway.broadcast(sir);
        sia = new SysInfoAggregate(sir, numberOfBroadcastMessages, username, new IReplyListener<Request, Reply>() {

            @Override
            public void onReply(Request request, Reply reply) {
                System.out.println("All servers responded to the broadcast. Message to send to user: ");
                System.out.println(reply.getMessage());
                we.sendToUser(reply.getMessage(), username);
                // TODO: Think of something in case a server drops out.
            }
        });
    }
}
