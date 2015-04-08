package api;

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
import service.UserService;

/**
 *
 * @author Astrid
 */
@Path("user")
public class UserResource {

    @Inject
    private UserService service;

    //<editor-fold defaultstate="collapsed" desc="User">
    /**
     * Gets all users
     *
     * @return list with users
     */
    @GET
    @Produces("application/xml,application/json")
    public List<User> getUsers() {
        throw new UnsupportedOperationException();
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
    public User getUserById(@PathParam("userId") String userId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new user
     *
     * @param user user that should be created
     */
    @POST
    @Consumes("application/xml,application/json")
    public void createUser(User user) {
        throw new UnsupportedOperationException();
    }

    /**
     * Updates a user
     *
     * @param user user with updated information
     * @param userId id of the user that should be updated
     */
    @POST
    @Consumes("application/xml,application/json")
    @Path("/{userId}")
    public void updateUser(User user, @PathParam("userId") String userId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes a user
     *
     * @param userId id of the user that should be deleted
     */
    @DELETE
    @Path("/{userId}")
    public void deleteUser(@PathParam("userId") String userId) {
        throw new UnsupportedOperationException();
    }
    //</editor-fold>
}
