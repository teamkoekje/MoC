package workspace.Replies;

import workspace.Requests.Action;

public class BroadcastReply extends Reply {
    
    public BroadcastReply(String message){
        super(Action.BROADCAST, message);
    }
}
