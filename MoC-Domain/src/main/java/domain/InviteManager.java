package domain;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A class used for managing invitations. Each team has their own InviteManager.
 */
public class InviteManager {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
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

    private final HashMap<String, InvitationState> sentInvitations;
    // </editor-fold >

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    /**
     * Creates a new instance of the InviteManager.
     */
    public InviteManager() {
        sentInvitations = new HashMap<>();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Get & Setters" >
    /**
     * Gets the iterator for HashMap of the sent invitations. The key is the
     * e-mail and the state of the invitation is represented as the value.
     *
     * @return An iterator representing the sent invitations HashMap.
     */
    public Iterator iterator() {
        return sentInvitations.entrySet().iterator();
    }

    /**
     * Changes the state associated with the given e-mail. If the e-mail is
     * unknown, an IllegalArgumentException is thrown.
     *
     * @param email The email to change the state of.
     * @param state The state to change to
     */
    public void setInvitationState(String email, InvitationState state) {
        if (sentInvitations.containsKey(email)) {
            sentInvitations.put(email, state);
        } else {
            throw new IllegalArgumentException("Unknown email");
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Adds the specified email to the map of sent invitations and sets the
     * state to undecided. If the email is already present, an
     * IllegalArgumentException is thrown.
     *
     * @param email The email to add.
     */
    public void addInvitation(String email) {
        if (!sentInvitations.containsKey(email)) {
            sentInvitations.put(email, InvitationState.UNDECIDED);
        }else{
            throw new IllegalArgumentException("Email already present.");
        }
    }
    // </editor-fold>
}
