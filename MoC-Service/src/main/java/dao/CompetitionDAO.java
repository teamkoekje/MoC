package dao;

import domain.Competition;
import domain.Round;
import domain.Team;
import domain.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Alternative
@Stateless
public class CompetitionDAO {

    @PersistenceContext
    private EntityManager em;

    //<editor-fold defaultstate="collapsed" desc="Competition">
    /**
     * Gets all competitions
     *
     * @return list with competitions
     */
    public List<Competition> getCompetitions() {
        Query q = em.createNamedQuery("Competition.findAll");
        return q.getResultList();
    }

    /**
     * Gets a competition with a certain id
     *
     * @param competitionId id of the competition
     * @return a competition
     */
    public Competition getCompetitionById(long competitionId) {
        Query q = em.createNamedQuery("Competition.find");
        q.setParameter("competitionId", competitionId);
        return (Competition) q.getSingleResult();
    }

    /**
     * Creates a new competition
     *
     * @param competition the competition that should be created
     * @return true if the competition was created, otherwise false
     */
    public boolean createCompetition(Competition competition) {
        if (!em.contains(competition)) {
            em.persist(competition);
            return true;
        }
        return false;
    }

    /**
     * Updates a competition
     *
     * @param competition competition with the updated information
     */
    public void updateCompetition(Competition competition) {
        em.merge(competition);
    }

    /**
     * Deletes a competition
     *
     * @param competitionId id of the competition that should be updated
     */
    public void deleteCompetition(long competitionId) {
        em.remove(getCompetitionById(competitionId));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Rounds">
    /**
     * Gets all rounds of a certain competition
     *
     * @param competitionId id of the competition
     * @return list with rounds
     */
    public List<Round> getRounds(long competitionId) {
        Query q = em.createNamedQuery("Round.findAll");
        return q.getResultList();
    }

    /**
     * A round with a certain id
     *
     * @param competitionId id of the competition that the round belongs to
     * @param roundId id of the round
     * @return a round
     */
    public Round getRoundById(long roundId) {
        Query q = em.createNamedQuery("Round.find");
        q.setParameter("roundId", roundId);
        return (Round) q.getSingleResult();
    }

    /**
     * Creates a new round
     *
     * @param round the round that should be created
     * @param competitionId id of the competition where the round should be
     * created
     */
    public void createRound(Round round) {
        if (!em.contains(round)) {
            em.persist(round);
        }
    }

    /**
     * Updates a round
     *
     * @param round round with the updated information
     * @param competitionId id of the competition that the round belongs to
     */
    public void updateRound(Round round) {
        em.merge(round);
    }

    /**
     * Deletes a round
     *
     * @param roundId id of the round that should be deleted
     */
    public void deleteRound(long roundId) {
        em.remove(getRoundById(roundId));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Teams">
    /**
     * Gets all teams of a certain competition
     *
     * @param competitionId id of the competition
     * @return list with teams
     */
    public List<Team> getTeams(long competitionId) {
        Query q = em.createNamedQuery("Team.findAll");
        return q.getResultList();
    }

    /**
     * Gets a team with a certain id
     *
     * @param competitionId id of the competition that the team belongs to
     * @param teamId id of the team
     * @return
     */
    public Team getTeamById(long teamId) {
        Query q = em.createNamedQuery("Team.find");
        q.setParameter("roundId", teamId);
        return (Team) q.getSingleResult();
    }

    /**
     * Creates a new team
     *
     * @param team team that should be created
     * @param competitionId id of the competition where the team should be
     * created
     */
    public void createTeam(Team team) {
        if (!em.contains(team)) {
            em.persist(team);
        }
    }

    /**
     * Updates a team
     *
     * @param team team with the updated information
     * @param competitionId id of the competition that the team belongs to
     */
    public void updateTeam(Team team) {
        em.merge(team);
    }

    /**
     * Deletes a team
     *
     * @param competitionId id of the competition that team belongs to
     * @param teamId id of the team that should be deleted
     */
    public void deleteTeam(long teamId) {
        em.remove(getTeamById(teamId));
    }

    //<editor-fold defaultstate="collapsed" desc="Invites">
    /**
     * Invites a member to a certain team
     *
     * @param email email address of the person that should be invited
     * @param competitionId id of the commpetition that the person should be
     * invited to
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
     * @param competitionId id of the competition that the team belongs to
     * @param teamId id of the team that the user should join
     */
    public void joinTeam(User user, String token, long teamId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Lets a user leave a certain team
     *
     * @param user user that should leave the team
     * @param competitionId id of the competition that the team belongs to
     * @param teamId id of the team that the user should leave
     */
    public void leaveTeam(User user, long teamId) {
        throw new UnsupportedOperationException();
    }
    //</editor-fold>
    //</editor-fold>
}
