package workspace.Requests;

/** 
 * A Request used to tell a Workspace Server to compile a project of a specified
 * team, in a specified competition.
 *
 * @author TeamKoekje
 */
public class TestAllRequest extends TeamRequest{
    
    private final String challengeName;

    public TestAllRequest(long competitionId, String teamName, String challengeName) {
        super(Action.TESTALL, competitionId, teamName);
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }
}
