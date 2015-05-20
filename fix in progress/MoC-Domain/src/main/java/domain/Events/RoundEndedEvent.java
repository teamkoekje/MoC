package domain.Events;

import domain.Round;
import java.io.Serializable;

/**
 *
 * @author TeamKoekje
 */
public class RoundEndedEvent implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="variables">
    private Round endedRound;
    //</code-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor(s)">
    public RoundEndedEvent() {
    }

    public RoundEndedEvent(Round endedRound) {
        this.endedRound = endedRound;
    }
    //</code-fold>

    //<editor-fold defaultstate="collapsed" desc="getters & setters">
    public Round getEndedRound() {
        return endedRound;
    }

    public void setEndedRound(Round endedRound) {
        this.endedRound = endedRound;
    }
    //</code-fold>

}
