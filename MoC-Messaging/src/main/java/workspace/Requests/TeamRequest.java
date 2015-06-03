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
public abstract class TeamRequest extends Request {

    private final String teamName;

    public TeamRequest(Action action, String competition, String teamName) {
        super(action, competition);
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }
}
