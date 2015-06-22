package workspace.requests;

/**
 * A Request used to tell a Workspace Server to delete the workspace of a
 * specified Team in a specified Competition.
 *
 * @author TeamKoekje
 */
public class DeleteRequest extends TeamRequest {

    public DeleteRequest(long competitionId, String teamName) {
        super(RequestAction.DELETE, competitionId, teamName);
    }
}
