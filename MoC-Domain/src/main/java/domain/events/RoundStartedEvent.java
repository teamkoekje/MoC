package domain.events;

import domain.enums.EventType;
import domain.Competition;
import domain.Round;

/**
 *
 * @author TeamKoekje
 */
public class RoundStartedEvent extends CompetitionEvent {

    private Competition competition;
    private final Round round;

    public RoundStartedEvent(Round round) {
        super(EventType.ROUND_STARTED);
        this.round = round;
    }

    public Round getStartedRound() {
        return round;
    }
    
    public Competition getCompetition(){
        return competition;
    }
    
    public void setCompetition(Competition competition){
        this.competition = competition;
    }
}
