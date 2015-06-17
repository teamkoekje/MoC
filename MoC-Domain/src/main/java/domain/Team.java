package domain;

// <editor-fold defaultstate="collapsed" desc="Imports" >
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
// </editor-fold>

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

    @XmlAttribute
    private int score = 0;

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

    public void setOwner(User owner) {
        addParticipant(owner);
        this.owner = owner;
    }

    public Competition getCompetition() {
        return competition;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Function adds a participant to the team.
     *
     * @param participant participant that should be added
     * @return True if the participant was added, otherwise false (already in
     * this team or team is full)
     */
    public boolean addParticipant(User participant) {
        if (teamIsFull()) {
            return false; // team is full
        } else if (participants.contains(participant)) {
            return false; // user already in team
        } else {
            participants.add(participant);
            participant.addTeam(this);
            return true;
        }
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

    /**
     * Checks whether the specified username is found in the list of Users in
     * this Team.
     *
     * @param username The name to check for
     * @return True if the username was found in the users of this Team,
     * otherwise false.
     */
    boolean containsParticipant(String username) {
        for (User p : participants) {
            if (p.getUsername().toUpperCase().equals(username.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    boolean teamIsFull() {
        return participants.size() >= competition.getMaxTeamSize();
    }
    //</editor-fold>
}
