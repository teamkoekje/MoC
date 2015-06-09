package workspace.Replies;

import workspace.Requests.RequestAction;

public class NormalReply extends Reply {
    
    public NormalReply(String message){
        super(ReplyAction.NORMAL, message);
    }
}
