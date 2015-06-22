package workspace.requests2;

// <editor-fold defaultstate="collapsed" desc="imports" >
// </editor-fold>

/**
 * TODO: A request for the workspace server to indicate which tests are available for the participant.
 *
 * @author TeamKoekje
 */
public class AvailableTestsRequest extends TeamRequest{

    private final String challengeName;

    public AvailableTestsRequest(long competitionId, String challengeName, String teamName) {
        super(RequestAction.AVAILABLE_TESTS, competitionId, teamName);
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }
}
