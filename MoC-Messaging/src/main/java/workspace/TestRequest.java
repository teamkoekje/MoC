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
    
    private final String challengeName;
    private final String testName;

    public TestRequest(String competition, String teamName, String challengeName, String testName) {
        super(Action.TEST, competition, teamName);
        this.challengeName = challengeName;
        this.testName = testName;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public String getTestName() {
        return testName;
    }
}
