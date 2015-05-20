package domain;

import domain.Invitation.InvitationState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A class used for managing invitations. Each team has their own InviteManager.
 * 
 * @author TeamKoekje
 */
public class InviteManager {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    private final List<Invitation> sentInvitations;
    // </editor-fold >

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    /**
     * Creates a new instance of the InviteManager.
     */
    public InviteManager() {
        sentInvitations = new ArrayList<>();
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
        return sentInvitations.iterator();
    }

    /**
     * Changes the state associated with the given e-mail. If the e-mail is
     * unknown, an IllegalArgumentException is thrown.
     *
     * @param email The email to change the state of.
     * @param state The state to change to
     */
    public void setInvitationState(String email, InvitationState state) {
        Invitation i = findInvitation(email);
        if (i != null) {
            i.setState(state);
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
     * @param token
     */
    public void addInvitation(String email, String token) {
        Invitation i = findInvitation(email);
        if (i == null) {
            //sentInvitations.add(new Invitation(email, token));
        } else {
            throw new IllegalArgumentException("Unknown email");
        }
    }

    private Invitation findInvitation(String email) {
        for (Invitation i : sentInvitations) {
            if (i.getEmail().equals(email)) {
                return i;
            }
        }
        return null;
    }
    // </editor-fold>
}
