package domain.Events;

/**
 * Base class for events that occur within the domain. has an EventType to
 * indicate which type of event it is, so that it can easily be casted to the
 * appropriate subclass.
 *
 * @author TeamKoekje
 */
public abstract class CompetitionEvent {

    private final EventType type;

    protected CompetitionEvent(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
