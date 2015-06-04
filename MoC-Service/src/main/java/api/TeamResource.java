package api;

import domain.Invitation;
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
import javax.ws.rs.core.Response;
import service.InvitationService;
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

    @Inject
    private InvitationService invitationService;

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

    @GET
    @Produces("application/xml,application/json")
    @Path("/{teamId}/users")
    @PermitAll
    public List<User> getUsersFromTeam(@PathParam("teamId") long teamId) {
        Team t = teamService.findById(teamId);
        return t.getParticipants();
    }

    @GET
    @Produces("application/xml,application/json")
    @Path("/token/{token}")
    @PermitAll
    public Team getTeamByToken(@PathParam("token") String token) {
        Invitation i = invitationService.findByToken(token);
        if (i != null) {
            return i.getTeam();
        }
        return null;
    }

    /**
     * Gets the teams of the logged in user
     *
     * @param request
     * @return a user
     */
    @GET
    @Produces("application/xml,application/json")
    @Path("/myTeams")
    @RolesAllowed({"User"})
    public List<Team> getMyTeams(@Context HttpServletRequest request) {
        String username = request.getRemoteUser();
        if (username != null) {
            User u = userService.findById(username);
            return u.getTeams();
        } else {
            return null;
        }
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
    
    //<editor-fold defaultstate="collapsed" desc="Invites">
    /**
     * Invites a member to a certain team
     *
     * @param email email address of the person that should be invited
     * @param teamId id of the team that the person should be invited to
     * @return
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{teamId}/invite")
    public Response inviteMember(String email, @PathParam("teamId") long teamId) {
        System.out.println(email + " - " + teamId);

        if (email == null || email.isEmpty()) {
            return Response.serverError().entity("No email").build();
        } else if (teamService.findById(teamId) == null) {
            return Response.serverError().entity("Team not found").build();
        } else {
            invitationService.inviteMember(email, teamId);
            return Response.ok("Invite send").build();
        }
    }

    /**
     * Lets a user join a certain team
     *
     * @param user user that should join the team
     * @param token string to verify if the user is allowed to join the team
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/join/{token}")
    public void joinTeam(User user, @PathParam("token") String token) {
        /*
         THIS METHOD IS NOT USED NOW
         you are either the owner of a team or you are invited,
         this method is handled in UserResource.createUser()
         */

        invitationService.acceptInvitation(user, token);
    }

    /**
     * Lets a user leave a certain team
     *
     * @param user user that should leave the team
     * @param teamId id of the team that the user should leave
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{teamId}/leave/{username}")
    @RolesAllowed({"User", "Admin"})
    public void leaveTeam(@PathParam("teamId") long teamId, @PathParam("username") String username) {
        User user = userService.findById(username);
        if (user != null) {
            teamService.leaveTeam(user, teamId);
        }
    }
    //</editor-fold>
}
