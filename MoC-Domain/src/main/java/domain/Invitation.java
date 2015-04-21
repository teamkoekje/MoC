package domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Used to keep track of invitations sent out by teams to people they wish to
 * participate in a competition with them.
 *
 * @author TeamKoekje
 */
@Entity
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

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private final Team team;
    private final String email;
    private final String token;
    private InvitationState invitationState;

    protected Invitation() {
        team = /*new Team(new Participant());*/ null;
        email = "no email";
        token = "no token";
    }

    public Invitation(Team team, String email, String token) {
        this.team = team;
        this.email = email;
        this.token = token;
        this.invitationState = InvitationState.UNDECIDED;
    }

    public InvitationState getState() {
        return invitationState;
    }

    public void setState(InvitationState state) {
        this.invitationState = state;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
