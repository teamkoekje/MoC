package messaging;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import java.util.ArrayList;
import java.util.List;
import workspace.replies.Reply;
import workspace.requests.Request;
import workspace.replies.BroadcastReply;

//</editor-fold>
/**
 * Aggregator for the replies of System Information Broadcasts.
 *
 * @author Teamkoekje
 */
public class SysInfoAggregate {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    private final Request request;
    private final List<Reply> replies;
    private final int nrExpectedReplies;
    private final IReplyListener<Request, Reply> replyListener;
    private final String username;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    /**
     * Creates a new instance of the SysInfoAggregate class, which is used to
     * accumulate broadcast replies of system information and eventually relay
     * them to the listener.
     *
     * @param request The original request
     * @param nrExpectedReplies The number of expected replies
     * @param username The name of the user that originally send the request
     * @param listener The handler for when all replies are accumulated.
     */
    public SysInfoAggregate(Request request, int nrExpectedReplies, String username, IReplyListener<Request, Reply> listener) {
        this.request = request;
        this.replies = new ArrayList<>();
        this.nrExpectedReplies = nrExpectedReplies;
        this.replyListener = listener;
        this.username = username;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter & Setters" >
    /**
     * Gets the username associated with this aggregator.
     *
     * @return A String containing the username associated with this aggregator.
     */
    public String getUsername() {
        return username;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter & Setters" >
    /**
     * Adds a reply to the list of replies and if there are as many replies as
     * expected, notifies the listener.
     *
     * @param reply The Reply to add.
     */
    public void addReply(Reply reply) {
        replies.add(reply);
        if (replies.size() == nrExpectedReplies) {
            notifyListener();
        }
    }

    /**
     * Merges all replies and notifies the result to the listener.
     */
    private void notifyListener() {
        if (replyListener != null) {
            String s = "{\"sysinfo\":{";
            int i = 1;
            for (Reply r : replies) {
                if (i < replies.size()) {
                    s += r.getMessage() + ",";
                    i++;
                } else {
                    s += r.getMessage() + "}}";
                }
            }
            BroadcastReply reply = new BroadcastReply(s);
            replyListener.onReply(request, reply);
        }
    }
}
