package domain.events;

import java.util.EventListener;

/**
 *
 * @author TeamKoekje
 */
public interface NewsItemPublishedListener extends EventListener{
    public void newsItemPublishedOccurred(NewsItemPublishedEvent event);
}
