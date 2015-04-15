package workspace;

import java.io.Serializable;

/**
 * This is the generic Reply class used to reply to requests on the JMS connections.
 * It simply contains a message (string) representing the reply.
 * @author TeamKoekje
 */
public class Reply implements Serializable {

    private final String message;
    
    public Reply(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
