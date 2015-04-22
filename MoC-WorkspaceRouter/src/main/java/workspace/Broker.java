package workspace;

import javax.jms.Message;

/**
 * //TODO: class description, what does this class do
 *
 * @author Robin
 * @author TeamKoekje
 */
public class Broker {

    private final ServiceGateway serviceGtw;
    private final WorkspaceGateway workspaceGtw;

    public Broker(String serviceReplyQueue, String workspaceReplyQueue, String workspaceRequestQueue) {
        workspaceGtw = new WorkspaceGateway(workspaceReplyQueue, workspaceRequestQueue) {

            @Override
            void onWorkspaceMessageReceived(Message message) {
                onMessageReceived(message);
            }
        };

        serviceGtw = new ServiceGateway(serviceReplyQueue) {

            @Override
            synchronized void onServiceRequest(Request request) {
                System.out.println("Message received!");
                if (request.getAction() == Action.CREATE) {
                    createNewWorkspace(request);
                } else {
                    sendToWorkspace(request);
                }
            }
        };
    }

    private void onMessageReceived(Message message) {
        // TODO Send message back to service module
    }

    private void createNewWorkspace(Request request) {
        workspaceGtw.addWorkspace(request);
    }

    private void sendToWorkspace(Request request) {

    }

    private void onWorkspaceReply(Request request, Reply reply) {
        serviceGtw.sendReply(request, reply);
    }

    public void start() {
        serviceGtw.start();
        workspaceGtw.start();
    }
}
