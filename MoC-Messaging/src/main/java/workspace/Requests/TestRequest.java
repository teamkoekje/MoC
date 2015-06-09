package workspace.Requests;

/**
 * A Request used to tell a Workspace Server to test a specified Test in a
 * specified Competition for a specified Team.
 *
 * @author TeamKoekje
 */
public class TestRequest extends TeamRequest {

    private String challengeName;
    private String testFile;
    private String testName;

    public TestRequest(long competitionId, String teamname, String challengeName, String testFile, String testName) {
        super(RequestAction.TEST, competitionId, teamname);
        this.challengeName = challengeName;
        this.testFile = testFile;
        this.testName = testName;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestFile() {
        return testFile;
    }

    public void setTestFile(String testFile) {
        this.testFile = testFile;
    }
}
