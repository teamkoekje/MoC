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
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The team class represents a team that participates in a competition. A team
 * consists of one or more participants, of which one is the initiator. A team
 * also has a name.
 *
 * @author TeamKoekje
 */
@Entity
public class Team implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    @Id
    @GeneratedValue
    @XmlAttribute
    private long id;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Participant> participants;
    @OneToOne(cascade = CascadeType.PERSIST)
    private final Participant initiatior;
    private String name;
    @ManyToOne
    private Competition competition;
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    protected Team() {
        this.initiatior = null;
    }

    public Team(Participant initiatior, String name) {
        this.initiatior = initiatior;
        this.name = name;
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

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods" >
    /**
     * Function adds a participant to the team.
     *
     * @param participant participant that should be added
     * @return True if the participant was added, otherwise false (already in
     * this team)
     */
    public boolean addParticipant(Participant participant) {
        if (!participants.contains(participant)) {
            participants.add(participant);
            return true;
        }
        return false;
    }

    /**
     * Function removes a participant from the team.
     *
     * @param participant participant that should be removed
     * @return True if the participant is removed (exists in list), otherwise
     * false
     */
    public boolean removeParticipant(Participant participant) {
        return participants.remove(participant);
    }

    /**
     * Tells the competition this Team is done with the current Round.
     */
    public void submit() {
        competition.submit(this);
    }
    //</editor-fold>

}
