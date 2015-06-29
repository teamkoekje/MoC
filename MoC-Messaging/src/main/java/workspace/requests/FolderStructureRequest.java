package workspace.requests;

/**
 * A Request used to tell a Workspace Server to retrieve the folder structure of
 * a specified Project for a specified Team in a specified Competition.
 *
 * @author TeamKoekje
 */
public class FolderStructureRequest extends TeamRequest {

    private final String challengeName;

    /**
     * Initializes a new FolderStructureRequest, used to retrieve the folder structure from a server.
     * @param competitionId The Id of the Competition.
     * @param challengeName The name of Challenge.
     * @param teamName The name of the Team.
     */
    public FolderStructureRequest(long competitionId, String challengeName, String teamName) {
        super(RequestAction.FOLDER_STRUCTURE, competitionId, teamName);
        this.challengeName = challengeName;
    }

    /**
     * Gets the name of the Challenge.
     *
     * @return A String indicating the name of the challenge.
     */
    public String getChallengeName() {
        return challengeName;
    }
}
