package workspace.requests2;

/**
 * A Request used to tell a Workspace Server to retrieve the contents of a
 * specified File from a specified Team in a specified Competition.
 *
 * @author TeamKoekje
 */
public class FileRequest extends TeamRequest {

    private String filepath;
    private String challengeName;

    public FileRequest(long competitionId, String teamname, String challengeName, String filepath) {
        super(RequestAction.FILE, competitionId, teamname);
        this.challengeName = challengeName;
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }
}
