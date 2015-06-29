package workspace.requests;

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

    /**
     * Initializes a new instance of the UpdateRequest class, used to update the contents of a file.
     * @param competitionId The Id of the competition
     * @param teamName The name of the Team
     * @param filePath The path to the file, relative to the project root
     * @param fileContent The new content of the file
     */
    public UpdateRequest(long competitionId, String teamName, String filePath, String fileContent) {
        super(RequestAction.UPDATE, competitionId, teamName);
        this.filePath = filePath;
        this.fileContent = fileContent;
    }

    /**
     * Gets the path to the file, relative to the project root.
     * @return A String indicating the path.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Gets the new content of the file.
     * @return A String indicating the file content.
     */
    public String getFileContent() {
        return fileContent;
    }
}
