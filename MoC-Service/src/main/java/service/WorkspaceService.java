package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.Message;
import messaging.WorkspaceGateway;
import websocket.WebsocketEndpoint;
import workspace.CompileRequest;
import workspace.CreateRequest;
import workspace.DeleteRequest;
import workspace.FileRequest;
import workspace.FolderStructureRequest;
import workspace.PushRequest;
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
    
    @Inject
    private WebsocketEndpoint we;

    @PostConstruct
    private void init() {
        System.out.println("Workspace gateway created");
        gateway = new WorkspaceGateway() {

            @Override
            public void onWorkspaceMessageReceived(Message message) {
                System.out.println("Message received from workspace");
                we.send("testmessage");
            }
        };
    }

    @PreDestroy
    private void preDestroy() {
        gateway.closeConnection();
    }

    public void create(String competitionName, String teamName) {
        System.out.println("Send message: create workspace");
        gateway.addWorkspace(new CreateRequest(competitionName, teamName));
    }

    public void delete(String competitionName, String teamName) {
        gateway.sendRequestToTeam(new DeleteRequest(competitionName, teamName));
    }

    public void update(String competitionName, String teamName, String filePath, String fileContent) {
        gateway.sendRequestToTeam(new UpdateRequest(competitionName, teamName, filePath, fileContent));
    }

    public void compile(String competition, String teamname, String challengeName) {
        gateway.sendRequestToTeam(new CompileRequest(competition, teamname, challengeName));
    }

    public void testAll(String competition, String teamname, String challengeName) {
        gateway.sendRequestToTeam(new TestAllRequest(competition, teamname, challengeName));
    }

    public void test(String competition, String teamname, String challengeName, String testFile, String testName) {
        gateway.sendRequestToTeam(new TestRequest(competition, teamname, challengeName, testFile, testName));
    }

    public void push(String competitionName, String challengeName) {
        try {
            byte[] data = Files.readAllBytes(Paths.get("C:\\MoC\\Challenges\\test.zip"));
            System.out.println("pushing challenge");
            gateway.broadcast(new PushRequest(competitionName, challengeName, data));
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void folderStructure(String competitionName, String challengeName, String teamname) {
        gateway.sendRequestToTeam(new FolderStructureRequest(competitionName, challengeName, teamname));
    }

    public void file(String competitionName, String teamname, String challengeName, String filePath) {
        gateway.sendRequestToTeam(new FileRequest(competitionName, teamname, challengeName, filePath));
    }
}
