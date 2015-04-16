/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workspace;

/**
 *
 * @author Robin
 */
public class WorkspaceServer {
    private Long id;
    private String queue;
    private int numberOfWorkspaces;
    
    public WorkspaceServer(Long id, String queue, int numberOfWorkspaces){
        this.id = id;
        this.queue = queue;
        this.numberOfWorkspaces = numberOfWorkspaces;
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
        return numberOfWorkspaces;
    }

    public void setNumberOfWorkspaces(int numberOfWorkspaces) {
        this.numberOfWorkspaces = numberOfWorkspaces;
    }
}
