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

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    private List<Participant> participants;
    private final Participant initiatior;
    private String name;
    private final Competition competition;
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    public Team(Participant initiatior) {
        this.initiatior = initiatior;
        this.competition = Competition.getInstance();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters & Setters" >
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="methods" >
    /**
     * Function adds a participant to the team.
     *
     * @param participant participant that should be added
     */
    public void addParticipant(Participant participant) {

    }

    /**
     * Function removes a participant from the team.
     *
     * @param participant participant that should be removed
     */
    public void removeParticipant(Participant participant) {

    }

    /**
     * Tells the competition this Team is done with the current Round.
     */
    public void submit() {
        competition.submit(this);
    }
    //</editor-fold>
}
