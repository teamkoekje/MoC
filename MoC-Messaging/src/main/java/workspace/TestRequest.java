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
    
    private String challangeName;
    private String testName;

    public TestRequest(String competition, String teamname, String challangeName, String testName) {
        super(Action.TEST, competition, teamname);
        this.challangeName = challangeName;
        this.testName = testName;
    }

    public String getChallengeName() {
        return challangeName;
    }

    public void setChallangeName(String challangeName) {
        this.challangeName = challangeName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
    
    
}
