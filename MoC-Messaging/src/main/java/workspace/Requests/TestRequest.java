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

    public TestRequest(long competitionId, String teamname, String challengeName, String testName, String filePath, String fileContent) {
        super(RequestAction.TEST, competitionId, teamname);
        this.challengeName = challengeName;
        this.testName = testName;
        this.filePath = filePath;
        this.fileContent = fileContent;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public String getTestName() {
        return testName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileContent() {
        return fileContent;
    }
}
