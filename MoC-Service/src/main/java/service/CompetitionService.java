package service;

import dao.AbstractDAO;
import dao.CompetitionDAO;
import domain.Competition;
import domain.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

@Stateless
@RequestScoped
public class CompetitionService extends AbstractService<Competition> {

    @Inject
    private CompetitionDAO dao;

    @Override
    protected AbstractDAO getDAO() {
        return dao;
    }
}
