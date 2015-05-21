package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
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
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
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
                        if (reply.getMessage().startsWith("[SYSINFO]")) {
                            System.out.println(reply.getMessage());
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

    public void sysInfo() {
        gateway.broadcast(new SysInfoRequest(Action.SYSINFO));
    }
}
