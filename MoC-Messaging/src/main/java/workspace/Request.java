package workspace;

import java.io.Serializable;

/**
 * This class represents an object that tells a specific workspace to perform a
 * specific action
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