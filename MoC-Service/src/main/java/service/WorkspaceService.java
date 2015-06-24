package service;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import com.sun.media.jfxmedia.logging.Logger;
import domain.Competition;
import domain.Team;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
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
import workspace.requests.RequestAction;
import workspace.requests.CompileRequest;
import workspace.requests.CreateRequest;
import workspace.requests.DeleteRequest;
import workspace.requests.FileRequest;
import workspace.requests.FolderStructureRequest;
import workspace.requests.PushRequest;
import workspace.replies.Reply;
import workspace.replies.ReplyAction;
import workspace.requests.AvailableTestsRequest;
import workspace.requests.Request;
import workspace.requests.SysInfoRequest;
import workspace.requests.TestAllRequest;
import workspace.requests.TestRequest;
import workspace.requests.UpdateRequest;

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

    private final Map<String, String> requests = new HashMap<>();

    private int numberOfBroadcastMessages;
    private SysInfoAggregate sia;

    @Inject
    private WebsocketEndpoint we;

    @Inject
    private CompetitionService cse;

    @PostConstruct
    private void init() {
        Logger.logMsg(Logger.INFO, "Workspace gateway created");
        gateway = new WorkspaceGateway() {

            @Override
            public void onWorkspaceMessageReceived(Message message) {
                Logger.logMsg(Logger.INFO, "OnWorkspaceMessageReceived");
                if (message instanceof ObjectMessage) {
                    try {
                        String username = requests.get(message.getJMSCorrelationID());
                        ObjectMessage objMsg = (ObjectMessage) message;
                        Reply reply = (Reply) objMsg.getObject();
                        Logger.logMsg(Logger.INFO, "Message received from workspace: " + reply.getMessage());
                        System.out.println("Message received from workspace: " + reply.getMessage());
                        if (reply.getAction() == ReplyAction.BROADCAST) {
                            sia.addReply(reply);
                        } else if (reply.getAction() == ReplyAction.SUBMIT) {
                            //TODO: The following code block should be removable - not tested
                            /*JsonParserFactory factory = JsonParserFactory.getInstance();
                            JSONParser parser = factory.newJsonParser();
                            Map jsonMap = parser.parseJson(reply.getMessage());
                            String get = (String) jsonMap.get("data");*/
                            if (reply.getMessage().contains(", Failures: 0, Errors: 0,")) {
                                for (Competition c : cse.getActiveCompetitions()) {
                                    Team t = c.getTeamByUsername(username);
                                    if (t != null) {
                                        c.submit(t);
                                        cse.edit(c);//the submitted round should be part of the competition, so this should updated the submitted teams & scores in the db
                                        Logger.logMsg(Logger.INFO, "Sending reply to user: " + username);
                                        we.sendToUser("{\"type\":\"submitresult\",\"data\":\"Successfully submitted\"}", username);
                                        Logger.logMsg(Logger.INFO, "Message sent to client");
                                        return;
                                    }
                                }
                                Logger.logMsg(Logger.ERROR, "Could not find the user connected with the reply of this submit request. Did the active competition end?");
                            } else {
                                Logger.logMsg(Logger.INFO, "Sending reply to user: " + username);
                                we.sendToUser("{\"type\":\"submitresult\",\"data\":\"Errors / Test failures when attempting to submit.\"}", username);
                                Logger.logMsg(Logger.INFO, "Message sent to client");
                            }
                        } else {
                            Logger.logMsg(Logger.INFO, "Sending reply to user: " + username);
                            we.sendToUser(reply.getMessage(), username);
                            Logger.logMsg(Logger.INFO, "Message sent to client");
                        }
                    } catch (JMSException ex) {
                        Logger.logMsg(Logger.ERROR, ex.getMessage());
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
            Logger.logMsg(Logger.INFO, "Request message send with id: " + messageId + " from user: " + username);
        }
    }

    public String create(long competitionId, String teamName) {
        return gateway.addWorkspace(new CreateRequest(competitionId, teamName));
    }

    public String delete(long competitionId, String teamName) {
        return gateway.deleteWorkspace(new DeleteRequest(competitionId, teamName));
    }

    public String update(long competitionId, String teamName, String filePath, String fileContent) {
        Logger.logMsg(Logger.INFO, "Updating file: " + filePath + " with content: " + fileContent);

        return gateway.sendRequestToTeam(new UpdateRequest(competitionId, teamName, filePath, fileContent));
    }

    public String compile(long competitionId, String teamName, String challengeName, String filePath, String fileContent) {
        return gateway.sendRequestToTeam(new CompileRequest(competitionId, teamName, challengeName, filePath, fileContent, false));
    }

    public String submit(long competitionId, String teamName, String challengeName, String filePath, String fileContent) {
        return gateway.sendRequestToTeam(new CompileRequest(competitionId, teamName, challengeName, filePath, fileContent, true));
    }

    public String testAll(long competitionId, String teamName, String challengeName, String filePath, String fileContent) {
        return gateway.sendRequestToTeam(new TestAllRequest(competitionId, teamName, challengeName, filePath, fileContent));
    }

    public String test(long competitionId, String teamName, String challengeName, String testName, String filePath, String fileContent) {
        return gateway.sendRequestToTeam(new TestRequest(competitionId, teamName, challengeName, testName, filePath, fileContent));
    }

    public void push(long competitionId, String challengeName) {
        try {
            byte[] data = Files.readAllBytes(Paths.get("C:\\MoC\\Challenges\\" + challengeName + ".zip"));
            System.out.println("pushing challenge");
            gateway.broadcast(new PushRequest(competitionId, challengeName, data));

        } catch (IOException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
        }
    }

    public String folderStructure(long competitionId, String challengeName, String teamName) {
        Logger.logMsg(Logger.INFO, "Get folder structure for competition: " + competitionId + " and challenge: " + challengeName + " and team: " + teamName);
        return gateway.sendRequestToTeam(new FolderStructureRequest(competitionId, challengeName, teamName));
    }

    public String availableTests(long competitionId, String challengeName, String teamName) {
        Logger.logMsg(Logger.INFO, "Get available tests for competition: " + competitionId + " and challenge: " + challengeName + " and team: " + teamName);
        return gateway.sendRequestToTeam(new AvailableTestsRequest(competitionId, challengeName, teamName));
    }

    public String file(long competitionId, String challengeName, String teamName, String filePath) {
        Logger.logMsg(Logger.INFO, "Get file content: " + filePath);
        return gateway.sendRequestToTeam(new FileRequest(competitionId, teamName, challengeName, filePath));
    }

    public void sysInfo(String username) {
        SysInfoRequest sir = new SysInfoRequest(RequestAction.SYSINFO);
        numberOfBroadcastMessages = gateway.broadcast(sir);
        sia = new SysInfoAggregate(sir, numberOfBroadcastMessages, username, new IReplyListener<Request, Reply>() {
            @Override
            public void onReply(Request request, Reply reply) {
                Logger.logMsg(Logger.INFO, "All servers responded to the broadcast. Message to send to user: ");
                Logger.logMsg(Logger.INFO, reply.getMessage());
                we.sendToUser(reply.getMessage(), sia.getUsername());
                // TODO: Think of something in case a server drops out.
            }
        });
    }

}
