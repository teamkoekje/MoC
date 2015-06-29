package workspace.requests;

/**
 * A Request used to send a zip to a Workspace Server in order to extract it to
 * a specified Competition.
 *
 * @author TeamKoekje
 */
public class PushRequest extends Request {

    private final String challengeName;
    private final byte[] data;

    /**
     * Initializes a new instance of the PushRequest class, used to send over a Challenge.
     * @param competitionId The id of the Competition.
     * @param challengeName The name of the Challenge.
     * @param data The data to be pushed, which should be a zip file.
     */
    public PushRequest(long competitionId, String challengeName, byte[] data) {
        super(RequestAction.PUSH_CHALLENGE, competitionId);
        this.challengeName = challengeName;
        this.data = data;
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
     * Gets the data of the challenge that should be pushed.
     * @return A byte[] containing the data to be pushed.
     */
    public byte[] getData() {
        return data;
    }
}
