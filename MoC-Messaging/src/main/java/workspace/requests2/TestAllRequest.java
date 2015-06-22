package workspace.requests2;

/** 
 * A Request used to tell a Workspace Server to compile a project of a specified
 * team, in a specified competition.
 *
 * @author TeamKoekje
 */
public class TestAllRequest extends TeamRequest{
    
    private final String challengeName;
    private final String filePath;
    private final String fileContent;

    public TestAllRequest(long competitionId, String teamName, String challengeName, String filePath, String fileContent) {
        super(RequestAction.TESTALL, competitionId, teamName);
        this.challengeName = challengeName;
        this.filePath = filePath;
        this.fileContent = fileContent;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileContent() {
        return fileContent;
    }
}
