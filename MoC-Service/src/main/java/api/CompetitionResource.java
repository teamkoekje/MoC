package api;

import domain.Competition;
import domain.Round;
import domain.Team;
import domain.User;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import service.CompetitionService;

/**
 * API used to manage competitions, round and teams
 *
 * @author Astrid
 */
@Path("competition")
public class CompetitionResource {

    @Inject
    private CompetitionService service;

    //<editor-fold defaultstate="collapsed" desc="Competition">
    /**
     * Gets all competitions
     *
     * @return list with competitions
     */
    @GET
    @Produces("application/xml,application/json")
    public List<Competition> getCompetitions() {
        return service.getCompetitions();
    }

    /**
     * Gets a competition with a certain id
     *
     * @param competitionId id of the competition
     * @return a competition
     */
    @GET
    @Produces("application/xml,application/json")
    @Path("/{competitionId}")
    public Competition getCompetitionById(@PathParam("competitionId") long competitionId) {
        return service.getCompetitionById(competitionId);
    }

    /**
     * Creates a new competition
     *
     * @param competition the competition that should be created
     */
    @POST
    @Consumes("application/xml,application/json")
    public void createCompetition(Competition competition) {
        service.createCompetition(competition);
    }

    /**
     * Updates a competition
     *
     * @param competition competition with the updated information
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/update")
    public void updateCompetition(Competition competition) {
        service.updateCompetition(competition);
    }

    /**
     * Deletes a competition
     *
     * @param competitionId id of the competition that should be updated
     */
    @DELETE
    @Path("/{competitionId}")
    public void deleteCompetition(@PathParam("competitionId") long competitionId) {
        service.deleteCompetition(competitionId);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Rounds">
    /**
     * Gets all rounds of a certain competition
     *
     * @param competitionId id of the competition
     * @return list with rounds
     */
    @GET
    @Produces("application/xml,application/json")
    @Path("/{competitionId}/round")
    public List<Round> getRounds(@PathParam("competitionId") long competitionId) {
        return service.getRounds(competitionId);
    }

    /**
     * A round with a certain id
     *
     * @param roundId id of the round
     * @return a round
     */
    @GET
    @Produces("application/xml,application/json")
    @Path("/{competitionId}/round/{roundId}")
    public Competition getRoundById(@PathParam("roundId") long roundId) {
        return service.getRoundById(roundId);
    }

    /**
     * Creates a new round
     *
     * @param round the round that should be created
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/round")
    public void createRound(Round round) {
        service.createRound(round);
    }

    /**
     * Updates a round
     *
     * @param round round with the updated information
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/round/update")
    public void updateRound(Round round) {
        service.updateRound(round);
    }

    /**
     * Deletes a round
     *
     * @param roundId id of the round that should be deleted
     */
    @DELETE
    @Path("/{competitionId}/round/{roundId}")
    public void deleteRound(@PathParam("roundId") long roundId) {
        service.deleteRound(roundId);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Teams">
    /**
     * Gets all teams of a certain competition
     *
     * @param competitionId id of the competition
     * @return list with teams
     */
    @GET
    @Produces("application/xml,application/json")
    @Path("/{competitionId}/team")
    public List<Team> getTeams(@PathParam("competitionId") long competitionId) {
        return service.getTeams(competitionId);
    }

    /**
     * Gets a team with a certain id
     *
     * @param teamId id of the team
     * @return
     */
    @GET
    @Produces("application/xml,application/json")
    @Path("/{competitionId}/team/{teamId}")
    public Competition getTeamById(@PathParam("teamId") long teamId) {
        return service.getTeamById(teamId);
    }

    /**
     * Creates a new team
     *
     * @param team team that should be created created
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/team")
    public void createTeam(Team team) {
        service.createTeam(team);
    }

    /**
     * Updates a team
     *
     * @param team team with the updated information
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/team/update")
    public void updateTeam(Team team) {
        service.updateTeam(team);
    }

    /**
     * Deletes a team
     *
     * @param teamId id of the team that should be deleted
     */
    @DELETE
    @Path("/{competitionId}/team/{teamId}")
    public void deleteTeam(@PathParam("teamId") long teamId) {
        service.deleteTeam(teamId);
    }

    //<editor-fold defaultstate="collapsed" desc="Invites">
    /**
     * Invites a member to a certain team
     *
     * @param email email address of the person that should be invited
     * @param teamId id of the team that the person should be invited to
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/team/{teamId}/invite")
    public void inviteMember(String email, @PathParam("teamId") long teamId) {
        service.inviteMember(email, teamId);
    }

    /**
     * Lets a user join a certain team
     *
     * @param user user that should join the team
     * @param token string to verify if the user is allowed to join the team
     * @param teamId id of the team that the user should join
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/team/{teamId}/join")
    public void joinTeam(User user, String token, @PathParam("teamId") long teamId) {
        service.joinTeam(user, token, teamId);
    }

    /**
     * Lets a user leave a certain team
     *
     * @param user user that should leave the team
     * @param teamId id of the team that the user should leave
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/team/{teamId}/leave")
    public void leaveTeam(User user, @PathParam("teamId") long teamId) {
        service.leaveTeam(user, teamId);
    }
    //</editor-fold>
    //</editor-fold>
}
