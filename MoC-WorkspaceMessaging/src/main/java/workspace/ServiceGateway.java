/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workspace;

import java.util.logging.Level;
import java.util.logging.Logger;
import messaging.MessagingGateway;
import messaging.AsynchronousReplier;
import messaging.IRequestListener;

/**
 *
 * @author Robin
 */
public abstract class ServiceGateway{
    MessagingGateway gtw;
    AsynchronousReplier<Request, Reply> rep;
    IRequestListener<Request> requestListener;
    
    public ServiceGateway(String serviceReplyQueue){
        try {
            this.requestListener = new IRequestListener<Request>() {
                public void receivedRequest(Request request) {
                    onServiceRequest((Request) request);
                }
            };
            
            // create the serializer
            rep = new AsynchronousReplier<>(serviceReplyQueue);
            rep.setRequestListener(requestListener);
        } catch (Exception ex) {
            Logger.getLogger(ServiceGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void sendReply(Request req, Reply reply) {
        rep.sendReply(req, reply);
    }
    
    void start(){
        rep.start();
    }
    
    abstract void onServiceRequest(Request r);
}
