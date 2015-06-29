package workspace.requests;

/**
 * Abstract class used to tell a Workspace Server to perform a Specified Action
 * in a specified Competition on a specified Team.
 *
 * @author TeamKoekje
 */
public abstract class TeamRequest extends Request {

    private final String teamName;

    /**
     * Initializes a new instance of the TeamRequest class.
     * @param action The type of RequestAction.
     * @param competitionId The Id of the competition.
     * @param teamName The name of the competition.
     */
    public TeamRequest(RequestAction action, long competitionId, String teamName) {
        super(action, competitionId);
        this.teamName = teamName;
    }
    
    /**
     * Gets the name of the Team.
     * @return A String indicating the name of the Team.
     */
    public String getTeamName() {
        return teamName;
    }
}
