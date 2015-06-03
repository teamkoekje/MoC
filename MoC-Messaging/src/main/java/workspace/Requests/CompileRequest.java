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
public class CompileRequest extends TeamRequest{
    
    private final String challengeName;

    public CompileRequest(String competition, String teamName, String challengeName) {
        super(Action.COMPILE, competition, teamName);
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }
}
