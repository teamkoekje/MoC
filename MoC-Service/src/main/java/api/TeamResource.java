package api;

import domain.Team;
import domain.User;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
     * Gets all users
     *
     * @return list with users
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
    @PermitAll
    public Team getTeamById(@PathParam("teamId") String teamId) {
        return teamService.findById(teamId);
    }

    @GET
    @Produces("application/xml,application/json")
    @Path("/user/{username}")
    @PermitAll
    public List<Team> getTeamsByUsername(@PathParam("username") String username) {
        User u = userService.findById(username);
        return u.getTeams();
    }
    
    //</editor-fold>
}
