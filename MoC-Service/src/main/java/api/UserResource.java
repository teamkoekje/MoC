package api;

import com.sun.media.jfxmedia.logging.Logger;
import domain.Invitation;
import domain.Team;
import domain.User;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import service.InvitationService;
import service.UserService;

/**
 *
 * @author TeamKoekje
 */
@Stateless
@Path("user")
@DeclareRoles({"User", "Admin"})
public class UserResource {

    @Inject
    private UserService userService;

    @Inject
    private InvitationService invitationService;

    @Context
    private HttpServletRequest request;

    //<editor-fold defaultstate="collapsed" desc="User">
    /**
     * Gets all users
     *
     * @return list with users
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed("Admin")
    public List<User> getUsers() {
        return userService.findAll();
    }

    /**
     * Gets a user with a certain id
     *
     * @param userId id of the user
     * @return a user
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{userId}")
    @PermitAll
    public User getUserById(@PathParam("userId") String userId) {
        return userService.findById(userId);
    }

    /**
     * Gets all users matching the query
     *
     * @param searchInput for the user
     * @return a list of users
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search/{searchInput}")
    @PermitAll
    public List<User> searchUsers(@PathParam("searchInput") String searchInput) {
        return userService.searchUsers(searchInput);
    }

    /**
     * Check if the request is made by an Admin
     *
     * @return A response
     */
    @GET
    @Path("/isAdmin")
    public Response isAdmin() {
        if (request.getRemoteUser() == null) {
            return Response.serverError().entity("You're not logged in").build();
        }
        if (!request.isUserInRole("Admin")) {
            return Response.serverError().entity("You're not in the admin group").build();
        }
        return Response.ok().build();
    }

    /**
     * Get all teams that a certain user is a member of
     *
     * @param username username of the user
     * @return list with teams of the user
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{username}/teams")
    public List<Team> getTeamsFromUser(@PathParam("username") String username) {
        User u = userService.findById(username);
        return u.getTeams();
    }

    /**
     * Get all invitations for a certain user
     *
     * @param username username of the user
     * @return list with invitations of the user
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{username}/invitations")
    public List<Invitation> getInvitationsFromUser(@PathParam("username") String username) {
        User u = userService.findById(username);
        return invitationService.findByEmail(u.getEmail());
    }

    /**
     * Check if a username is available (create call takes User as parameter)
     * and that errors when you submit a user with invalid username
     *
     * @param username
     * @return
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    @Path("/username")
    public Response checkUsername(@FormParam("username") String username) {
        if (username == null || username.isEmpty()) {
            return Response.serverError().entity("Invalid username").build();
        } else if (userService.findById(username) != null) {
            return Response.serverError().entity("Username in use already").build();
        } else {
            return Response.ok("Username is available").build();
        }
    }

    /**
     * Creates a new user
     *
     * @param user user that should be created
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/register")
    public Response createUser(User user) {
        if (userService.findById(user.getUsername()) != null) {
            return Response.serverError().entity("User already exists").build();
        }
        userService.create(user);
        userService.sendAccountCreatedEmail(user);
        User createdUser = userService.findById(user.getUsername());
        if (createdUser == null) {
            return Response.serverError().entity("Error creating user").build();
        }
        return Response.ok(user).build();
    }

    /**
     * Creates a new user with a token for a invitation
     *
     * @param token token of the invitation for a team
     * @param user user that should be created
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/register/{token}")
    public Response createUserWithToken(@PathParam("token") String token, User user) {
        if (userService.findById(user.getUsername()) != null) {
            return Response.serverError().entity("User already exists").build();
        }
        userService.create(user);
        userService.sendAccountCreatedEmail(user);
        User createdUser = userService.findById(user.getUsername());
        if (createdUser == null) {
            return Response.serverError().entity("Error creating user").build();
        }
        if (token != null && invitationService.acceptInvitation(user, token)) {
            return Response.ok(user).build();
        }
        return Response.serverError().entity("Error while adding user, User is already in this team or team is full").build();
    }

    /**
     * Updates a user
     *
     * @param user user with updated information
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    @RolesAllowed({"User", "Admin"})
    public Response updateUser(User user) {
        userService.edit(user);
        return Response.ok(userService.findById(user.getUsername())).build();
    }

    /**
     * Deletes a user
     *
     * @param userId id of the user that should be deleted
     */
    @DELETE
    @Path("/{userId}")
    @RolesAllowed({"Admin"})
    public void deleteUser(@PathParam("userId") Long userId) {
        userService.remove(userId);
    }

    /**
     * Logs in the user with the specified name and password.
     * @param username The username of the user.
     * @param password The password of the user
     * @return A Response indicating the login success.
     */
    @POST
    @Path("/login")
    @PermitAll
    public Response login(
            @FormParam("username") String username,
            @FormParam("password") String password) {
        try {
            request.getSession();
            request.login(username, password);
            Logger.logMsg(Logger.INFO, "User: " + request.isUserInRole("User"));
            Logger.logMsg(Logger.INFO, "Admin: " + request.isUserInRole("Admin"));
            Logger.logMsg(Logger.INFO, "Logged in user: " + request.getRemoteUser());
        } catch (ServletException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
            return Response.serverError().entity(ex.getMessage()).build();
        }
        return Response.ok("Logged in as " + username).build();
    }

    /**
     * Checks whether the specified user is logged in.
     * @param username The name of the user to check
     * @return True if the specified user is logged in, otherwise false.
     */
    @GET
    @Path("/isLoggedIn/{username}")
    @PermitAll
    public boolean isLoggedIn(
            @PathParam("username") String username) {
        return username.equals(request.getRemoteUser());
    }

    /**
     * Gets the currently logged in User
     *
     * @return The currently logged in User object, or null if no one is logged
     * in.
     */
    @GET
    @Path("/authenticated")
    @PermitAll
    public User getAuthenticatedUser() {
        String username = request.getRemoteUser();
        if (username != null) {
            return userService.findById(username);
        }
        return null;

    }

    /**
     * Logs out the current user. Cannot log out if no one is logged in.
     *
     * @return A Response indicating whether the logged in user logged out
     * successfully.
     */
    @POST
    @Path("/logout")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"User", "Admin"})
    public Response logout() {
        try {
            Logger.logMsg(Logger.INFO, "User: " + request.isUserInRole("User"));
            Logger.logMsg(Logger.INFO, "Admin: " + request.isUserInRole("Admin"));
            Logger.logMsg(Logger.INFO, "Logged in user: " + request.getRemoteUser());
            request.getSession().invalidate();
            request.logout();
        } catch (ServletException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
            return Response.serverError().entity(ex.getMessage()).build();
        }
        return Response.ok("Logged out!").build();
    }
    //</editor-fold>
}
