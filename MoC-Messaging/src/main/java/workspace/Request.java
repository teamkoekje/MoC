package workspace;

import java.io.Serializable;

/**
 * This class represents an object that tells a specific workspace to perform a
 * specific action
 *
 * @author TeamKoekje
 */
public class Request implements Serializable {

    private final Action action;
    private String testFile;
    private String testName;
    private String teamName;
    private String filePath;
    private String fileContent;
    private String challengeName;

    public Request(Action action, String teamName) {
        this.action = action;
        this.teamName = teamName;
    }

    public Action getAction() {
        return action;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTestFile() {
        return testFile;
    }

    public void setTestFile(String testFile) {
        this.testFile = testFile;
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

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

}
