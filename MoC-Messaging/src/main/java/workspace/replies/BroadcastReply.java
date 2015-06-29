package workspace.replies;

/**
 * 
 * @author TeamKoekje
 */
public class BroadcastReply extends Reply {
    
    /**
     * Initializes a new instance of BroadCastReply, which is a Reply that should be used to respond to Broadcast Requests.
     * @param message The content of the message to reply.
     */
    public BroadcastReply(String message){
        super(ReplyAction.BROADCAST, message);
    }
}
