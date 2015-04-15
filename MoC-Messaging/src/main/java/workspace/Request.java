package workspace;

import java.io.Serializable;

/**
 * This class represents an object that tells a specific workspace to perform a specific action
 * @author TeamKoekje
 */
public class Request implements Serializable {

    private final Action action;
    private final long workspaceId;
    private String testName;

    public Request(Action action, long workspaceId) {
        this.action = action;
        this.workspaceId = workspaceId;
    }

    public Action getAction() {
        return action;
    }

    public long getWorkspaceId() {
        return workspaceId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
