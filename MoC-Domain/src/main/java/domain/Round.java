package domain;

import domain.Events.CompetitionEvent;
import domain.Events.HintReleasedEvent;
import domain.Events.RoundEndedEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

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

    @OneToOne
    private Challenge challenge;
    private long totalRoundTime;
    private long currentTime;
    private Set<Team> submittedTeams;
    private int roundOrder;
    private RoundState roundState;

    @Inject
    @Any
    private Event<RoundEndedEvent> endedEvent;
    //to fire: endedEvent.fire(new RoundEndedEvent(this));
    //to listen to it, define a method in a listener class:
    //  public void endedHandler(@Observes RoundEndedEvent event){/* do stuff */}
    @Transient
    private Event<HintReleasedEvent> hintReleasedEvent;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    public Round() {
        init();
        challenge = null;
        totalRoundTime = 0;
    }

    public Round(Challenge challenge, long roundTime) {
        if (challenge == null) {
            throw new IllegalArgumentException("Challenge can't be null");
        }
        if (roundTime <= 0) {
            throw new IllegalArgumentException("RoundTime must be positive");
        }
        init();
        this.challenge = challenge;
        this.totalRoundTime = roundTime;
    }

    private void init() {
        this.submittedTeams = new HashSet<>();
        this.roundState = RoundState.NOT_STARTED;
        this.currentTime = 0;
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
     * Gets the total amount of roundTime for this round, in seconds.
     *
     * @return An integer indicating the total time for this Round.
     */
    public long getTotalTime() {
        return totalRoundTime;
    }

    /**
     * Sets the total amount of time for this Round, in seconds.
     *
     * @param time The total amount of time availble for this Round.
     */
    public void setTime(long time) {
        this.totalRoundTime = time;
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
        return totalRoundTime - currentTime;
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
    public RoundState getRoundState() {
        return roundState;
    }

    /**
     * Sets the roundState to the specified state
     *
     * @param roundState The new state of the round.
     */
    protected void setRoundState(RoundState roundState) {
        this.roundState = roundState;
    }

    /**
     * Gets all submitted teams
     *
     * @return Set of teams
     */
    public Set<Team> getSubmittedTeams() {
        return submittedTeams;
    }

    /**
     * Gets the amount of teams that have submitted. If the amount of teams that
     * have submitted equals the amount of teams in the competition, the round
     * should be over.
     *
     * @return The amount of teams that have submitted
     */
    public int submittedTeamCount() {
        return submittedTeams.size();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods" >
    // <editor-fold defaultstate="collapsed" desc="roundstates" >
    /**
     * Start the round.
     */
    public void start() {
        if (roundState == RoundState.NOT_STARTED) {
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
            currentTime++;
            if (currentTime >= totalRoundTime) {
                stop();
                events.add(new RoundEndedEvent(this));
            }
            Iterator<Hint> hintsIterator = challenge.hintsIterator();
            while (hintsIterator.hasNext()) {
                Hint h = hintsIterator.next();
                if (!h.isPublished()) {
                    if (currentTime >= h.getTime()) {
                        events.add(new HintReleasedEvent(h));
                        h.setPublished(true);
                    }
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
    public void increaseTime(int seconds) {
        totalRoundTime += seconds;
    }

    /**
     * Releases the next hint for this challenge. Returns true if the hint has
     * been released or false if there are no hints left to release. True if the
     * hint has been released, false if there are no hints left to release.
     *
     * @return
     */
    public boolean releaseNextHint() {
        Iterator<Hint> hintsIterator = challenge.hintsIterator();
        while (hintsIterator.hasNext()) {
            Hint h = hintsIterator.next();
            if (!h.isPublished()) {
                hintReleasedEvent.fire(new HintReleasedEvent(h));
                h.setPublished(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the specified team to the list of teams that completed this
     * challenge
     *
     * @param toSubmit The team to submit
     */
    public void submit(Team toSubmit) {
        submittedTeams.add(toSubmit);
    }
    //</editor-fold>
}
