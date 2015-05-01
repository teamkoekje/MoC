package service;

import java.io.File;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.Message;
import messaging.WorkspaceGateway;
import workspace.Action;
import workspace.CompileRequest;
import workspace.CreateRequest;
import workspace.DeleteRequest;
import workspace.FileRequest;
import workspace.FolderStructureRequest;
import workspace.PushRequest;
import workspace.Request;
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

    @PostConstruct
    private void init() {
        System.out.println("Workspace gateway created");
        gateway = new WorkspaceGateway() {

            @Override
            public void onWorkspaceMessageReceived(Message message) {
                System.out.println("Message received from workspace");
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

    public void test(String competition, String teamname, String challengeName, String testName) {
        gateway.sendRequestToTeam(new TestRequest(competition, teamname, challengeName, testName));
    }

    public void push(String competitionName, String challengeName) {
        gateway.broadcast(new PushRequest(competitionName, challengeName));
    }

    public void folderStructure(String competitionName, String challengeName, String teamname) {
        gateway.sendRequestToTeam(new FolderStructureRequest(competitionName, challengeName, teamname));
    }

    public void file(String competitionName, String teamname, String filePath) {
        gateway.sendRequestToTeam(new FileRequest(competitionName, teamname, filePath));
    }
}
