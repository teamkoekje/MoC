/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.HashMap;

/**
 *
 * @author Robin
 */
public class InviteManager {
    public enum InvitationState{UNDECIDED, ACCEPTED, DECLINED}
    
    private HashMap<String, InvitationState> sentInvitations;

    public HashMap<String, InvitationState> getSentInvitations() {
        return sentInvitations;
    }
    
    public void addInvitation(String email){
        sentInvitations.put(email, InvitationState.UNDECIDED);
    }
    
    public void setInvitationState(String email, InvitationState state){
        if (sentInvitations.containsKey(email)) {
            sentInvitations.replace(email, state);
        }else{
            throw new IllegalArgumentException("Unknown email");
        }
    }
}
