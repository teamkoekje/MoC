/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workspace;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Robin
 */
public class Broker {
    private final ServiceGateway serviceGtw;
    //private final WorkspaceGateway workspaceGtw;
    
    HashMap<Long, Integer> workspaces = new HashMap<>();
    
    public Broker(String serviceRequestQueue, String serviceReplyQueue){
        serviceGtw = new ServiceGateway(serviceRequestQueue, serviceReplyQueue) {

            @Override
            void onServiceRequest(Request serviceRequest) {
                createNewWorkspace(serviceRequest);
                // or
                compileCode(serviceRequest);
                // or
                testCode(serviceRequest);
            }
        };
    }
    
    private void createNewWorkspace(Request serviceRequest){
        Iterator<Long> iterator = workspaces.keySet().iterator();
        Long workspaceId = null;
        while(iterator.hasNext()){
            Long id = iterator.next();
            if(workspaceId == null || workspaces.get(id) < workspaces.get(workspaceId)){
                workspaceId = id;
            }
        }
        
        
    }
    
    private void compileCode(Request serviceRequest){
        
        
    }
    
    private void testCode(Request serviceRequest){
        
    }
    
    public void start(){
        serviceGtw.start();
    }
}
