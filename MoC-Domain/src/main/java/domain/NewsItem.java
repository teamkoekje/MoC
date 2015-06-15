package domain;

/**
 * Generic class that is send out to clients listening to the Newsfeed.
 * 
 * @author TeamKoekje
 */
public class NewsItem {

    private final String content;
    private String timestamp;

    public NewsItem(String content) {
        this.content = content;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getTimestamp(){
        return this.timestamp;
    }

    public String getContent() {
        return content;
    }
}
