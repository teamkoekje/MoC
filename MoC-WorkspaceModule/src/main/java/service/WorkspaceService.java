package service;

import dao.WorkspaceDAO;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

@Stateless
@RequestScoped
public class WorkspaceService {

    @Inject
    private WorkspaceDAO dao;
    
    public WorkspaceService(){
        
    }
}
