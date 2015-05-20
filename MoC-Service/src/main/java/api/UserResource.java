package api;

import domain.User;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
import service.UserService;

/**
 *
 * @author Astrid
 */
@Path("user")
@DeclareRoles({"User", "Admin"})
public class UserResource {

    @Inject
    private UserService userService;

    //<editor-fold defaultstate="collapsed" desc="User">
    /**
     * Gets all users
     *
     * @return list with users
     */
    @GET
    @Produces("application/xml,application/json")
    @RolesAllowed("Admin")
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
    public User getUserById(@PathParam("userId") Long userId) {
        return userService.findById(userId);
    }

    /**
     * Creates a new user
     *
     * @param user user that should be created
     */
    @POST
    @Consumes("application/xml,application/json")
    //@RolesAllowed("Admin")
    @DenyAll
    public void createUser(User user) {
        userService.create(user);
    }

    /**
     * Updates a user
     *
     * @param user user with updated information
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/update")
    @RolesAllowed({"User", "Admin"})
    public void updateUser(User user) {
        userService.edit(user);
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
    public String login(
            @FormParam("username") String username,
            @FormParam("password") String password,
            @Context HttpServletRequest request) {
        try {
            request.getSession();
            request.login(username, password);
            System.out.println("User: " + request.isUserInRole("User"));
            System.out.println("Admin: " + request.isUserInRole("Admin"));
            System.out.println("Logged in user: " + request.getRemoteUser());
        } catch (ServletException ex) {
            System.err.println(ex.getMessage());
            return ex.getMessage();
        }
        return "Logged in as " + username;
    }

    @GET
    @Path("/isLoggedIn/{username}")
    @PermitAll
    public boolean isLoggedIn(
            @PathParam("username") String username,
            @Context HttpServletRequest request) {
        System.out.println("Checking if user is logged in: " + username);
        return username.equals(request.getRemoteUser());
    }

    @POST
    @Path("/logout")
    @RolesAllowed({"User", "Admin"})
    public String logout(
            @Context HttpServletRequest request) {
        try {
            System.out.println("User: " + request.isUserInRole("User"));
            System.out.println("Admin: " + request.isUserInRole("Admin"));
            System.out.println("logged out user: " + request.getRemoteUser());
            request.getSession().invalidate();
            request.logout();
        } catch (ServletException ex) {
            System.err.println(ex.getMessage());
            return ex.getMessage();
        }
        return "Logged out!";
    }
    //</editor-fold>
}
