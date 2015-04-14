package domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * The team class represents a team that participates in a competition. A team
 * consists of one or more participants, of which one is the initiator. A team
 * also has a name.
 *
 */
@Entity
public class Team implements Serializable{

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    @Id
    @GeneratedValue
    private long id;

    @OneToMany(cascade = CascadeType.PERSIST) 
    private List<Participant> participants;
    @OneToOne(cascade = CascadeType.PERSIST) 
    private final Participant initiatior;
    private String name;
    @ManyToOne(cascade = CascadeType.PERSIST) 
    private final Competition competition;
    //private InviteManager inviteManager;
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    protected Team(){
        this.competition = Competition.getInstance();
        this.initiatior = null;
    }
    
    public Team(Participant initiatior) {
        this.initiatior = initiatior;
        this.competition = Competition.getInstance();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters & Setters" >
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public Participant getInitiatior() {
        return initiatior;
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="methods" >
    /**
     * Function adds a participant to the team.
     *
     * @param participant participant that should be added
     */
    public void join(Participant participant) {

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
