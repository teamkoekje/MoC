package domain.events;

import domain.enums.EventType;
import domain.Competition;

public class CompetitionEndedEvent extends CompetitionEvent {

    private  final Competition competition;

    public CompetitionEndedEvent(Competition competition) {
        super(EventType.COMPETITION_ENDED);
        this.competition = competition;
    }

    public Competition getCompetition() {
        return competition;
    }
}
