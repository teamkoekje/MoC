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
    private IReplyListener<Request, Reply> workspaceReplyListener;
    private ArrayList<WorkspaceServer> workspaceServers;
    
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
        
        workspaceReplyListener = new IReplyListener<Request, Reply>() {
            @Override
            public void onReply(Request request, Reply reply) {
                onWorkspaceReply(request, reply);
            }
        };
    }
    
    private void createNewWorkspace(Request request){
        WorkspaceServer workspaceServer = null;
        if(!workspaceServers.isEmpty()){
            for(WorkspaceServer w : workspaceServers){
                if(workspaceServer == null || w.getNumberOfWorkspaces() < workspaceServer.getNumberOfWorkspaces()){
                    workspaceServer = w;
                }
            }
        }else{
            workspaceServer = new WorkspaceServer(0L, "", 0);
            workspaceServers.add(workspaceServer);
        }
        
        //workspaceGtw.sendRequest(request, workspaceReplyListener);
        
        // TODO
    }
    
    private void sendToWorkspace(Request request){
        workspaceGtw.sendRequest(request, workspaceReplyListener);
    }

    private void onWorkspaceReply(Request request, Reply reply) {
        serviceGtw.sendReply(request, reply);
    }
    
    public void start(){
        serviceGtw.start();
        workspaceGtw.start();
    }
}
