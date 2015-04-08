package service;

import dao.AbstractDAO;
import dao.UserDAO;
import domain.User;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

/**
 * Service class used to manage users
 *
 * @author Astrid
 */
@Stateless
@RequestScoped
public class UserService extends AbstractService<User> {

    @Inject
    private UserDAO dao;

    @Override
    protected AbstractDAO getDAO() {
        return dao;
    }
}
