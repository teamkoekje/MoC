package domain.Events;

import java.util.EventListener;

/**
 *
 * @author Robin
 */
public interface NewsItemPublishedListener extends EventListener{
    public void newsItemPublishedOccurred(NewsItemPublishedEvent event);
}
