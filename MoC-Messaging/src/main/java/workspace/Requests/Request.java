package workspace.Requests;

import java.io.Serializable;

/**
 * Base class used to tell a Workspace Server to perform a specified Action.
 *
 * @author TeamKoekje
 */
public abstract class Request implements Serializable {

    private final Action action;
    private long competitionId;

    protected Request(Action action, long competitionId) {
        this.action = action;
        this.competitionId = competitionId;
    }
    
    protected Request(Action action){
        this.action = action;
    }
    
    public Action getAction() {
        return action;
    }

    public long getCompetitionId() {
        return competitionId;
    }
}