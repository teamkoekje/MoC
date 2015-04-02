package domain;

//@Author Casper
public class NewsItem {

    private final String content;
    private String timestamp;
    
    public NewsItem(String content){
        this.content = content;
    }
    
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }
}
