package domain;

//@Author Casper
public class NewsFeed {
    
    //should probably be a singleton
    public NewsFeed(){}
    
    public void publishNewsItem(NewsItem toPublish){
        toPublish.setTimestamp("current time");
        //throw event
    }
}
