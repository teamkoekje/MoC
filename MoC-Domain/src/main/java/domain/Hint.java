package domain;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
// </editor-fold>

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
    private long releaseTime;
    
    @ManyToOne
    private Challenge challenge;
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
        this.releaseTime = time;
        this.content = content;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters" >

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
        return releaseTime;
    }

    /**
     * Set the time when the hint should be released.
     *
     * @param time the time in seconds
     */
    public void setTime(long time) {
        this.releaseTime = time;
    }

    //</editor-fold>    
}
