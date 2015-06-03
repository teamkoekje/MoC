package domain.Events;

import domain.Competition;
import domain.Round;

/**
 *
 * @author TeamKoekje
 */
public class RoundEndedEvent extends CompetitionEvent {

    private Competition competition;
    private final Round round;

    public RoundEndedEvent(Round round) {
        super(EventType.ROUND_ENDED);
        this.round = round;
    }

    public Round getEndedRound() {
        return round;
    }
    
    public Competition getCompetition(){
        return competition;
    }
    
    public void setCompetition(Competition competition){
        this.competition = competition;
    }

}
