package api;

import domain.Challenge;
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
import javax.ws.rs.core.Response;
import service.CompetitionService;
import service.InvitationService;
import service.RoundService;
import service.TeamService;
import service.UserService;

/**
 * API used to manage competitions, round and teams
 *
 * @author Astrid
 */
@Path("competition")
public class CompetitionResource {

    @Inject
    private CompetitionService competitionService;

    @Inject
    private RoundService roundService;

    @Inject
    private TeamService teamService;

    @Inject
    private InvitationService invitationService;

    @Inject
    private UserService userService;

    //<editor-fold defaultstate="collapsed" desc="Competition">
    /**
     * Gets all competitions
     *
     * @return list with competitions
     */
    @GET
    @Produces("application/xml,application/json")
    public List<Competition> getCompetitions() {
        return competitionService.findAll();
    }

    @GET
    @Produces("application/xml,application/json")
    @Path("/active")
    public List<Competition> getActiveCompetitions() {
        return competitionService.getActiveCompetitions();
    }

    @GET
    @Produces("application/xml,application/json")
    @Path("/future")
    public List<Competition> getFutureCompetitions() {
        return competitionService.geFutureCompetitions();
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
        return competitionService.findById(competitionId);
    }

    /**
     * Creates a new competition
     *
     * @param competition the competition that should be created
     */
    @POST
    @Consumes("application/xml,application/json")
    public void createCompetition(Competition competition) {
        competitionService.create(competition);
    }

    /**
     * Updates a competition
     *
     * @param competition competition with the updated information
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/update")
    public void editCompetition(Competition competition) {
        competitionService.edit(competition);
    }

    /**
     * Deletes a competition
     *
     * @param competitionId id of the competition that should be updated
     */
    @DELETE
    @Path("/{competitionId}")
    public void removeCompetition(@PathParam("competitionId") Long competitionId) {
        competitionService.remove(competitionId);
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
        return roundService.findByCompetition(competitionId);
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
    public Round getRoundById(@PathParam("roundId") long roundId) {
        return roundService.findById(roundId);
    }

    @GET
    @Produces("application/xml,application/json")
    @Path("/{competitionId}/challenges")
    public List<Challenge> getChallengesByCompetition(@PathParam("competitionId") long competitionId) {
        Competition c = competitionService.findById(competitionId);
        return c.getChallenges();
    }
    
    @GET
    @Produces("application/xml,application/json")
    @Path("/{competitionId}/teams")
    public List<Team> getTeamsByCompetition(@PathParam("competitionId") long competitionId) {
        Competition c = competitionService.findById(competitionId);
        return c.getTeams();
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
        roundService.create(round);
    }

    /**
     * Updates a round
     *
     * @param round round with the updated information
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/round/update")
    public void editRound(Round round) {
        roundService.edit(round);
    }

    /**
     * Deletes a round
     *
     * @param roundId id of the round that should be deleted
     */
    @DELETE
    @Path("/{competitionId}/round/{roundId}")
    public void removeRound(@PathParam("roundId") long roundId) {
        roundService.remove(roundId);
    }

    /**
     * Starts a round
     *
     * @param round the round that should be started
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/round/start")
    public void startRound(Round round) {
        //round.start(); idk
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Teams">
    /**
     *
     * // /** // * Creates a new team // * // * @param request // * @param t //
     * * @return //
     */
//    @POST
//    @Path("/{competitionId}/team")
//    @Produces(MediaType.TEXT_PLAIN)
//    @RolesAllowed({"User", "Admin"})
//    public Response createTeam(@Context HttpServletRequest request, Team t) {
//        try {
//            User user = userService.findById(request.getRemoteUser());
//            Team team = new Team(user, t.getName());
//            Competition competition = competitionService.findById(t.getCompetition().getId());
//            team.setCompetition(competition);
//            teamService.createTeam(team);
//            Team createdTeam = teamService.findById(team.getId());
//            return Response.ok(createdTeam.getName()).build();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return Response.serverError().entity(ex.getMessage()).build();
//        }
//    }
    //<editor-fold defaultstate="collapsed" desc="Invites">
    /**
     * Invites a member to a certain team
     *
     * @param email email address of the person that should be invited
     * @param competitionId
     * @param teamId id of the team that the person should be invited to
     * @return
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/team/{teamId}/invite")
    public Response inviteMember(String email, @PathParam("competitionId") long competitionId, @PathParam("teamId") long teamId) {
        if (email == null || email.isEmpty()) {
            return Response.serverError().entity("no email").build();
        } else if (competitionService.findById(competitionId) == null) {
            return Response.serverError().entity("Competition not found").build();
        } else if (teamService.findById(teamId) == null) {
            return Response.serverError().entity("Team not found").build();
        } else {
            invitationService.inviteMember(email, teamId, competitionId);
            return Response.ok("Invite send").build();
        }

    }

    /**
     * Lets a user join a certain team
     *
     * @param user user that should join the team
     * @param token string to verify if the user is allowed to join the team
     * @param competitionId id of the competition that the team belongs to
     * @param teamId id of the team that the user should join
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/team/{teamId}/join/{token}")
    public void joinTeam(User user, @PathParam("token") String token, @PathParam("competitionId") long competitionId, @PathParam("teamId") long teamId) {
        competitionService.joinTeam(user.getEmail(), "token", competitionId, teamId);
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
        teamService.leaveTeam(user, teamId);
    }
    //</editor-fold>
    //</editor-fold>
}
