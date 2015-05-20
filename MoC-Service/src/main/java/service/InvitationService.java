package service;

import domain.Competition;
import domain.Invitation;
import domain.Team;
import java.io.BufferedReader;
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

@Stateless
@RequestScoped
public class InvitationService extends GenericService<Invitation> {

    @Resource(name = "mail/MoC")
    private Session session; //mailing session

    private final SecureRandom random = new SecureRandom();

    @Inject
    private CompetitionService competitionService;

    public InvitationService() {
        super(Invitation.class);
    }

    //<editor-fold defaultstate="collapsed" desc="Invites">
    /**
     * Invites a member to a certain team
     *
     * @param email email address of the person that should be invited
     * @param teamId id of the team that the person should be invited to
     * @param competitionId
     *
     *
     */
    public void inviteMember(String email, long teamId, Long competitionId) {
        /*
        TODO:
        Catch errors
         */
        
        //Generate token
        String token = generateToken();

        //Get Team
        Team team = null;
        Competition comp = competitionService.findById(competitionId);

        List< Team> teams = comp.getTeams();
        for (Team allTeams : teams) {
            if (allTeams.getId() == teamId) {
                team = allTeams;
            }
        }

        //Create invite
        Invitation invite = new Invitation(team, email, token);

        //Send email
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("c.linschooten@gmail.com"));//this doesnt work as the injected session will override it
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));

            message.setSubject("You have been Invited!");
            message.setHeader("Content-Type", "text/html");

            /*
             email content            
             */
            //get mail.html file to string
            URL url = new URL("http://localhost:8080/MoC-Service/mail.html");
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
            mail = mail.replace("#competitionId", String.valueOf(competitionId));
            //replace #teamId
            mail = mail.replace("#teamId", String.valueOf(teamId));
            //replace #token
            mail = mail.replace("#token", token);
            //replace #teamname
            mail = mail.replace("#teamname", team.getName());
            //set Content
            message.setContent(mail, "text/html");
            /*
             end email content
             */
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Persist invite
        this.create(invite);

    }

    private String generateToken() {
        return new BigInteger(130, random).toString(32);
    }
 //</editor-fold>
}
