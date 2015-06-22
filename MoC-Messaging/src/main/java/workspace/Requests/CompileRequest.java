package workspace.Requests;

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

    public CompileRequest(long competitionId, String teamName, String challengeName, String filePath, String fileContent, boolean submitRequest) {
        super(RequestAction.COMPILE, competitionId, teamName);
        this.challengeName = challengeName;
        this.filePath = filePath;
        this.fileContent = fileContent;
        this.submitRequest = submitRequest;
    }
    
    public boolean isSubmitRequest(){
        return submitRequest;
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
