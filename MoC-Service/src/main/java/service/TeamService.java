package service;

import dao.AbstractDAO;
import dao.InvititationDAO;
import dao.TeamDAO;
import domain.Invitation;
import domain.Team;
import domain.User;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.*;

@Stateless
@RequestScoped
public class TeamService extends AbstractService<Team> {

    @Inject
    private TeamDAO teamDao;

    @Inject
    private InvititationDAO invitationDao;

    private final SecureRandom random = new SecureRandom();

    @Override
    protected AbstractDAO getDAO() {
        return teamDao;
    }

    /**
     * Find all teams that belong to a certain competition
     *
     * @param competitionId the id of the competition
     * @return list with teams that belong to the competition
     */
    public List<Team> findByCompetition(long competitionId) {
        return teamDao.findByCompetition(competitionId);
    }

    //<editor-fold defaultstate="collapsed" desc="Invites">
    /**
     * Invites a member to a certain team
     *
     * @param email email address of the person that should be invited
     * @param teamId id of the team that the person should be invited to
     */
    public void inviteMember(String email, long teamId) {
        //Generate token
        String token = generateToken();
        Team t = teamDao.findById(teamId);
        Invitation invite = new Invitation(t, email, token);
        invitationDao.create(invite);
        //send email
        // Recipient's email ID needs to be mentioned.
        String to = "c.linschooten@gmail.com";
        // Sender's email ID needs to be mentioned
        String from = "no-reply@MastersOfCode.com";
        // Assuming you are sending email from localhost
        String host = "localhost";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set Subject: header field
            message.setSubject("This is the Subject Line!");
            // Send the actual HTML message, as big as you like
            message.setContent("<h1>This is actual message</h1>", "text/html");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    /**
     * Lets a user join a certain team
     *
     * @param user user that should join the team
     * @param token string to verify if the user is allowed to join the team
     * @param teamId id of the team that the user should join
     */
    public void joinTeam(User user, String token, long teamId) {
        Team t = teamDao.findById(teamId);

        throw new UnsupportedOperationException();
    }

    /**
     * Lets a user leave a certain team
     *
     * @param user user that should leave the team
     * @param teamId id of the team that the user should leave
     */
    public void leaveTeam(User user, long teamId) {
        throw new UnsupportedOperationException();
    }

    private String generateToken() {
        return new BigInteger(130, random).toString(32);
    }
    //</editor-fold>

}
