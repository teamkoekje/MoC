package domain;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import domain.enums.RoundState;
import domain.Events.CompetitionEndedEvent;
import domain.Events.CompetitionEvent;
import domain.Events.HintReleasedEvent;
import domain.Events.MessageReleasedEvent;
import domain.Events.RoundEndedEvent;
import domain.enums.CompetitionState;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlAttribute;
// </editor-fold>

/**
 * The Competition class represents a Masters of Code competition. A competition
 * contains a name, competitionDate, startTime and location. It also has a
 * minimum and maximum team size, a list of rounds and a list of participating
 * teams.
 *
 * @author TeamKoekje
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Competition.findActive",
            query = "SELECT comp FROM Competition comp WHERE comp.competitionState = :state")})
public class Competition implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    @Id
    @GeneratedValue
    @XmlAttribute
    private long id;

    private String name;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date competitionDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startTime;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endTime;

    private String location;

    private int minTeamSize;
    private int maxTeamSize;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Round> rounds = new ArrayList<>();

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private final List<Team> teams = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Round currentRound;

    private CompetitionState competitionState = CompetitionState.NOT_STARTED;

    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    protected Competition() {
    }

    public Competition(String name, Date competitionDate, Date startingTime, Date endTime, String location) {
        this.name = name;
        this.competitionDate = competitionDate;
        this.startTime = startingTime;
        this.endTime = endTime;
        this.location = location;
    }
    
     public Competition(String name, Date competitionDate, Date startingTime, Date endTime, String location, int minTeamSize, int maxTeamSize) {
        this.name = name;
        this.competitionDate = competitionDate;
        this.startTime = startingTime;
        this.endTime = endTime;
        this.location = location;
        this.competitionState = CompetitionState.NOT_STARTED;
        this.minTeamSize = minTeamSize;
        this.maxTeamSize = maxTeamSize;
    }
    
    //</editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters" >
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCompetitionDate() {
        return competitionDate;
    }

    public void setCompetitionDate(Date competitionDate) {
        this.competitionDate = competitionDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMinTeamSize() {
        return minTeamSize;
    }

    public void setMinTeamSize(int minTeamSize) {
        this.minTeamSize = minTeamSize;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public List<Challenge> getChallenges() {
        List<Challenge> challenges = new ArrayList<>();
        for (Round r : rounds) {
            challenges.add(r.getChallenge());
        }
        return challenges;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public Team getTeamByUsername(String username) {
        for (Team t : teams) {
            if (t.containsParticipant(username)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Gets the CompetitionState of this Competition.
     *
     * @return A CompetitionState object indicating the state of this
     * Competition.
     */
    public CompetitionState getCompetitionState() {
        return this.competitionState;
    }

    /**
     * Sets the competitionState to the specified state
     *
     * @param competitionState The new state of the competition.
     */
    protected void setCompetitionState(CompetitionState competitionState) {
        this.competitionState = competitionState;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    public List<CompetitionEvent> update() {
        if (currentRound != null) {
            List<CompetitionEvent> events = currentRound.update();
            for (CompetitionEvent event : events) {
                switch (event.getType()) {
                    case ROUND_ENDED:
                        RoundEndedEvent ree = (RoundEndedEvent) event;
                        ree.setCompetition(this);
                        System.out.println("Round ended: " + ree.getEndedRound().getChallenge().getName());
                        //if there is another round, set it as current
                        int nextRoundOrder = ree.getEndedRound().getRoundOrder() + 1;
                        if (rounds.size() > nextRoundOrder) {
                            currentRound = rounds.get(nextRoundOrder);

                            //oterwise, tell the calling service the entire competition has ended
                        } else {
                            currentRound = null;
                            competitionState = CompetitionState.ENDED;
                            ArrayList<CompetitionEvent> temp = new ArrayList<>();
                            temp.add(new CompetitionEndedEvent(this));
                            return temp;
                        }
                        break;
                    case COMPETITION_ENDED:
                        throw new IllegalArgumentException("Received a CompetitionEndedEvent from a round - should never happen");
                    case HINT_RELEASED:
                        HintReleasedEvent hre = (HintReleasedEvent) event;
                        System.out.println("Hint released: " + hre.getReleasedHint().getContent());
                        break;
                    case MESSAGE_RELEASED:
                        MessageReleasedEvent mre = (MessageReleasedEvent) event;
                        System.out.println("Message released: " + mre.getReleasedMessage().getContent());
                        break;
                    default:
                        throw new AssertionError(event.getType().name());
                }
            }
            return events;
        }
        return new ArrayList<>();
    }

    /**
     * Function adds a challenge to the competition.
     *
     * @param challenge challenge that is added to the competition
     * @param time time in seconds allowed for completing the challenge
     */
    public void addChallenge(Challenge challenge, int time) {
        Round r = new Round(challenge, time);
        r.setRoundOrder(rounds.size());
        rounds.add(r);
    }

    /**
     * Function removes a challenge from the competition.
     *
     * @param challenge challenge that has to be removed
     */
    public void removeChallenge(Challenge challenge) {
        for (Round r : rounds) {
            if (r.getChallenge() == challenge) {
                rounds.remove(r);
                return;
            }
        }
    }

    /**
     * Function changes the position of a challenge within the competition.
     *
     * @param challenge challenge that has to change position
     * @param position the position to which the challenge should be set
     */
    public void changeChallengeOrder(Challenge challenge, int position) {
        //unneeded? SHOULD be edit round, but we already create/remove/edit rounds using RoundService/DAO
    }

    /**
     * Function add a participating team to the competition.
     *
     * @param team team that needs to be added
     */
    public void addTeam(Team team) {
        teams.add(team);
    }

    /**
     * Remove a participating team from the competition.
     *
     * @param team team that needs to be removed
     */
    public void removeTeam(Team team) {
        teams.remove(team);
    }

    /**
     * Tells the current round the specified Team is done.
     *
     * @param toSubmit The Team that is done.
     */
    public void submit(Team toSubmit) {
        currentRound.submit(toSubmit);
    }

    public boolean joinTeam(User user, long teamId) {
        for (Team t : teams) {
            if (t.getId() == teamId) {
                t.addParticipant(user);
            }
        }
        return false;
    }

    public boolean participantIsInTeam(User p) {
        for (Team t : teams) {
            if (t.getParticipants().contains(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to start the next round in the competition (including starting
     * the first round).
     *
     * @throws IllegalStateException Thrown if the current round's state is
     * NOT_STARTED or there are no rounds to start.
     */
    public void startNextRound() throws IllegalStateException {
        switch (competitionState) {
            case NOT_STARTED:
                if (rounds.size() > 0) {
                    currentRound = rounds.get(0);
                    currentRound.start();
                    competitionState = CompetitionState.ONGOING;
                } else {
                    throw new IllegalStateException("Can't start the first round as there are no rounds defined in the competition");
                }
                break;
            case ONGOING:
                if (currentRound.getRoundState() == RoundState.NOT_STARTED) {
                    currentRound.start();
                } else {
                    throw new IllegalStateException(
                            "Can't start the current round as it's state is not NOT_STARTED, current state: "
                            + currentRound.getRoundState());
                }
                break;
            case ENDED:
                throw new IllegalStateException("Can't start the first round as the Competition is already over.");
            default:
                throw new AssertionError(competitionState.name());
        }
    }
    //</editor-fold>
}
