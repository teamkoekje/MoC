package api;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import javax.ws.rs.core.Response;
import domain.Challenge;
import domain.Competition;
import domain.Round;
import domain.Team;
import domain.enums.CompetitionState;
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
import service.InvitationService;
import service.RoundService;
import service.TeamService;
import service.UserService;
// </editor-fold>

/**
 * API used to manage competitions, round and teams
 *
 * @author Astrid
 */
@Path("competition")
public class CompetitionResource {

    // <editor-fold defaultstate="collapsed" desc="variables" >
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
    //</editor-fold>

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
        return competitionService.getFutureCompetitions();
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
        competitionService.addFutureCompetition(competition);
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
        competitionService.removeFutureCompetition(competitionService.findById(competitionId));
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
     * @param competitionId
     * @return Response object
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{competitionId}/start")
    public Response startRound(@PathParam("competitionId") long competitionId) {
        Competition c = competitionService.findById(competitionId);
        if (c.getCompetitionState() == CompetitionState.NOT_STARTED && c.getRounds().size() > 0) {
            competitionService.addActiveCompetition(c);
            competitionService.removeFutureCompetition(c);
        }

        boolean ongoing = c.getCompetitionState() == CompetitionState.ONGOING;
        try {
            c.startNextRound();
        } catch (IllegalStateException ex) {
            return Response.serverError().entity(ex.getLocalizedMessage()).build();
        }
        competitionService.edit(c);
        if (ongoing) {
            competitionService.replaceActiveCompetition(c);
        }
        return Response.ok().build();
    }

    @POST
    @Path("/{competitionId}/pause")
    public Response pauseRound(@PathParam("competitionId") long competitionId) {
        System.out.println("pause competition: " + competitionId);
        Competition c = competitionService.findById(competitionId);
        c.getCurrentRound().pause();
        competitionService.edit(c);
        return Response.ok().build();
    }

    @POST
    @Path("/{competitionId}/freeze")
    public Response freezeRound(@PathParam("competitionId") long competitionId) {
        System.out.println("freeze competition: " + competitionId);
        Competition c = competitionService.findById(competitionId);
        c.getCurrentRound().freeze();
        competitionService.edit(c);
        return Response.ok().build();
    }

    @POST
    @Path("/{competitionId}/stop")
    public Response stopRound(@PathParam("competitionId") long competitionId) {
        System.out.println("stop competition: " + competitionId);
        Competition c = competitionService.findById(competitionId);
        c.getCurrentRound().stop();
        competitionService.edit(c);
        return Response.ok().build();
    }
    //</editor-fold>
}
