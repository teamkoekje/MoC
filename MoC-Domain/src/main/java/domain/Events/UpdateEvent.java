package domain.events;

import domain.enums.EventType;

public class UpdateEvent extends CompetitionEvent {

    public UpdateEvent() {
        super(EventType.UPDATE);
    }

}
