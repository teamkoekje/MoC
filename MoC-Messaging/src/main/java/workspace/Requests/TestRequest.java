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
public class TestRequest extends TeamRequest{
    
    private String challengeName;
    private String testFile;
    private String testName;

    public TestRequest(long competitionId, String teamname, String challengeName, String testFile, String testName) {
        super(Action.TEST, competitionId, teamname);
        this.challengeName = challengeName;
        this.testFile = testFile;
        this.testName = testName;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestFile() {
        return testFile;
    }

    public void setTestFile(String testFile) {
        this.testFile = testFile;
    }
}
