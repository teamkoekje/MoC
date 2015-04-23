package service;

import domain.Invitation;
import java.math.BigInteger;
import java.security.SecureRandom;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.mail.*;
import javax.mail.internet.*;

@Stateless
@RequestScoped
public class InvitationService extends GenericService<Invitation> {

    @Resource(name = "mail/MoC")
    private Session session; //mailing session

    private final SecureRandom random = new SecureRandom();

    public InvitationService() {
        super(Invitation.class);
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
        //String token = generateToken();
        //Team t = new Team(new Participant());
        //Team t = teamDao.findById(teamId);
        //Invitation invite = new Invitation(t, "c.linschooten@gmail.com", token);
        //invitationDao.create(invite);
        //send email

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("c.linschooten@gmail.com"));//this doesnt work as the injected session will override it
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("c.linschooten@gmail.com"));
            message.setSubject("BEEEERRRRR!!");
            message.setText("Let's have a beer party sometime.");

            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //this one uses ssl instead of tsl, untested code
        /*Properties props = new Properties();
         props.put("mail.smtp.host", "smtp.moc.com");
         props.put("mail.smtp.socketFactory.port", "25");
         props.put("mail.smtp.socketFactory.class",
         "javax.net.ssl.SSLSocketFactory");
         props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.port", "25");

         Session session2 = Session.getDefaultInstance(props,
         new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
         return new PasswordAuthentication("no-reply@smtp.moc.com","admin");
         }
         });

         try {

         Message message = new MimeMessage(session2);
         message.setFrom(new InternetAddress("no-reply@smtp.moc.com"));
         message.setRecipients(Message.RecipientType.TO,
         InternetAddress.parse("c.linschooten@gmail.com"));
         message.setSubject("Testing Subject");
         message.setText("Dear Mail Crawler," +
         "\n\n No spam to my email, please!");

         Transport.send(message);

         System.out.println("Done");

         } catch (MessagingException e) {
         throw new RuntimeException(e);
         }*/
    }

    private String generateToken() {
        return new BigInteger(130, random).toString(32);
    }
    //</editor-fold>

}
