/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workspace.Requests;

/**
 *
 * @author Luc
 */
public class TestAllRequest extends TeamRequest{
    
    private String challengeName;

    public TestAllRequest(String competition, String teamName, String challengeName) {
        super(Action.TESTALL, competition, teamName);
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }
}
