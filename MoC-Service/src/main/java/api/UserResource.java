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
 * @author Astrid
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
    @Produces("application/xml,application/json")
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
    @Produces("application/xml,application/json")
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
    @Produces("application/xml,application/json")
    @Path("/search/{searchInput}")
    @PermitAll
    public List<User> searchUsers(@PathParam("searchInput") String searchInput) {
        return userService.searchUsers(searchInput);
    }

    /**
     * Check if the request is made by an Admin
     *
     * @param request The request
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
    @Produces("application/xml,application/json")
    @Path("/{username}/teams")
    //@RolesAllowed({"User", "Admin"})
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
    @Produces("application/xml,application/json")
    @Path("/{username}/invitations")
    //@RolesAllowed({"User", "Admin"})
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
    @Consumes("application/xml,application/json")
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
    @Consumes("application/xml,application/json")
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
        if (token != null) {
            invitationService.acceptInvitation(user, token);
        }
        return Response.ok(user).build();
    }

    /**
     * Updates a user
     *
     * @param user user with updated information
     * @return
     */
    @POST
    @Consumes("application/xml,application/json")
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

    @POST
    @Path("/login")
    @PermitAll
    public Response login(
            @FormParam("username") String username,
            @FormParam("password") String password) {
        try {
            request.getSession();
            request.login(username, password);
            System.out.println("User: " + request.isUserInRole("User"));
            System.out.println("Admin: " + request.isUserInRole("Admin"));
            System.out.println("Logged in user: " + request.getRemoteUser());
        } catch (ServletException ex) {
            System.err.println(ex.getMessage());
            return Response.serverError().entity(ex.getMessage()).build();
        }
        return Response.ok("Logged in as " + username).build();
    }

    @GET
    @Path("/isLoggedIn/{username}")
    @PermitAll
    public boolean isLoggedIn(
            @PathParam("username") String username) {
        System.out.println("Checking if user is logged in: " + username);
        return username.equals(request.getRemoteUser());
    }

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

    @POST
    @Path("/logout")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"User", "Admin"})
    public Response logout() {
        try {
            System.out.println("User: " + request.isUserInRole("User"));
            System.out.println("Admin: " + request.isUserInRole("Admin"));
            System.out.println("logged out user: " + request.getRemoteUser());
            request.getSession().invalidate();
            request.logout();
        } catch (ServletException ex) {
            System.err.println(ex.getMessage());
            return Response.serverError().entity(ex.getMessage()).build();
        }
        return Response.ok("Logged out!").build();
    }
    //</editor-fold>
}
