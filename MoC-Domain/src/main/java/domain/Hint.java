package domain;

/**
 * The Hint class represents a hint that can be shown during a challenge. It
 * contains a string with the content of the hint and a set time in seconds when
 * the hint should be released.
 */
public class Hint {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    private String content;
    private int time;
    private boolean published;
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    public Hint(String content) {
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

    //</editor-fold>    
}
