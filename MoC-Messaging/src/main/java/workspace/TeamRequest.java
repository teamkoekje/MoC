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
public abstract class TeamRequest extends Request {

    private final String teamName;

    public TeamRequest(Action action, long competitionId, String teamName) {
        super(action, competitionId);
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }
}
