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
public class FileRequest extends TeamRequest{
    
    private final String filepath;
    private final String challengeName;

    public FileRequest(String competition, String teamName, String challengeName, String filepath) {
        super(Action.FILE, competition, teamName);
        this.challengeName = challengeName;
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getChallengeName() {
        return challengeName;
    }
}
