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
    
    private String filepath;
    private String challangeName;

    public FileRequest(String competition, String teamname, String challangeName, String filepath) {
        super(Action.FILE, competition, teamname);
        this.challangeName = challangeName;
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getChallangeName() {
        return challangeName;
    }

    public void setChallangeName(String challangeName) {
        this.challangeName = challangeName;
    }
}
