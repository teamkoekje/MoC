package workspace.replies;

/**
 * @author TeamKoekje
 */
public class NormalReply extends Reply {
    
    /**
     * Initializes a new NormalReply, which is the standard reply for requests.
     * @param message The content of the message to reply.
     */
    public NormalReply(String message){
        super(ReplyAction.NORMAL, message);
    }    
}
