package service;

import com.sun.media.jfxmedia.logging.Logger;
import domain.Competition;
import domain.Invitation;
import domain.Invitation.InvitationState;
import domain.Team;
import domain.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.*;
import javax.persistence.Query;

@Stateless
@RequestScoped
public class InvitationService extends GenericService<Invitation> {

    @Resource(name = "mail/MoC")
    private Session session; //mailing session

    private final SecureRandom random = new SecureRandom();

    @Inject
    CompetitionService competitionService;

    @Inject
    TeamService teamService;

    public InvitationService() {
        super(Invitation.class);
    }

    //<editor-fold defaultstate="collapsed" desc="Invites">
    /**
     * Invites a member to a certain team
     *
     * @param email email address of the person that should be invited
     * @param teamId id of the team that the person should be invited to
     *
     */
    public void inviteMember(String email, long teamId) {
        /*
         TODO:
         Catch errors
         */

        //Generate token
        String token = generateToken();
        Team team = teamService.findById(teamId);
        Competition comp = team.getCompetition();

        //Create invite
        Invitation invite = new Invitation(team, email, token);

        //Send email
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("TeamKoekje@gmail.com"));//this doesnt work as the injected session will override it
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));

            message.setSubject("You have been Invited!");
            message.setHeader("Content-Type", "text/html");

            /*
             email content            
             */
            //get mail.html file to string
            URL url = new URL("http://daangoumans.nl/etc/MoC/mail.html");
            URLConnection con = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            String mail = sb.toString();

            //replace #competitionId/team/#teamId/join/#token
            //replace #competitionId
            mail = mail.replace("#competitionId", String.valueOf(comp.getId()));
            //replace #teamId
            mail = mail.replace("#teamId", String.valueOf(teamId));
            //replace #token
            mail = mail.replace("#token", token);
            //replace #teamname
            mail = mail.replace("#teamname", team.getName());
            //set Content
            message.setContent(mail, "text/html");
            Transport.send(message);
        } catch (MessagingException | IOException ex) {
            System.out.println(ex.getMessage());
            Logger.logMsg(Logger.ERROR, ex.getMessage());
        }

        //Persist invite
        this.create(invite);

    }

    private String generateToken() {
        return new BigInteger(130, random).toString(32);
    }

    public Invitation findByToken(String token) {
        Query q = em.createNamedQuery("Invitation.findByToken");
        q.setParameter("token", token);
        return (Invitation) q.getSingleResult();
    }

    public List<Invitation> findByEmail(String email) {
        Query q = em.createNamedQuery("Invitation.findByEmail");
        q.setParameter("email", email);
        return (List<Invitation>) q.getResultList();
    }

    /**
     * Lets a user join a certain team
     *
     * @param user user that should join the team
     * @param token string to verify if the user is allowed to join the team
     */
    public boolean acceptInvitation(User user, String token) {
        Invitation inv = findByToken(token);
        Team team = inv.getTeam();

        boolean result = team.addParticipant(user);
        if (result) {
            inv.setState(Invitation.InvitationState.ACCEPTED);
            teamService.edit(team);
            this.edit(inv);
            return true;
        }
        return false;
    }

    public void declineInvitation(long invitationId) {
        Invitation inv = findById(invitationId);
        inv.setState(Invitation.InvitationState.DECLINED);
        edit(inv);
    }

    public List<Invitation> findInvitationsByTeam(Team team) {
        Query q = em.createNamedQuery("Invitation.findByTeam");
        q.setParameter("team", team);
        q.setParameter("state", InvitationState.ACCEPTED);
        return (List<Invitation>) q.getResultList();
    }

    public boolean emailAlreadyInTeam(String email, long teamId) {
        Team team = teamService.findById(teamId);
        for (Team t : teamService.findByCompetition(team.getCompetition())) {
            for (User u : t.getParticipants()) {
                if (u.getEmail().equals(email)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean emailAlreadyInvited(String email, long teamId) {
        Query q = em.createNamedQuery("Invitation.countUndecidedInvitations");
        q.setParameter("email", email);
        q.setParameter("team", teamService.findById(teamId));
        q.setParameter("state", InvitationState.UNDECIDED);
        return ((Long) q.getSingleResult()) > 0;
    }

 //</editor-fold>
}
