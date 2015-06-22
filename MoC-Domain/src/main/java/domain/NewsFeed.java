package domain;

import domain.events.NewsItemPublishedEvent;
import domain.events.NewsItemPublishedListener;
import domain.events.HintReleasedEvent;
import domain.events.RoundEndedEvent;
import java.io.Serializable;
import javax.enterprise.event.Observes;
import javax.swing.event.EventListenerList;

/**
 * The Newsfeed listens to events within competitions and forwards them to the
 * appropriate clients
 *
 * @author TeamKoekje
 */
public class NewsFeed implements Serializable {

    protected EventListenerList newsItemPublishedListenerList;

    //should probably be a singleton
    public NewsFeed() {
        this.newsItemPublishedListenerList = new EventListenerList();
    }

    public void publishNewsItem(NewsItem toPublish) {
        toPublish.setTimestamp("current time");
        //throw event
    }

    /**
     * Adds the specified NewsItemPublishedListener to the listener list.
     *
     * @param toAdd The NewsItemPublishedListener to add.
     */
    public void addRoundEndedListener(NewsItemPublishedListener toAdd) {
        newsItemPublishedListenerList.add(NewsItemPublishedListener.class, toAdd);
    }

    /**
     * Fires the news item published event.
     *
     * @param toFire The event to fire.
     */
    private void fireRoundEndedEvent(NewsItemPublishedEvent toFire) {
        Object[] listeners = newsItemPublishedListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == NewsItemPublishedListener.class) {
                ((NewsItemPublishedListener) listeners[i + 1]).newsItemPublishedOccurred(toFire);
            }
        }
    }

    public void hintReleased(@Observes HintReleasedEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void roundEnded(@Observes RoundEndedEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
