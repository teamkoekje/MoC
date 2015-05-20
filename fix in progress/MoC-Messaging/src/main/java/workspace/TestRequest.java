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
public class TestRequest extends TeamRequest{
    
    private String challengeName;
    private String testName;

    public TestRequest(String competition, String teamname, String challengeName, String testName) {
        super(Action.TEST, competition, teamname);
        this.challengeName = challengeName;
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
    
    
}
