package workspace.Requests;

/**
 * A Request used to tell a Workspace Server to create a Workspace for a
 * specified Team in a specified Competition
 *
 * @author TeamKoekje
 */
public class CreateRequest extends TeamRequest {

    public CreateRequest(long competitionId, String teamName) {
        super(Action.CREATE, competitionId, teamName);
    }
}
