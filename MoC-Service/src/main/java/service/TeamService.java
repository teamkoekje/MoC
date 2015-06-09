package service;

import domain.Competition;
import domain.Invitation;
import domain.Team;
import domain.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Stateless
@RequestScoped
public class TeamService extends GenericService<Team> {

    @Inject
    private UserService userService;

    public TeamService() {
        super(Team.class);
    }

    public void createTeam(Team team) {
        Competition c = team.getCompetition();
        if (!c.participantIsInTeam(team.getOwner())) {
            this.create(team);
        }
    }

    /**
     * Find all teams that belong to a certain competition
     *
     * @param competition the competition
     * @return list with teams that belong to the competition
     */
    public List<Team> findByCompetition(Competition competition) {
        Query q = em.createNamedQuery("Team.findByCompetition");
        q.setParameter("competition", competition);
        return q.getResultList();
    }

    /**
     * Lets a user leave a certain team
     *
     * @param user user that should leave the team
     * @param teamId id of the team that the user should leave
     */
    public void leaveTeam(User user, long teamId) {
        Team team = findById(teamId);
        team.removeParticipant(user);
        user.removeTeam(team);
        edit(team);
    }
}
