package domain;

import domain.Events.HintReleasedEvent;
import domain.Events.HintReleasedListener;
import domain.Events.RoundEndedEvent;
import domain.Events.RoundEndedListener;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.swing.event.EventListenerList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * The Round class represents a round within a Masters of Code competition. A
 * round contains a challenge and a totalTime that represents how much totalTime
 * there is left before the round is over.
 *
 * @author Astrid Belder
 */
public class Round {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    @Id
    @GeneratedValue
    private long id;

    private Challenge challenge;
    private int totalTime;
    private int currentTime;
    private Set<Team> submittedTeams;
    private int roundOrder;
    private RoundState roundState;

    protected EventListenerList roundEndedListenerList;
    protected EventListenerList hintReleasedListenerList;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    public Round() {
        this.submittedTeams = new HashSet<>();
        this.roundState = RoundState.NOT_STARTED;
        this.currentTime = 0;
        this.roundEndedListenerList = new EventListenerList();
        this.hintReleasedListenerList = new EventListenerList();
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
     * Gets the total amount of totalTime for this round, in seconds.
     *
     * @return An integer indicating the total time for this Round.
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * Sets the total amount of time for this Round, in seconds.
     *
     * @param time The total amount of time availble for this Round.
     */
    public void setTime(int time) {
        this.totalTime = time;
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
     * Function returns the amount of remaining totalTime.
     *
     * @return the remaining totalTime in seconds
     */
    public int getRemainingTime() {
        return totalTime - currentTime;
    }

    /**
     * Function returns the amount of remaining points. This amount is equal to
     * the amount of totalTime that is left multiplied with the difficulty of
     * the challenge.
     *
     * @return the amount of remaining points for the challenge
     */
    public int getRemainingPoints() {
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
    private void setRoundState(RoundState roundState) {
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
    /**
     * Adds the specified RoundEndedListener to the listener list.
     *
     * @param toAdd The RoundEndedListener to add.
     */
    public void addRoundEndedListener(RoundEndedListener toAdd) {
        roundEndedListenerList.add(RoundEndedListener.class, toAdd);
    }

    /**
     * Removes the specified RoundEndedListener from the listener list.
     *
     * @param toRemove The RoundEndedListener to remove.
     */
    public void removeRoundEndedListener(RoundEndedListener toRemove) {
        roundEndedListenerList.remove(RoundEndedListener.class, toRemove);
    }

    /**
     * Fires the round ended event.
     *
     * @param toFire The event to fire.
     */
    private void fireRoundEndedEvent(RoundEndedEvent toFire) {
        Object[] listeners = roundEndedListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == RoundEndedListener.class) {
                ((RoundEndedListener) listeners[i + 1]).roundEndedOccurred(toFire);
            }
        }
    }

    /**
     * Fires the hint released event.
     *
     * @param toFire The event to fire.
     */
    private void fireHintReleasedEvent(HintReleasedEvent toFire) {
        Object[] listeners = hintReleasedListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == HintReleasedListener.class) {
                ((HintReleasedListener) listeners[i + 1]).hintReleasedOccurred(toFire);
            }
        }
    }

    /**
     * Adds the specified HintReleasedListener to the listener list.
     *
     * @param toAdd The HintReleasedListener to add.
     */
    public void addHintReleasedListener(HintReleasedListener toAdd) {
        hintReleasedListenerList.add(HintReleasedListener.class, toAdd);
    }

    /**
     * Removes the specified HintReleasedListener from the listener list.
     *
     * @param toRemove The HintReleasedListener to remove.
     */
    public void removeHintReleasedListener(HintReleasedListener toRemove) {
        hintReleasedListenerList.remove(HintReleasedListener.class, toRemove);
    }

    // </editor-fold>
    /**
     * Increase the remaining totalTime of the round with a certain amount of
     * totalTime.
     *
     * @param seconds the amount to increase the remaining totalTime with in
     * seconds
     */
    public void increaseTime(int seconds) {
        totalTime += seconds;
    }

    /**
     * Function returns the next hint in the list of hints from the challenge,
     * that hasn't been given out yet.
     *
     */
    public void releaseNextHint() {
        throw new NotImplementedException();
    }

    /**
     * Adds the specified team to the list of teams that completed this challe
     *
     * @param toSubmit The team to submit
     */
    public void submit(Team toSubmit) {
        submittedTeams.add(toSubmit);
    }
    //</editor-fold>
}
