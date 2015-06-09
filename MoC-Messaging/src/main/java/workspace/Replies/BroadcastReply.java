package workspace.Replies;

import workspace.Requests.RequestAction;

public class BroadcastReply extends Reply {
    
    public BroadcastReply(String message){
        super(ReplyAction.BROADCAST, message);
    }
}
