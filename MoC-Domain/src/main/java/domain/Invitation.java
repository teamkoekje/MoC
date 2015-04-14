package domain;

public class Invitation {

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
    
    private final Team team;
    private final String email;
    private final String token;
    private InvitationState state;

    public Invitation(Team team, String email, String token) {
        this.team = team;
        this.email = email;
        this.token = token;
        this.state = InvitationState.UNDECIDED;
    }

    public InvitationState getState() {
        return state;
    }

    public void setState(InvitationState state) {
        this.state = state;
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
}
