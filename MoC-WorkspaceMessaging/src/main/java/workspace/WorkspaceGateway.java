/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workspace;

import domain.Workspace;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import messaging.MessagingGateway;

/**
 *
 * @author Robin
 */
public class WorkspaceGateway{
    
    private WorkspaceSenderRouter sender;
    
    public WorkspaceGateway(String brokerRequestQueue, String brokerReplyQueue){
        try {
            sender = new WorkspaceSenderRouter();
        } catch (Exception ex) {
            Logger.getLogger(WorkspaceGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void addWorkspace(Request request){
        try {
            MessagingGateway gtw = sender.getServerWithLeastWorkspaces();
            ObjectMessage msg = gtw.createObjectMessage((Serializable)request);
            
            gtw.sendMessage(msg);
        } catch (JMSException ex) {
            Logger.getLogger(WorkspaceGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addWorkspaceServer(){
        sender.addWorkspaceServer();
    }
    
    public void sendMessage(){
    }
    
    void start(){
        sender.openConnection();
    }
}
