package service;

import dao.AbstractDAO;
import dao.TeamDAO;
import domain.Team;
import domain.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

@Stateless
@RequestScoped
public class TeamService extends AbstractService<Team> {

    @Inject
    private TeamDAO dao;

    @Override
    protected AbstractDAO getDAO() {
        return dao;
    }

    /**
     * Find all teams that belong to a certain competition
     *
     * @param competitionId the id of the competition
     * @return list with teams that belong to the competition
     */
    public List<Team> findByCompetition(long competitionId) {
        return dao.findByCompetition(competitionId);
    }

    //<editor-fold defaultstate="collapsed" desc="Invites">
    /**
     * Invites a member to a certain team
     *
     * @param email email address of the person that should be invited
     * @param teamId id of the team that the person should be invited to
     */
    public void inviteMember(String email, long teamId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Lets a user join a certain team
     *
     * @param user user that should join the team
     * @param token string to verify if the user is allowed to join the team
     * @param teamId id of the team that the user should join
     */
    public void joinTeam(User user, String token, long teamId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Lets a user leave a certain team
     *
     * @param user user that should leave the team
     * @param teamId id of the team that the user should leave
     */
    public void leaveTeam(User user, long teamId) {
        throw new UnsupportedOperationException();
    }
    //</editor-fold>
}
