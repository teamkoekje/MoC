/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 * The Hint class represents a hint that can be shown during a challenge. It
 * contains a string with the content of the hint and a set time in seconds when the hint
 * should be released.
 *
 * @author Astrid Belder
 */
public class Hint {

    private String content;
    private int time;

    public Hint(String content) {
        this.content = content;
    }

    /**
     * Function returns the content of the hint.
     *
     * @return content of the hint
     */
    public String getContent() {
        return content;
    }

    /**
     * Get the time when the hint should be released.
     *
     * @return time in seconds
     */
    public int getTime() {
        return time;
    }

    /**
     * Set the time when the hint should be released.
     *
     * @param time the time in seconds
     */
    public void setTime(int time) {
        this.time = time;
    }

}
