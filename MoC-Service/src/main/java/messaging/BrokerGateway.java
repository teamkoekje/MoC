package messaging;

import workspace.Reply;
import workspace.Request;

public class BrokerGateway implements IReplyListener<Request, Reply> {

    private AsynchronousRequestor<Request, Reply> requestor = null;

    public BrokerGateway(String requestSenderQueue, String replyReceiverQueue) {
        try {
            requestor = new AsynchronousRequestor(requestSenderQueue, replyReceiverQueue);
            requestor.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void sendRequest(Request request) {
        requestor.sendRequest(request, this);
    }

    @Override
    public void onReply(Request request, Reply reply) {
        System.out.println("Reply received: " + reply.getMessage());
    }
}
