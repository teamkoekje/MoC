package workspace.Requests;

/**
 * A Request used to tell a Workspace Server to update the contents of a
 * specified File in a specified Competition for a specified Team to the
 * specified content.
 *
 * @author TeamKoekje
 */
public class UpdateRequest extends TeamRequest {

    private final String filePath;
    private final String fileContent;

    public UpdateRequest(long competitionId, String teamName, String filePath, String fileContent) {
        super(Action.UPDATE, competitionId, teamName);
        this.filePath = filePath;
        this.fileContent = fileContent;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileContent() {
        return fileContent;
    }
}
