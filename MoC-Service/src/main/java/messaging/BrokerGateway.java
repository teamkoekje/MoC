package messaging;

import workspace.Reply;
import workspace.Request;

public class BrokerGateway implements IReplyListener<Request, Reply> {

    private final AsynchronousRequestor<Request, Reply> requestor;

    @SuppressWarnings("LeakingThisInConstructor")
    public BrokerGateway(String senderName, String receiverName) throws Exception {
        requestor = new AsynchronousRequestor(senderName, receiverName);
        requestor.start();
    }

    public void sendRequest(Request request) {
        requestor.sendRequest(request, this);
    }

    @Override
    public void onReply(Request request, Reply reply) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
