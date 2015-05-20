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
    
    private final String filePath;
    private final String fileContent;

    public UpdateRequest(String competition, String teamName, String filePath, String fileContent) {
        super(Action.UPDATE, competition, teamName);
        this.filePath = filePath;
        this.fileContent = fileContent;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileContent() {
        return fileContent;
    }
}
