package domain;

import java.io.Serializable;

public class CodeFile implements Serializable{

    private String filePath;
    private String fileContent;

    protected CodeFile() {}

    public CodeFile(String filePath, String fileContent) {
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
