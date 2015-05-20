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
public class PushRequest extends Request {

    private String challengeName;

    public PushRequest(String competition, String challengeName) {
        super(Action.PUSH_CHALLENGE, competition);
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challangeName) {
        this.challengeName = challangeName;
    }
}
