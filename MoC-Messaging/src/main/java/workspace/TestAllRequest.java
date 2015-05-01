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
    
    private String challangeName;

    public TestAllRequest(String competition, String teamname, String challangeName) {
        super(Action.TESTALL, competition, teamname);
        this.challangeName = challangeName;
    }

    public String getChallengeName() {
        return challangeName;
    }

    public void setChallangeName(String challangeName) {
        this.challangeName = challangeName;
    }
    
    
}
