package workspace;

public class BroadcastReply extends Reply {
    
    public BroadcastReply(String message){
        super(Action.BROADCAST, message);
    }
}
