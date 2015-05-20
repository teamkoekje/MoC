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

    public TestAllRequest(String competition, String teamname, String challengeName) {
        super(Action.TESTALL, competition, teamname);
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challangeName) {
        this.challengeName = challangeName;
    }
    
    
}
