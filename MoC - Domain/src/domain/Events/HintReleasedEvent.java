package domain.Events;

import domain.Hint;
import java.util.EventObject;

//@Author Casper
public class HintReleasedEvent extends EventObject {

    private final Hint releasedHint;

    public HintReleasedEvent(Object source, Hint releasedHint) {
        super(source);
        this.releasedHint = releasedHint;
    }

    public Hint getReleasedHint() {
        return releasedHint;
    }

}
