package workspace.Replies;

import workspace.Requests.Action;

public class NormalReply extends Reply {
    
    public NormalReply(String message){
        super(Action.NORMAL, message);
    }
}
