package messaging;

import java.util.ArrayList;
import workspace.Action;
import workspace.Reply;
import workspace.Request;
import workspace.BroadcastReply;


/**
 * @author Team koekje
 */
public class SysInfoAggregate {

    private Request request;
    private ArrayList<Reply> replies;
    private int nrExpectedReplies;
    private Reply bestReply = null;
    private IReplyListener<Request, Reply> replyListener;

    public SysInfoAggregate(Request request, int nrExpectedReplies, IReplyListener<Request, Reply> listener) {
        super();
        this.request = request;
        this.replies = new ArrayList<>();
        this.nrExpectedReplies = nrExpectedReplies;
        this.replyListener = listener;
    }

    public void addReply(Reply reply) {
        replies.add(reply);
        if(replies.size() == nrExpectedReplies){
            notifyListener();
        }
    }

    private void notifyListener() {
        if (replyListener != null) {
            String s = "";
            for (Reply r : replies) {
                s += r.getMessage();
            }
            BroadcastReply reply = new BroadcastReply(s);
            replyListener.onReply(request, reply);
        }
    }
}
