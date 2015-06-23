package controllers;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Casper
 */
public class PathControllerTest {

    public PathControllerTest() {
    }

    /**
     * Test of getInstance method, of class PathController.
     */
    @Test
    public void testGetInstance() {
        PathController pc1 = PathController.getInstance();
        PathController pc2 = PathController.getInstance();
        assertTrue(pc1 != null);
        assertTrue(pc2 != null);
        assertEquals(pc1, pc2);
    }

    /**
     * Test of getDefaultPath method, of class PathController.
     */
    @Test
    public void testGetDefaultPath() {
        PathController pc = PathController.getInstance();
        assertTrue(pc.getDefaultPath().endsWith("MoC"));
    }

    /**
     * Test of getCompetitionsPath method, of class PathController.
     */
    @Test
    public void testGetCompetitionsPath() {
        PathController pc = PathController.getInstance();
        assertTrue(pc.getCompetitionsPath().endsWith("MoC\\Competitions"));
    }

    /**
     * Test of competitionPath method, of class PathController.
     */
    @Test
    public void testCompetitionPath() {
        PathController pc = PathController.getInstance();
        assertTrue(pc.competitionPath("testCompetition").endsWith("MoC\\Competitions\\testCompetition"));
    }

    /**
     * Test of challengesPath method, of class PathController.
     */
    @Test
    public void testChallengesPath() {
        PathController pc = PathController.getInstance();
        assertTrue(pc.challengesPath("testCompetition").endsWith("MoC\\Competitions\\testCompetition\\Challenges"));
    }

    /**
     * Test of teamsPath method, of class PathController.
     */
    @Test
    public void testTeamsPath() {
        PathController pc = PathController.getInstance();
        assertTrue(pc.teamsPath("testCompetition").endsWith("MoC\\Competitions\\testCompetition\\Teams"));
    }

    /**
     * Test of teamPath method, of class PathController.
     */
    @Test
    public void testTeamPath() {
        PathController pc = PathController.getInstance();
        assertTrue(pc.teamPath("testCompetition", "testTeam").endsWith("MoC\\Competitions\\testCompetition\\Teams\\testTeam"));
    }

    /**
     * Test of teamChallengePath method, of class PathController.
     */
    @Test
    public void testTeamChallengePath() {
        PathController pc = PathController.getInstance();
        assertTrue(pc.teamChallengePath("testCompetition", "testTeam", "testChallenge").endsWith("MoC\\Competitions\\testCompetition\\Teams\\testTeam\\testChallenge"));
    }

}
