package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * The team class represents a team that participates in a competition. A team
 * consists of one or more participants, of which one is the initiator. A team
 * also has a name.
 *
 * @author TeamKoekje
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Team.findByCompetition",
            query = "SELECT t FROM Team t WHERE t.competition = :competition")})
public class Team implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    @Id
    @GeneratedValue
    @XmlAttribute
    private long id;

    @ManyToMany(cascade = CascadeType.ALL)
    private final List<User> participants = new ArrayList<>();

    @ManyToOne
    private User owner;

    private String name;
    
    @ManyToOne
    @XmlElement
    private Competition competition;
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    protected Team() {
    }

    public Team(String name, Competition competition) {
        this.name = name;
        this.competition = competition;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters & Setters" >
    public long getId() {
        //CDI.current().select(EventManager.class).get();
        return id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public User getOwner() {
        return owner;
    }
    
    public void setOwner(User owner){
        addParticipant(owner);
        this.owner = owner;
    }

    public Competition getCompetition() {
        return competition;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Function adds a participant to the team.
     *
     * @param participant participant that should be added
     * @return True if the participant was added, otherwise false (already in
     * this team)
     */
    public boolean addParticipant(User participant) {
        if (!participants.contains(participant)) {
            participants.add(participant);
            participant.addTeam(this);
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
    public boolean removeParticipant(User participant) {
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
