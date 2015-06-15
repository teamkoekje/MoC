package domain.Events;

import domain.enums.EventType;
import domain.NewsItem;

/**
 * 
 * @author TeamKoekje
 */
public class MessageReleasedEvent extends CompetitionEvent {

    private final NewsItem message;

    public MessageReleasedEvent(NewsItem newsItem) {
        super(EventType.MESSAGE_RELEASED);
        this.message = newsItem;
    }

    public NewsItem getReleasedMessage() {
        return message;
    }
}
