package workspace.requests;

/**
 * A Request used to tell a Workspace Server to delete the workspace of a
 * specified Team in a specified Competition.
 *
 * @author TeamKoekje
 */
public class DeleteRequest extends TeamRequest {

    /**
     * Initializes a new instance of DeleteRequest, which is used to remove a Workspace on a server.
     * @param competitionId The Id of the competition.
     * @param teamName The name of the Team.
     */
    public DeleteRequest(long competitionId, String teamName) {
        super(RequestAction.DELETE, competitionId, teamName);
    }
}
