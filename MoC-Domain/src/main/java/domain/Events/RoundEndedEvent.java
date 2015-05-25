package domain.Events;

import domain.Round;

/**
 *
 * @author TeamKoekje
 */
public class RoundEndedEvent extends CompetitionEvent {

    private final Round round;

    public RoundEndedEvent(Round round) {
        super(EventType.ROUND_ENDED);
        this.round = round;
    }

    public Round getEndedRound() {
        return round;
    }

}
