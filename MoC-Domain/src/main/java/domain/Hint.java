package domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * The Hint class represents a hint that can be shown during a challenge. It
 * contains a string with the content of the hint and a set time in seconds when
 * the hint should be released.
 *
 * @author TeamKoekje
 */
@Entity
public class Hint implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    @Id
    @GeneratedValue
    private long id;

    private String content;
    private long time;
    private boolean published;
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    protected Hint() {
    }

    /**
     * Create a new hint
     *
     * @param content the content of the hint
     * @param time the time when the hint will be released after the rounds has
     * started in seconds
     */
    public Hint(String content, long time) {
        if (content == null) {
            throw new IllegalArgumentException("Content can't be null");
        }
        if (time <= 0) {
            throw new IllegalArgumentException("Time must be positive");
        }
        this.time = time;
        this.content = content;
        this.published = false;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters" >
    /**
     * Gets whether this Hint is published or not.
     *
     * @return
     */
    public boolean isPublished() {
        return published;
    }

    /**
     * Sets whether this Hint has been published or not
     *
     * @param published Whether the boolean has been published or not.
     */
    public void setPublished(boolean published) {
        this.published = published;
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
    public long getTime() {
        return time;
    }

    /**
     * Set the time when the hint should be released.
     *
     * @param time the time in seconds
     */
    public void setTime(long time) {
        this.time = time;
    }

    //</editor-fold>    
}
