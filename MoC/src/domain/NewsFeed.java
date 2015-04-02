package domain;

//@Author Casper

import domain.Events.HintReleasedEvent;
import domain.Events.HintReleasedListener;
import domain.Events.RoundEndedEvent;
import domain.Events.RoundEndedListener;
import java.util.ArrayList;
import java.util.List;

public class NewsFeed implements HintReleasedListener, RoundEndedListener{

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
    }
    
    public void publishNewsItem(NewsItem toPublish){
        toPublish.setTimestamp("current time");
        //throw event
    }

    @Override
    public void hintReleasedOccurred(HintReleasedEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void roundEndedOccurred(RoundEndedEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
