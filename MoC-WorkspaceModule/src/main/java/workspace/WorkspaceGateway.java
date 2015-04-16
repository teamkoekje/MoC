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
import messaging.AsynchronousRequestor;
import messaging.IReplyListener;
import messaging.IRequestListener;

/**
 *
 * @author Robin
 */
public class WorkspaceGateway{
    MessagingGateway gtw;
    AsynchronousRequestor<Object, Object> req;
    
    public WorkspaceGateway(String brokerRequestQueue, String brokerReplyQueue){
        try {
            req = new AsynchronousRequestor<>(brokerReplyQueue, brokerRequestQueue);
        } catch (Exception ex) {
            Logger.getLogger(WorkspaceGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void sendRequest(Request request, IReplyListener listener) {
        req.sendRequest(request, listener);
    }
    
    void start(){
        req.start();
    }
}
