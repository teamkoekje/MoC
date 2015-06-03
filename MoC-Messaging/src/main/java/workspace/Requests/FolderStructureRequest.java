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
public class FolderStructureRequest extends TeamRequest{
    
    private final String challengeName;

    public FolderStructureRequest(long competitionId, String challengeName, String teamName) {
        super(Action.FOLDER_STRUCTURE, competitionId, teamName);
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }
}
