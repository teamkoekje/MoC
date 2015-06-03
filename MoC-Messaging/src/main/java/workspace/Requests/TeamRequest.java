package workspace.Requests;

/**
 * Abstract class used to tell a Workspace Server to perform a Specified Action
 * in a specified Competition on a specified Team.
 *
 * @author TeamKoekje
 */
public abstract class TeamRequest extends Request {

    private final String teamName;

    public TeamRequest(Action action, long competitionId, String teamName) {
        super(action, competitionId);
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }
}