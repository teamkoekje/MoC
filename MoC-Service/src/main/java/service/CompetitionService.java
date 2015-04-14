package service;

import dao.AbstractDAO;
import dao.CompetitionDAO;
import domain.Competition;
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

    public boolean joinTeam(String email, String token, long competitionId, long teamId) {
        Competition c = dao.findById(competitionId);
        boolean result = c.joinTeam(email, token, teamId);
        if (result) {
            dao.edit(c);
        }
        return result;
    }
}
