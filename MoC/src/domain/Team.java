/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Astrid
 */
public class Team {
    
    private List<Participant> participants;
    
    public Team(Participant participant){
        this.participants = new ArrayList<>();
        this.participants.add(participant);
    }
    
    public void addParticipant(Participant participant){
        this.participants.add(participant);
    }
}
