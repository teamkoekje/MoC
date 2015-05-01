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
public class PushRequest extends Request{
    
    private String challangeName;

    public PushRequest(String competition, String challangeName) {
        super(Action.PUSH_CHALLENGE, competition);
        this.challangeName = challangeName;
    }

    public String getChallengeName() {
        return challangeName;
    }

    public void setChallangeName(String challangeName) {
        this.challangeName = challangeName;
    }
}
