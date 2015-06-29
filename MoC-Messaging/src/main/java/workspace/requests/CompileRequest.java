package workspace.requests;

/**
 * A Request used to tell a Workspace Server to compile a project of a specified
 * team, in a specified competition.
 *
 * @author TeamKoekje
 */
public class CompileRequest extends TeamRequest {

    private final String challengeName;
    private final String filePath;
    private final String fileContent;
    private final boolean submitRequest;

    /**
     * Initializes a new instance of CompileRequest, used to compile a specified
     * project on a server.
     *
     * @param competitionId The Id of the Competition.
     * @param teamName The name of the Team.
     * @param challengeName The name of the Challenge.
     * @param filePath The path to the file, relative to the project root.
     * @param fileContent The new contents of the file.
     * @param submitRequest Whether this CompileRequest is used for a Team to
     * submit or not.
     */
    public CompileRequest(long competitionId, String teamName, String challengeName, String filePath, String fileContent, boolean submitRequest) {
        super(RequestAction.COMPILE, competitionId, teamName);
        this.challengeName = challengeName;
        this.filePath = filePath;
        this.fileContent = fileContent;
        this.submitRequest = submitRequest;
    }

    /**
     * Gets whether this CompileRequest is used to submit a Team.
     *
     * @return True/False
     */
    public boolean getSubmitRequest() {
        return submitRequest;
    }

    /**
     * Gets the name of the Challenge.
     *
     * @return A String indicating the name of the challenge.
     */
    public String getChallengeName() {
        return challengeName;
    }

    /**
     * Gets the path to the file, relative to the project root.
     *
     * @return A String indicating the path.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Gets the new content of the file.
     *
     * @return A String indicating the file content.
     */
    public String getFileContent() {
        return fileContent;
    }
}
