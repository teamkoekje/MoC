package domain.events;

import domain.NewsItem;
import java.util.EventObject;

/**
 *
 * @author TeamKoekje
 */
public class NewsItemPublishedEvent extends EventObject {
    private final NewsItem releasedItem;

    public NewsItemPublishedEvent(Object source, NewsItem releasedItem) {
        super(source);
        this.releasedItem = releasedItem;
    }

    public NewsItem getReleasedNewsItem() {
        return releasedItem;
    }
}
