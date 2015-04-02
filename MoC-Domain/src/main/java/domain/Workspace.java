package domain;

/**
 * The workspace class represents the workspace where a team can access the
 * challenges of a competition and create solutions.
 *
 * @author Astrid Belder
 */
public class Workspace {

    private Team team;
    private String path;

    public Workspace(Team team) {
        this.team = team;
    }

    /**
     * Function returns the team that owns the workspace.
     *
     * @return team that owns the workspace
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Function returns the path of the workspace directory.
     *
     * @return path of the workspace
     */
    public String getPath() {
        return null;
    }

    public String testCode() {
        return null;
    }

    public String compileCode() {
        return null;
    }
}
