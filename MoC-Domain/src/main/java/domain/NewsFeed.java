package domain;

//@Author Casper
import domain.Events.NewsItemPublishedEvent;
import domain.Events.NewsItemPublishedListener;

import domain.Events.HintReleasedEvent;
import domain.Events.HintReleasedListener;
import domain.Events.RoundEndedEvent;
import domain.Events.RoundEndedListener;
import java.io.Serializable;
import javax.swing.event.EventListenerList;

public class NewsFeed implements HintReleasedListener, RoundEndedListener, Serializable {

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

    @Override
    public void hintReleasedOccurred(HintReleasedEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public void roundEndedOccurred(RoundEndedEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
