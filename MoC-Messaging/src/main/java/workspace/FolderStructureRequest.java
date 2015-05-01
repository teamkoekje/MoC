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
public class FolderStructureRequest extends TeamRequest{
    
    private String challengeName;

    public FolderStructureRequest(String competition, String challengeName, String teamname) {
        super(Action.FOLDER_STRUCTURE, competition, teamname);
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }
}
