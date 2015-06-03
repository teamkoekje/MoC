package workspace.Requests;

/**
 * A Request used to tell a Workspace Server to retrieve the folder structure of
 * a specified Project for a specified Team in a specified Competition.
 *
 * @author TeamKoekje
 */
public class FolderStructureRequest extends TeamRequest {

    private final String challengeName;

    public FolderStructureRequest(long competitionId, String challengeName, String teamName) {
        super(Action.FOLDER_STRUCTURE, competitionId, teamName);
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }
}
