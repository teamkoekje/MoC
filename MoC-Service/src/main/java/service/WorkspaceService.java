package service;

import java.io.File;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import messaging.BrokerGateway;
import messaging.JMSSettings;
import workspace.Action;
import workspace.Request;

/**
 * Service class used to manage users
 *
 * @author Astrid
 */
@Stateless
@RequestScoped
public class WorkspaceService {

    private BrokerGateway gateway;

    @PostConstruct
    private void init() {
        gateway = new BrokerGateway(JMSSettings.BROKER_REQUEST, JMSSettings.SERVICE_REPLY);
    }

    public void create(String teamName) {
        System.out.println("Send message: create workspace");
        Request request = new Request(Action.CREATE, teamName);
        gateway.sendRequest(request);
    }

    public void delete(String teamName) {
        Request request = new Request(Action.DELETE, teamName);
        gateway.sendRequest(request);
    }

    public void update(File file, String teamName) {
        Request request = new Request(Action.UPDATE, teamName);
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
}
