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
public class UpdateRequest extends TeamRequest{
    
    private String filePath;
    private String fileContent;

    public UpdateRequest(String competition, String teamname, String filePath, String fileContent) {
        super(Action.UPDATE, competition, teamname);
        this.filePath = filePath;
        this.fileContent = fileContent;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
}
