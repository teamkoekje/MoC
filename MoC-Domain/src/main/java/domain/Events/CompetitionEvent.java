package domain.Events;



public class CompetitionEvent {

    private final EventType type;

    protected CompetitionEvent(EventType type) {
        this.type = type;
    }
        
    public EventType getType(){
        return type;
    }
}
