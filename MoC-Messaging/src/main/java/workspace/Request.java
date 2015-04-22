package workspace;

import java.io.Serializable;

/**
 * This class represents an object that tells a specific workspace to perform a specific action
 * @author TeamKoekje
 */
public class Request implements Serializable {

    private final Action action;
    private String testName;
    private String teamName;

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

    public String getChallengeName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getTestFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getFilePath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getFileContent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
