package domain.Events;

//@Author Casper
import domain.Round;
import java.util.EventObject;

public class RoundEndedEvent extends EventObject {

    private final Round endedRound;

    public RoundEndedEvent(Object source, Round endedRound) {
        super(source);
        this.endedRound = endedRound;
    }

    public Round getEndedRound() {
        return endedRound;
    }

}
