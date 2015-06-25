package service;

import com.sun.media.jfxmedia.logging.Logger;
import domain.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;

/**
 * Service class used to manage users
 *
 * @author TeamKoekje
 */
@Stateless
@RequestScoped
public class UserService extends GenericService<User> {

    @Resource(name = "mail/MoC")
    private Session session;

    /**
     * Creates a new instance of the UserService class
     */
    public UserService() {
        super(User.class);
    }

    @Override
    public void create(User u) {
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
            Logger.logMsg(Logger.ERROR, ex.getMessage());
        }
        return hash;
    }

    /**
     * Sends an email to the specified user, notifying that their account has
     * been created.
     *
     * @param u The user to mail to.
     */
    public void sendAccountCreatedEmail(User u) {
        try {
            Message message = new MimeMessage(session);
            //normally, the following should be set, but as the Session is injected, 
            //it uses the settings from the server. Which already set this, making it irrelevant.
            message.setFrom(new InternetAddress("TeamKoekje@gmail.com"));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(u.getEmail()));
            message.setSubject("Account created succesfully");
            message.setHeader("Content-Type", "text/html");

            URL url = new URL("http://daangoumans.nl/etc/MoC/accountCreateMail.html");
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            String mail = sb.toString();
            mail = mail.replace("#email", u.getEmail());
            mail = mail.replace("#username", u.getUsername());
            message.setContent(mail, "text/html");
            Transport.send(message);
            Logger.logMsg(Logger.INFO, "Sent message successfully....");
        } catch (MessagingException | IOException ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
        }
    }

    /**
     * Search in the user table for users which contain the specified input in
     * the username.
     *
     * @param searchInput The String the users must have in their name.
     * @return A List of User objects, which contain the input in their name.
     */
    public List<User> searchUsers(String searchInput) {
        Query q = em.createNamedQuery("User.searchUsers");
        q.setParameter("searchInput", "%" + searchInput + "%");
        return (List<User>) q.getResultList();
    }
}
