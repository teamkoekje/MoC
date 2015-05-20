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
    private String competition;

    public Request(Action action, String competition) {
        this.action = action;
        this.competition = competition;
    }

    public Action getAction() {
        return action;
    }

    public String getCompetition() {
        return competition;
    }
}