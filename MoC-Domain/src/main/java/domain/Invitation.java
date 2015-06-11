package domain;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
// </editor-fold>

/**
 * Used to keep track of invitations sent out by teams to people they wish to
 * participate in a competition with them.
 *
 * @author TeamKoekje
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Invitation.findByToken",
            query = "SELECT inv FROM Invitation inv WHERE inv.token = :token"),

    @NamedQuery(name = "Invitation.findByTeam",
            query = "SELECT inv FROM Invitation inv WHERE inv.team = :team AND inv.invitationState != :state"),

    @NamedQuery(name = "Invitation.countUndecidedInvitations",
            query = "SELECT COUNT(inv) FROM Invitation inv WHERE inv.email = :email AND inv.team = :team AND inv.invitationState = :state"),

    @NamedQuery(name = "Invitation.findByEmail",
            query = "SELECT inv FROM Invitation inv WHERE inv.email = :email")})
public class Invitation implements Serializable {

    /**
     * An enum indicating the state of an invitation
     */
    public enum InvitationState {

        /**
         * Indicates that an invitation has not been accepted or declined yet,
         * this should be the default state.
         */
        UNDECIDED,
        /**
         * Indicates an invitation has been accepted.
         */
        ACCEPTED,
        /**
         * Indicates an invitation has been declined.
         */
        DECLINED
    }

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    @Id
    @GeneratedValue
    @XmlAttribute
    private long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @XmlElement
    private final Team team;
    @XmlElement
    private String email;
    private final String token;
    private InvitationState invitationState;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    protected Invitation() {
        team = null;
        email = "no email";
        token = "no token";
    }

    public Invitation(Team team, String email, String token) {
        this.team = team;
        this.email = email;
        this.token = token;
        this.invitationState = InvitationState.UNDECIDED;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter & Setters" >
    public InvitationState getState() {
        return invitationState;
    }

    public void setState(InvitationState state) {
        this.invitationState = state;
    }

    public Team getTeam() {
        return team;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >    
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Invitation)) {
            return false;
        }
        Invitation otherInvitation = (Invitation) other;
        return otherInvitation.email.equals(this.email);
    }
    // </editor-fold>
}
