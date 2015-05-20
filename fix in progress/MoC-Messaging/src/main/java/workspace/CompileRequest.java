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
public class CompileRequest extends TeamRequest{
    
    private String challengeName;

    public CompileRequest(String competition, String teamname, String challengeName) {
        super(Action.COMPILE, competition, teamname);
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }
}
