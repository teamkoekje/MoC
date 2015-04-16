/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workspace;

import domain.Workspace;
import java.util.ArrayList;
import messaging.GatewayType;
import messaging.MessagingGateway;

/**
 *
 * @author Robin
 */
public class WorkspaceServer {

    private Long id;
    private String queue;
    private ArrayList<Workspace> workspaces;
    private MessagingGateway sender;

    public WorkspaceServer(Long id, String queue) throws Exception {
        this.id = id;
        this.queue = queue;
        this.workspaces = new ArrayList<>();
        sender = new MessagingGateway(queue, GatewayType.SENDER);
    }

    MessagingGateway getSender() {
        return sender;
    }

    public ArrayList<Workspace> getWorkspaces() {
        return workspaces;
    }

    public void addWorkspace(Workspace w) {
        this.workspaces.add(w);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public int getNumberOfWorkspaces() {
        return workspaces.size();
    }
}
