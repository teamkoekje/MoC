/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import workspace.Reply;
import workspace.Request;

/**
 *
 * @author Robin
 */
public class BrokerGateway implements IReplyListener<Request, Reply>, IRequestListener<Request> {
    
    AsynchronousRequestor<Request, Reply> requestor;
    AsynchronousReplier<Request, Reply> replier;
    
    public BrokerGateway(String requestSenderQueue, String replyReceiverQueue, String requestReceiverQueue) throws Exception {
        requestor = new AsynchronousRequestor<>(requestSenderQueue, replyReceiverQueue);
        replier = new AsynchronousReplier<>(requestReceiverQueue);
    }
    
    public void sendRequest(Request request){
        requestor.sendRequest(request, this);
    }
    
    @Override
    public void onReply(Request request, Reply reply) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receivedRequest(Request request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void start(){
        requestor.start();
        replier.start();
    }
    
}
