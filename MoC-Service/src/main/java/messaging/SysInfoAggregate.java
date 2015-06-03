package messaging;

import java.util.ArrayList;
import workspace.Requests.Action;
import workspace.Replies.Reply;
import workspace.Requests.Request;
import workspace.Replies.BroadcastReply;


/**
 * @author Team koekje
 */
public class SysInfoAggregate {

    private Request request;
    private ArrayList<Reply> replies;
    private int nrExpectedReplies;
    private IReplyListener<Request, Reply> replyListener;
    private String username;

    public SysInfoAggregate(Request request, int nrExpectedReplies, String username, IReplyListener<Request, Reply> listener) {
        super();
        this.request = request;
        this.replies = new ArrayList<>();
        this.nrExpectedReplies = nrExpectedReplies;
        this.replyListener = listener;
        this.username = username;
    }

    public void addReply(Reply reply) {
        replies.add(reply);
        if(replies.size() == nrExpectedReplies){
            notifyListener();
        }
    }

    private void notifyListener() {
        if (replyListener != null) {
            String s = "{\"sysinfo\":{";
            int i = 1;
            for (Reply r : replies) {
                if(i < replies.size()){
                    s += r.getMessage() + ",";
                    i++;
                }else{
                    s += r.getMessage() + "}}";
                }
            }
            BroadcastReply reply = new BroadcastReply(s);
            replyListener.onReply(request, reply);
        }
    }

    public String getUsername() {
        return username;
    }
}
