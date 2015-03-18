package domain;

//@Author Casper
public class NewsItem {

    private String content;
    private String timestamp;
    
    public NewsItem(String content){
        this.content = content;
    }
    
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
}
