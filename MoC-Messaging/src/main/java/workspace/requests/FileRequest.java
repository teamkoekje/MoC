package workspace.requests;

/**
 * A Request used to tell a Workspace Server to retrieve the contents of a
 * specified File from a specified Team in a specified Competition.
 *
 * @author TeamKoekje
 */
public class FileRequest extends TeamRequest {

    private final String filepath;
    private final String challengeName;

    /**
     * Initializes a new instance of FileRequest, used to retrieve the contents of a specified file.
     * @param competitionId The Id of the Competition.
     * @param teamName The name of the Team.
     * @param challengeName The name of the Challenge.
     * @param filepath The path to the file, relative to the project root.
     */
    public FileRequest(long competitionId, String teamName, String challengeName, String filepath) {
        super(RequestAction.FILE, competitionId, teamName);
        this.challengeName = challengeName;
        this.filepath = filepath;
    }

    /**
     * Gets the path to the file, relative to the project root.
     *
     * @return A String indicating the path.
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * Gets the name of the Challenge.
     * @return A String indicating the name of the challenge.
     */
    public String getChallengeName() {
        return challengeName;
    }
}
