/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 * The Round class represents a round within a Masters of Code competition. A
 * round contains a challenge and a time that represents how much time there is
 * left before the round is over.
 *
 * @author Astrid Belder
 */
public class Round {

    private Challenge challenge;
    private int time;

    public Round() {

    }

    /**
     * Start the round.
     */
    public void start() {

    }

    /**
     * Stop the round.
     */
    public void stop() {

    }

    /**
     * Pause the round: the timer will be stopped but the participants can still
     * continue working on the challenge.
     */
    public void pause() {

    }

    /**
     * Freeze the round: the timer will be stopped and the participants won't be
     * able to work on the challenge.
     */
    public void freeze() {

    }

    /**
     * Resume the round after it has been paused or frozen.
     */
    public void resume() {

    }

    /**
     * Increase the remaining time of the round with a certain amount of time.
     *
     * @param seconds the amount to increase the remaing time with in seconds
     */
    public void increaseTime(int seconds) {

    }

    /**
     * Function returns the amount of remaining time.
     *
     * @return the remaining time in seconds
     */
    public int getRemainingTime() {
        return 0;
    }

    /**
     * Function returns the amount of remaining points. This amount is equal to
     * the amount of time that is left multiplied with the difficulty of the
     * challenge.
     *
     * @return the amount of remaining points for the challenge
     */
    public int getRemainingPoints() {
        return 0;
    }

    /**
     * Function returns the next hint in the list of hints from the challenge,
     * that hasn't been given out yet.
     *
     * @return the first hint of the challenge that hasn't been given yet
     */
    public Hint giveNextHint() {
        return null;
    }

}
