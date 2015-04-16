package service;

import dao.AbstractDAO;
import domain.User;
import java.io.File;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
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
        //gateway = new BrokerGateway(JMSSettings.BROKER_REQUEST, JMSSettings.SERVICE_REPLY);
    }

    public void update(File file, long teamId) {
        Request request = new Request(Action.UPDATE, teamId);
        gateway.sendRequest(request);
    }

    public void compile(String artifactName, long teamId) {
        Request request = new Request(Action.COMPILE, teamId);
        gateway.sendRequest(request);
    }

    public void testAll(String artifactName, long teamId) {
        Request request = new Request(Action.TESTALL, teamId);
        gateway.sendRequest(request);
    }

    public void test(String artifactName, long teamId, String testName) {
        Request request = new Request(Action.TEST, teamId);
        request.setTestName(testName);
        gateway.sendRequest(request);
    }

}
