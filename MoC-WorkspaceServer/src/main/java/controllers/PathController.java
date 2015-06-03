package controllers;

// <editor-fold defaultstate="collapsed" desc="imports" >
import java.io.File;
// </editor-fold>

/**
 * Singleton class used to simply get paths within the workspace servers.
 *
 * @author TeamKoekje
 */
public class PathController {

    //<editor-fold defaultstate="collapsed" desc="Variables" >
    private final String defaultPath;
    private final String competitionsPath;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    private static PathController instance;

    /**
     * Creates a new instance of the PathController class
     */
    public PathController() {
        String osName = System.getProperty("os.name");
        if ("linux".equalsIgnoreCase(osName)) {
            defaultPath = "MoC";
        } else {
            defaultPath = "C:/MoC";
        }

        competitionsPath = defaultPath + File.separator + "Competitions";
    }

    /**
     * Gets the instance of the PathController singleton.
     *
     * @return The PathController singleton instance.
     */
    public static PathController getInstance() {
        if (instance == null) {
            instance = new PathController();
        }
        return instance;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters" >
    /**
     * Gets the default path where all other paths are related to
     *
     * @return If the OS is linux: "MoC" otherwise: "C:/MoC"
     */
    public String getDefaultPath() {
        return defaultPath;
    }

    /**
     * Gets the path to the folder where all competitions are stored. Path:
     * "default path"/Competitions
     *
     * @return A String containing the path to the folder where all competitions
     * are stored.
     */
    public String getCompetitionsPath() {
        return competitionsPath;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Gets the path for a specific competition. Path: "default
     * path"/Competitions/"competition id"
     *
     * @param competitionId The id of the competition to get the path of.
     * @return A String containing the path to the specified competition
     */
    public String competitionPath(String competitionId) {
        return getCompetitionsPath() + File.separator + competitionId;
    }

    public String challengesPath(String competitionId) {
        return competitionPath(competitionId) + File.separator + "Challenges";
    }

    /**
     * Gets the path to the teams directory within a specific competition. Path:
     * "default path"/Competitions/"competition id"/Teams
     *
     * @param competitionId The competition to get the teams path for
     * @return A String containing the path to the Teams directory within the
     * specified competition
     */
    public String teamsPath(String competitionId) {
        return competitionPath(competitionId) + File.separator + "Teams";
    }

    /**
     * Gets the path to a specific team within a specific competition. Path:
     * "default path"/Competitions/"competition id"/Teams/"team name"
     *
     * @param competitionId The id of the competition to get the path for
     * @param teamName The name of the team to get the path of
     * @return A String containing the path to the specified team within the
     * specified competition.
     */
    public String teamPath(String competitionId, String teamName) {
        return teamsPath(competitionId) + File.separator + teamName;
    }

    /**
     * Gets the path to a specific challenge within a specific team. Path:
     * "default path"/Competitions/"competition id"/Teams/"team name"/"challenge
     * name"
     *
     * @param competitionId The id of the competition to get the path for
     * @param teamName The name of the team to get the path of
     * @param challengeName The name of the challenge to get the path of
     * @return
     */
    public String teamChallengePath(String competitionId, String teamName, String challengeName) {
        return teamPath(competitionId, teamName) + File.separator + challengeName;
    }
    //</editor-fold>
}
