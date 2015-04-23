package workspace;

import messaging.MessagingGateway;
import messaging.AsynchronousReplier;
import messaging.IRequestListener;

/**
 * Gateway used to communicate with the service module
 *
 * @author TeamKoekje
 */
public abstract class ServiceGateway {

    MessagingGateway gateway;
    AsynchronousReplier<Request, Reply> replier;
    IRequestListener<Request> requestListener;

    public ServiceGateway(String serviceReplyQueue) {
        try {
            this.requestListener = new IRequestListener<Request>() {
                @Override
                public void receivedRequest(Request request) {
                    onServiceRequest((Request) request);
                }
            };
            replier = new AsynchronousReplier<>(serviceReplyQueue);
            replier.setRequestListener(requestListener);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    void sendReply(Request req, Reply reply) {
        replier.sendReply(req, reply);
    }

    void start() {
        replier.start();
    }

    abstract void onServiceRequest(Request r);
}
