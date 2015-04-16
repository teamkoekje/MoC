/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workspace;

import java.util.ArrayList;
import messaging.IReplyListener;

/**
 *
 * @author Robin
 */
public class Broker {
    private final ServiceGateway serviceGtw;
    private final WorkspaceGateway workspaceGtw;
    
    public Broker(String serviceReplyQueue, String workspaceReplyQueue, String workspaceRequestQueue){
        workspaceGtw = new WorkspaceGateway(workspaceReplyQueue, workspaceRequestQueue);

        serviceGtw = new ServiceGateway(serviceReplyQueue) {

            @Override
            void onServiceRequest(Request request) {
                if (request.getAction() == Action.CREATE) {
                    createNewWorkspace(request);
                }else{
                    sendToWorkspace(request);
                }
            }
        };
    }
    
    private void createNewWorkspace(Request request){
        workspaceGtw.addWorkspace(request);
    }
    
    private void sendToWorkspace(Request request){
        
    }

    private void onWorkspaceReply(Request request, Reply reply) {
        serviceGtw.sendReply(request, reply);
    }
    
    public void start(){
        serviceGtw.start();
        workspaceGtw.start();
    }
}
