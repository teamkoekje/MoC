/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.List;

/**
 * The team class represents a team that participates in a competition. A team
 * consists of one or more participants, of which one is the initiator. A team
 * also has a name.
 *
 * @author Astrid Belder
 */
public class Team {

    private List<User> participants;
    private User initiatior;
    private String name;

    public Team(User initiatior) {
        this.initiatior = initiatior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Function adds a participant to the team.
     *
     * @param participant participant that should be added
     */
    public void addParticipant(User participant) {

    }

    /**
     * Function removes a participant from the team.
     *
     * @param participant participant that should be removed
     */
    public void removeParticipant(User participant) {

    }

}
