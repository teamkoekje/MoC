package domain;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import domain.enums.RoundState;
import domain.events.CompetitionEvent;
import domain.events.HintReleasedEvent;
import domain.events.RoundEndedEvent;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlElement;
// </editor-fold>

/**
 * The Round class represents a round within a Masters of Code competition. A
 * round contains a challenge and a roundTime that represents how much roundTime
 * there is left before the round is over.
 *
 * @author TeamKoekje
 */
@Entity
public class Round implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    @Id
    @GeneratedValue
    private long id;

    private int roundOrder;
    private RoundState roundState = RoundState.NOT_STARTED;

    @OneToOne(cascade = CascadeType.ALL)
    private Challenge challenge;

    private long duration;

    @Temporal(javax.persistence.TemporalType.DATE)
    @XmlElement
    private Calendar startTime;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar endTime;

    private List<Hint> hintsCopy;
    //private List<Hint> releasedHints;

    private List<Team> teams;
    @XmlElement
    private List<TeamScore> teamScores;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    protected Round() {
    }

    public Round(Challenge challenge, long roundTime, Collection<Team> teams) {
        if (challenge == null) {
            throw new IllegalArgumentException("Challenge can't be null");
        }
        if (roundTime <= 0) {
            throw new IllegalArgumentException("RoundTime must be positive");
        }
        this.challenge = challenge;
        this.duration = roundTime;
        this.teams = new ArrayList<>(teams);
        //this.releasedHints = new ArrayList<>();
    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters" >
    /**
     * Gets the Challenge associated with this Round.
     *
     * @return
     */
    public Challenge getChallenge() {
        return challenge;
    }

    /**
     * Sets the challenge associated with this Round.
     *
     * @param challenge
     */
    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    /**
     * Sets the total amount of time for this Round, in seconds.
     *
     * @param time The total amount of time availble for this Round.
     */
    public void setDuration(long time) {
        this.duration = time;
    }

    public long getDuration() {
        return duration;
    }

    /**
     * Gets the position of this round in the competition.
     *
     * @return An integer indicating the position of this round in the
     * competition.
     */
    public int getRoundOrder() {
        return roundOrder;
    }

    /**
     * Sets the position of this Round in the competition.
     *
     * @param roundOrder Indicates the position of this Round in the
     * competition.
     */
    public void setRoundOrder(int roundOrder) {
        this.roundOrder = roundOrder;
    }

    /**
     * Function returns the amount of remaining roundTime.
     *
     * @return the remaining roundTime in seconds
     */
    public long getRemainingTime() {
        //get the Duration between the current and end time, then retrieve it's seconds.
        return Duration.between(Instant.now(), endTime.toInstant()).getSeconds();
    }

    protected long elapsedTime() {
        return Duration.between(startTime.toInstant(), Instant.now()).getSeconds();
    }

    /**
     * Function returns the amount of remaining points. This amount is equal to
     * the amount of roundTime that is left multiplied with the difficulty of
     * the challenge.
     *
     * @return the amount of remaining points for the challenge
     */
    public long getRemainingPoints() {
        return getRemainingTime() * challenge.getDifficulty();
    }

    /**
     * Gets the RoundState of this Round.
     *
     * @return A RoundState object indicating the state of this Round.
     */
    @XmlElement
    public RoundState getRoundState() {
        return this.roundState;
    }

    public void setRoundSate(RoundState state) {
        this.roundState = state;
    }

    public List<TeamScore> getTeamScores() {
        return teamScores;
    }

    public Calendar getStartTime() {
        return startTime;
    }
    
    /**
     * Gets the amount of teams that have submitted. If the amount of teams that
     * have submitted equals the amount of teams in the competition, the round
     * should be over.
     *
     * @return The amount of teams that have submitted
     */
    public int submittedTeamCount() {
        return teamScores.size();
    }
    
    /*public List<Hint> getReleasedHints(){
        return this.releasedHints;
    }*/
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods" >
    // <editor-fold defaultstate="collapsed" desc="roundstates" >
    /**
     * Start the round.
     */
    public void start() {
        System.out.println("Starting round: " + this.id + " - " + this.roundState);
        if (roundState == RoundState.NOT_STARTED) {
            //set the start time, end time and the hints to be released
            startTime = Calendar.getInstance();
            endTime = Calendar.getInstance();
            endTime.add(Calendar.SECOND, (int) duration);
            hintsCopy = challenge.getHintsCopy();
            roundState = RoundState.ONGOING;
        } else {
            throw new IllegalArgumentException("The round has already started.");
        }
    }

    /**
     * Stop the round.
     */
    public void stop() {
        if (roundState == RoundState.NOT_STARTED) {
            throw new IllegalArgumentException("Cannot stop a round that has not been started yet.");
        } else {
            roundState = RoundState.ENDED;
        }
    }

    /**
     * Pause the round: the timer will be stopped but the participants can still
     * continue working on the challenge.
     */
    public void pause() {
        if (roundState != RoundState.ONGOING) {
            throw new IllegalArgumentException("Cannot pause a round which is not ongoing.");
        } else {
            roundState = RoundState.PAUSED;
        }
    }

    /**
     * Freeze the round: the timer will be stopped and the participants won't be
     * able to work on the challenge.
     */
    public void freeze() {
        if (roundState != RoundState.ONGOING) {
            throw new IllegalArgumentException("Cannot freeze a round which is not ongoing.");
        } else {
            roundState = RoundState.FROZEN;
        }
    }

    /**
     * Resume the round after it has been paused or frozen.
     */
    public void resume() {
        if (roundState == RoundState.FROZEN || roundState == RoundState.PAUSED) {
            roundState = RoundState.ONGOING;
        } else {
            throw new IllegalArgumentException("Cannot resume a round which is not frozen or paused.");
        }
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Events" >
    public List<CompetitionEvent> update() {
        List<CompetitionEvent> events = new ArrayList<>();
        if (roundState == RoundState.ONGOING) {
            //stop the round if it ended
            System.out.println("Remaing Time: " + getRemainingTime());

            if (getRemainingTime() <= 0) {
                stop();
                //addNonSubmittedTeams();
                events.add(new RoundEndedEvent(this));
            }

            //loop through hints and release + remove any expired ones
            for (int i = hintsCopy.size() - 1; i >= 0; i--) {
                Hint h = hintsCopy.get(i);
                if (elapsedTime() >= h.getTime()) {
                    events.add(new HintReleasedEvent(h));
                    hintsCopy.remove(i);
                    //releasedHints.add(h);
                }
            }
        }
        return events;
    }

// </editor-fold>
    /**
     * Increase the remaining roundTime of the round with a certain amount of
     * roundTime.
     *
     * @param seconds the amount to increase the remaining roundTime with in
     * seconds
     */
    public void increaseDuration(int seconds) {
        endTime.add(Calendar.SECOND, seconds);
    }

    /**
     * Releases the next hint for this challenge. Returns true if the hint has
     * been released or false if there are no hints left to release. True if the
     * hint has been released, false if there are no hints left to release.
     *
     * @return
     */
    public HintReleasedEvent releaseNextHint() {
        if (hintsCopy.size() > 0) {
            Hint h = hintsCopy.get(0);
            hintsCopy.remove(0);
            return new HintReleasedEvent(h);
        }
        return null;
    }

    /**
     * Adds the specified team to the list of teams that completed this
     * challenge
     *
     * @param toSubmit The team to submit
     */
    public void submit(Team toSubmit) {
        teamScores.add(new TeamScore(toSubmit.getName(), getRemainingPoints()));
    }
    //</editor-fold>
}
