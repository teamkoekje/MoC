package workspace.requests;

/**
 * A Request used to tell a Workspace Server to test a specified Test in a
 * specified Competition for a specified Team.
 *
 * @author TeamKoekje
 */
public class TestRequest extends TeamRequest {

    private final String challengeName;
    private final String testName;
    private final String filePath;
    private final String fileContent;
       
    /**
     * Initializes a new instance of the TestRequest class, which is used to test a specific test.
     * @param competitionId The Id of the competition.
     * @param teamName The name of the Team.
     * @param challengeName The name of the Challenge.
     * @param testName The name of the Test.
     * @param filePath The path to the file, relative to the project root.
     * @param fileContent The new content of the file.
     */
    public TestRequest(long competitionId, String teamName, String challengeName, String testName, String filePath, String fileContent) {
        super(RequestAction.TEST, competitionId, teamName);
        this.challengeName = challengeName;
        this.testName = testName;
        this.filePath = filePath;
        this.fileContent = fileContent;
    }

    /**
     * Gets the name of the Challenge.
     * @return A String indicating the name of the challenge.
     */
    public String getChallengeName() {
        return challengeName;
    }

    /**
     * Gets the name of the Test to run.
     * @return A String indicating the name of the Test to run.
     */
    public String getTestName() {
        return testName;
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
