package workspace.Replies;

import java.io.Serializable;
import workspace.Requests.Action;

/**
 * This is the generic Reply class used to reply to requests on the JMS connections.
 * It simply contains a message (string) representing the reply.
 * @author TeamKoekje
 */
public class Reply implements Serializable {

    private final Action action;
    private final String message;
    
    public Reply(Action action, String message){
        this.action = action;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
    public Action getAction() {
        return action;
    }
}
