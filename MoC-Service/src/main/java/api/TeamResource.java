package api;

import domain.Team;
import domain.User;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import service.TeamService;
import service.UserService;

/**
 *
 * @author Astrid
 */
@Stateless
@Path("team")
@DeclareRoles({"User", "Admin"})
public class TeamResource {

    @Inject
    private TeamService teamService;

    @Inject
    private UserService userService;

    //<editor-fold defaultstate="collapsed" desc="Team">
    /**
     * Gets all teams
     *
     * @return list with teams
     */
    @GET
    @Produces("application/xml,application/json")
    //@RolesAllowed("Admin")
    public List<Team> getTeams() {
        return teamService.findAll();
    }

    /**
     * Gets a team with a certain id
     *
     * @param teamId id of the team
     * @return a user
     */
    @GET
    @Produces("application/xml,application/json")
    @Path("/{teamId}")
    @RolesAllowed({"User", "Admin"})
    public Team getTeamById(@PathParam("teamId") long teamId) {
        return teamService.findById(teamId);
    }

    /**
     * Get all teams that a certain user is a member of
     *
     * @param username username of the user
     * @return list with teams of the user
     */
    @GET
    @Produces("application/xml,application/json")
    @Path("/user/{username}")
    @RolesAllowed({"User", "Admin"})
    public List<Team> getTeamsByUsername(@PathParam("username") String username) {
        User u = userService.findById(username);
        return u.getTeams();
    }

    /**
     * Create a new team
     *
     * @param request request with authentication info
     * @param team team that should be created
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"User", "Admin"})
    public void createTeam(@Context HttpServletRequest request, Team team) {
        User user = userService.findById(request.getRemoteUser());
        team.setOwner(user);
        teamService.createTeam(team);
    }

    /**
     * Updates a team
     *
     * @param team team with the updated information
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/update")
    public void editTeam(Team team) {
        teamService.edit(team);
    }

    /**
     * Deletes a team
     *
     * @param teamId id of the team that should be deleted
     */
    @DELETE
    @Path("/{teamId}")
    public void removeTeam(@PathParam("teamId") long teamId) {
        teamService.remove(teamId);
    }

    //</editor-fold>
}
