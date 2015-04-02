package domain;

//@Author Casper
import domain.Events.NewsItemPublishedEvent;
import domain.Events.NewsItemPublishedListener;
import domain.Events.RoundEndedListener;
import javax.swing.event.EventListenerList;

public class NewsFeed {

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
     * Removes the specified NewsItemPublishedListener from the listener list.
     *
     * @param toRemove The NewsItemPublishedListener to remove.
     */
    public void removeRoundEndedListener(NewsItemPublishedListener toRemove) {
        newsItemPublishedListenerList.remove(NewsItemPublishedListener.class, toRemove);
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
}
