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
public class FileRequest extends TeamRequest{
    
    private String filepath;
    private String challengeName;

    public FileRequest(String competition, String teamname, String challengeName, String filepath) {
        super(Action.FILE, competition, teamname);
        this.challengeName = challengeName;
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }
}
