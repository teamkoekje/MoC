package workspace.replies;

import java.io.Serializable;

/**
 * This is the generic Reply class used to reply to requests on the JMS
 * connections. It simply contains a message (string) representing the reply.
 *
 * @author TeamKoekje
 */
public class Reply implements Serializable {

    private ReplyAction action;
    private final String message;

    /**
     * Initializes a new instance of Reply.
     *
     * @param action The type of the action, indicating what kind of Reply it
     * is.
     * @param message The message associated with the Reply.
     */
    public Reply(ReplyAction action, String message) {
        this.action = action;
        this.message = message;
    }

    /**
     * Gets the message of the Reply.
     *
     * @return A String containing the Message of the Reply.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the ReplyAction of this Reply, used to indicate what kind of Request
     * this Reply actually replies to.
     *
     * @return The ReplyAction.
     */
    public ReplyAction getAction() {
        return action;
    }

    /**
     * Sets the ReplyAction.
     *
     * @param toSet The new value.
     */
    public void setReplyAction(ReplyAction toSet) {
        this.action = toSet;
    }
}
