package workspace.requests;

/**
 * A Request used to tell a Workspace Server to compile a project of a specified
 * team, in a specified competition.
 *
 * @author TeamKoekje
 */
public class TestAllRequest extends TeamRequest {

    private final String challengeName;
    private final String filePath;
    private final String fileContent;

    /**
     * Initializes a new instance of the TestAllRequest class, which is used to test all tests.
     *
     * @param competitionId The Id of the competition.
     * @param teamName The name of the Team.
     * @param challengeName The name of the Challenge.
     * @param filePath The path to the file, relative to the project root.
     * @param fileContent The new content of the file.
     */
    public TestAllRequest(long competitionId, String teamName, String challengeName, String filePath, String fileContent) {
        super(RequestAction.TESTALL, competitionId, teamName);
        this.challengeName = challengeName;
        this.filePath = filePath;
        this.fileContent = fileContent;
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
