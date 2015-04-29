package service;

import java.io.File;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.Message;
import messaging.WorkspaceGateway;
import workspace.Action;
import workspace.Request;

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
    private void preDestroy(){
        gateway.closeConnection();
    }

    public void create(String teamName) {
        System.out.println("Send message: create workspace");
        Request request = new Request(Action.CREATE, teamName);
        gateway.addWorkspace(request);
    }

    public void delete(String teamName) {
        Request request = new Request(Action.DELETE, teamName);
        gateway.sendRequest(request);
    }

    public void update(String filePath, String fileContent, String teamName) {
        Request request = new Request(Action.UPDATE, teamName);
        request.setFilePath("test challenge/some text.txt");
        request.setFileContent("hooi");
        gateway.sendRequest(request);
    }

    public void compile(String artifactName, String teamName) {
        Request request = new Request(Action.COMPILE, teamName);
        gateway.sendRequest(request);
    }

    public void testAll(String artifactName, String teamName) {
        Request request = new Request(Action.TESTALL, teamName);
        gateway.sendRequest(request);
    }

    public void test(String artifactName, String teamName, String testName) {
        Request request = new Request(Action.TEST, teamName);
        request.setTestName(testName);
        gateway.sendRequest(request);
    }

    public void push(String challengeName) {
        Request request = new Request(Action.PUSH_CHALLENGE, "");
        request.setChallengeName(challengeName);
        gateway.broadcast(request);
    }
    
    public void addChallenge(String path){
        gateway.sendZipFile(path);
    }
}
