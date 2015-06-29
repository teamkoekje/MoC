package workspace.requests;

/**
 * A Request used to tell a Workspace Server to create a Workspace for a
 * specified Team in a specified Competition
 *
 * @author TeamKoekje
 */
public class CreateRequest extends TeamRequest {

    /**
     * Initializes a new instance of CreateRequest, which is used to add a
     * Workspace on a server.
     *
     * @param competitionId The Id of the competition.
     * @param teamName The name of the Team.
     */
    public CreateRequest(long competitionId, String teamName) {
        super(RequestAction.CREATE, competitionId, teamName);
    }
}
