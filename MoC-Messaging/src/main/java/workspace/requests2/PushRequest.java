package workspace.requests2;

/**
 * A Request used to send a zip to a Workspace Server in order to extract it to
 * a specified Competition.
 *
 * @author TeamKoekje
 */
public class PushRequest extends Request {

    private final String challengeName;
    private final byte[] data;

    public PushRequest(long competitionId, String challengeName, byte[] data) {
        super(RequestAction.PUSH_CHALLENGE, competitionId);
        this.challengeName = challengeName;
        this.data = data;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public byte[] getData() {
        return data;
    }
}
