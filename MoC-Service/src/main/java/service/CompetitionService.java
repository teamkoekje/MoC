package service;

import dao.CompetitionDAO;
import domain.Competition;
import domain.Round;
import domain.Team;
import domain.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

@Stateless
@RequestScoped
public class CompetitionService {

    @Inject
    private CompetitionDAO dao;

    //<editor-fold defaultstate="collapsed" desc="Competition">
    /**
     * Gets all competitions
     *
     * @return list with competitions
     */
    public List<Competition> getCompetitions() {
        return dao.getCompetitions();
    }

    /**
     * Gets a competition with a certain id
     *
     * @param competitionId id of the competition
     * @return a competition
     */
    public Competition getCompetitionById(long competitionId) {
        return dao.getCompetitionById(competitionId);
    }

    /**
     * Creates a new competition
     *
     * @param competition the competition that should be created
     * @return true if the competition was created, otherwise false
     */
    public boolean createCompetition(Competition competition) {
        return dao.createCompetition(competition);
    }

    /**
     * Updates a competition
     *
     * @param competition competition with the updated information
     */
    public void updateCompetition(Competition competition) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes a competition
     *
     * @param competitionId id of the competition that should be updated
     */
    public void deleteCompetition(long competitionId) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    /**
     * A round with a certain id
     *
     * @param competitionId id of the competition that the round belongs to
     * @param roundId id of the round
     * @return a round
     */
    public Competition getRoundById(long roundId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new round
     *
     * @param round the round that should be created
     * @param competitionId id of the competition where the round should be
     * created
     */
    public void createRound(Round round) {
        throw new UnsupportedOperationException();
    }

    /**
     * Updates a round
     *
     * @param round round with the updated information
     * @param competitionId id of the competition that the round belongs to
     */
    public void updateRound(Round round) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes a round
     *
     * @param competitionId id of the competition that the round belongs to
     * @param roundId id of the round that should be deleted
     */
    public void deleteRound(long roundId) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    /**
     * Gets a team with a certain id
     *
     * @param competitionId id of the competition that the team belongs to
     * @param teamId id of the team
     * @return
     */
    public Competition getTeamById(long teamId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new team
     *
     * @param team team that should be created
     * @param competitionId id of the competition where the team should be
     * created
     */
    public void createTeam(Team team) {
        throw new UnsupportedOperationException();
    }

    /**
     * Updates a team
     *
     * @param team team with the updated information
     * @param competitionId id of the competition that the team belongs to
     */
    public void updateTeam(Team team) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes a team
     *
     * @param competitionId id of the competition that team belongs to
     * @param teamId id of the team that should be deleted
     */
    public void deleteTeam(long teamId) {
        throw new UnsupportedOperationException();
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
