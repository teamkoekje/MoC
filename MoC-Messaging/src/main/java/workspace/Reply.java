package workspace;

import java.io.Serializable;

public class Reply implements Serializable {

    private final String message;
    
    public Reply(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
