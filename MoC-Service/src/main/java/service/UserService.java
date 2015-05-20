package service;

import domain.User;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    
    @Override
    public void create(User u){
        String passwordHash = getHash(u.getPassword());
        u.setPassword(passwordHash);
        em.persist(u);
    }

    private String getHash(String text) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hash = bigInt.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            System.err.println(ex.getMessage());
        }
        return hash;
    }
}
