package domain.Events;

import domain.Hint;

/**
 * 
 * @author TeamKoekje
 */
public class HintReleasedEvent extends CompetitionEvent {

    private final Hint hint;

    public HintReleasedEvent(Hint hint) {
        super(EventType.HINT_RELEASED);
        this.hint = hint;
    }

    public Hint getReleasedHint() {
        return hint;
    }
}
