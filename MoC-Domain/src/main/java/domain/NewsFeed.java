package domain;

//@Author Casper
<<<<<<< HEAD
import domain.Events.NewsItemPublishedEvent;
import domain.Events.NewsItemPublishedListener;
=======

import domain.Events.HintReleasedEvent;
import domain.Events.HintReleasedListener;
import domain.Events.RoundEndedEvent;
>>>>>>> 7f8809cb67711473af09c09a45fc612ae34b720f
import domain.Events.RoundEndedListener;
import java.util.ArrayList;
import java.util.List;

public class NewsFeed implements HintReleasedListener, RoundEndedListener{

<<<<<<< HEAD
    protected EventListenerList newsItemPublishedListenerList;

    //should probably be a singleton
    public NewsFeed() {
        this.newsItemPublishedListenerList = new EventListenerList();
=======
    private List<NewsItem> newsItems;
    
    //should probably be a singleton
    public NewsFeed(){
        this.newsItems = new ArrayList<>();
    }

    public List<NewsItem> getNewsItems() {
        return newsItems;
    }
    
    public void addNewsItem(NewsItem newsItem){
        newsItems.add(newsItem);
>>>>>>> 7f8809cb67711473af09c09a45fc612ae34b720f
    }

    public void publishNewsItem(NewsItem toPublish) {
        toPublish.setTimestamp("current time");
        //throw event
    }
<<<<<<< HEAD

    /**
     * Adds the specified NewsItemPublishedListener to the listener list.
     *
     * @param toAdd The NewsItemPublishedListener to add.
     */
    public void addRoundEndedListener(NewsItemPublishedListener toAdd) {
        newsItemPublishedListenerList.add(NewsItemPublishedListener.class, toAdd);
    }
=======
>>>>>>> 7f8809cb67711473af09c09a45fc612ae34b720f

    @Override
    public void hintReleasedOccurred(HintReleasedEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

<<<<<<< HEAD
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
=======
    @Override
    public void roundEndedOccurred(RoundEndedEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
>>>>>>> 7f8809cb67711473af09c09a45fc612ae34b720f
    }
}
