package workspace.requests;

// <editor-fold defaultstate="collapsed" desc="imports" >
// </editor-fold>

/**
 * A request for the workspace server to indicate which tests are available for the participant.
 *
 * @author TeamKoekje
 */
public class AvailableTestsRequest extends TeamRequest{

    private final String challengeName;

    /**
     * Initializes a new instance of AvailableTestsRequest, used to get which Tests are available for participants.
     * @param competitionId The Id of the Competition.
     * @param challengeName The name of the Challenge.
     * @param teamName The name of the Team.
     */
    public AvailableTestsRequest(long competitionId, String challengeName, String teamName) {
        super(RequestAction.AVAILABLE_TESTS, competitionId, teamName);
        this.challengeName = challengeName;
    }

    /**
     * Gets the name of the Challenge.
     *
     * @return A String indicating the name of the challenge.
     */
    public String getChallengeName() {
        return challengeName;
    }
}
