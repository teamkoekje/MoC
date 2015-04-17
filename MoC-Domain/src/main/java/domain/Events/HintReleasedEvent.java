package domain.Events;

import domain.Hint;
import java.io.Serializable;

/**
 * 
 * @author TeamKoekje
 */
public class HintReleasedEvent implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="variables">
    private Hint releasedHint;
    //</code-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor(s)">
    public HintReleasedEvent() {
    }

    public HintReleasedEvent(Hint releasedHint) {
        this.releasedHint = releasedHint;
    }
    //</code-fold>

    //<editor-fold defaultstate="collapsed" desc="getters & setters">
    public Hint getReleasedHint() {
        return releasedHint;
    }

    public void setReleasedHint(Hint releasedHint) {
        this.releasedHint = releasedHint;
    }
    //</code-fold>

}
