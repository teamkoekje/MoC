package service;

import domain.User;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;

/**
 * Service class used to manage users
 *
 * @author Astrid
 */
@Stateless
@RequestScoped
public class UserService extends GenericService<User> {

    public UserService() {
        super(User.class);
    }

}
