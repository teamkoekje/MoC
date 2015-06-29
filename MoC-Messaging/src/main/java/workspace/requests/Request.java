package workspace.requests;

import java.io.Serializable;

/**
 * Base class used to tell a Workspace Server to perform a specified Action.
 *
 * @author TeamKoekje
 */
public abstract class Request implements Serializable {

    private final RequestAction action;
    private long competitionId;

    /**
     * Initializes a new instance of Request, with the specified action and competition id.
     * @param action The action the Request is for.
     * @param competitionId The Id of the competition.
     */
    protected Request(RequestAction action, long competitionId) {
        this.action = action;
        this.competitionId = competitionId;
    }
    
    /**
     * Initializes a new instance of Request, with the specified action.
     * @param action The action the Request is for.
     */
    protected Request(RequestAction action){
        this.action = action;
    }
    
    /**
     * Gets the RequestAction, which is used to identify what the Request should do.
     * @return The RequestAction.
     */
    public RequestAction getAction() {
        return action;
    }

    /**
     * Gets the Id of the competition
     * @return A long indicating the Id of the competition
     */
    public long getCompetitionId() {
        return competitionId;
    }
}