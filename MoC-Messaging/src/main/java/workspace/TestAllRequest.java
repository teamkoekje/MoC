/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workspace;

/**
 *
 * @author Luc
 */
public class TestAllRequest extends TeamRequest{
    
    private String challengeName;

    public TestAllRequest(long competitionId, String teamName, String challengeName) {
        super(Action.TESTALL, competitionId, teamName);
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }
}
