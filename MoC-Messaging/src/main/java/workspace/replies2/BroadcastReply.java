package workspace.replies2;

public class BroadcastReply extends Reply {
    
    public BroadcastReply(String message){
        super(ReplyAction.BROADCAST, message);
    }
}
