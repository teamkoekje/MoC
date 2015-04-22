package service;

import domain.Team;
import domain.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.persistence.Query;

@Stateless
@RequestScoped
public class TeamService extends GenericService<Team> {

    public TeamService() {
        super(Team.class);
    }

    /**
     * Find all teams that belong to a certain competition
     *
     * @param competitionId the id of the competition
     * @return list with teams that belong to the competition
     */
    public List<Team> findByCompetition(long competitionId) {
        Query q = em.createNamedQuery("Team.findByCompetition");
        q.setParameter("competitionId", competitionId);
        return q.getResultList();
    }

    /**
     * Lets a user join a certain team
     *
     * @param user user that should join the team
     * @param token string to verify if the user is allowed to join the team
     * @param teamId id of the team that the user should join
     */
    public void joinTeam(User user, String token, long teamId) {
        Team t = this.findById(teamId);

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

}
