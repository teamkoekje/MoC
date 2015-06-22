package domain;

import domain.enums.RoundState;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoundTest {

    Round r;

    public RoundTest() {
    }

    @Before
    public void setUp() {
        Challenge c = new Challenge("Challenge1");
        dummyTeams.add(new Team("team 1", new Competition("comp 1", null, null, null, null, 0, 0)));
        dummyTeams.add(new Team("team 2", new Competition("comp 1", null, null, null, null, 0, 0)));
        dummyTeams.add(new Team("team 3", new Competition("comp 1", null, null, null, null, 0, 0)));
        r = new Round(c, 100,dummyTeams);
    }
    
    private List<Team> dummyTeams;

    @Test
    public void createRoundTest() {
        Round r;
        Challenge c = new Challenge("Challenge1");
        try {
            r = new Round(null, 1, dummyTeams);
            fail("Expected IllegalArgumentException to be thrown on challenge");
        } catch (IllegalArgumentException ex) {
        }
        try {
            r = new Round(c, 0, dummyTeams);
            fail("Expected IllegalArgumentException to be thrown on roundTime");
        } catch (IllegalArgumentException ex) {
        }
        try {
            r = new Round(c, 1, dummyTeams);
            assertEquals(r.getChallenge(), c);
            assertEquals(r.getDuration(), 1);
        } catch (IllegalArgumentException ex) {
            fail("Unexpected IllegalArgumentException");

        }
    }

    @Test
    public void testRoundState() {
        assertEquals(r.getRoundState(), RoundState.NOT_STARTED);
        r.start();
        assertEquals(r.getRoundState(), RoundState.ONGOING);
        r.pause();
        assertEquals(r.getRoundState(), RoundState.PAUSED);
        r.resume();
        assertEquals(r.getRoundState(), RoundState.ONGOING);
    }

    @Test
    public void testStartRound() {
        assertEquals(r.getRoundState(), RoundState.NOT_STARTED);
        try {
            r.start();
        } catch (IllegalArgumentException ex) {
            fail("Unexpected IllegalArgumentException");
        }
        assertEquals(r.getRoundState(), RoundState.ONGOING);
        try {
            r.start();
            fail("Expected IllegalArgumentException to be thrown on start");
        } catch (IllegalArgumentException ex) {
        }
        assertEquals(r.getRoundState(), RoundState.ONGOING);
    }

    @Test
    public void testPauseRound() {
        assertEquals(r.getRoundState(), RoundState.NOT_STARTED);
        try {
            r.pause();
            fail("Expected IllegalArgumentException to be thrown on pause");
        } catch (IllegalArgumentException ex) {
        }
        assertEquals(r.getRoundState(), RoundState.NOT_STARTED);
        r.start();
        assertEquals(r.getRoundState(), RoundState.ONGOING);

        try {
            r.pause();
        } catch (IllegalArgumentException ex) {
            fail("Unexpected IllegalArgumentException");
        }
        assertEquals(r.getRoundState(), RoundState.PAUSED);
        try {
            r.pause();
            fail("Expected IllegalArgumentException to be thrown on pause");
        } catch (IllegalArgumentException ex) {
        }
    }
    
    @Test
    public void testResumeRound() {
        assertEquals(r.getRoundState(), RoundState.NOT_STARTED);
        try {
            r.resume();
            fail("Expected IllegalArgumentException to be thrown on resume");
        } catch (IllegalArgumentException ex) {
        }
        r.start();
        r.pause();
        assertEquals(r.getRoundState(), RoundState.PAUSED);
        try {
            r.resume();
        } catch (IllegalArgumentException ex) {
            fail("Unexpected IllegalArgumentException");
        }
        assertEquals(r.getRoundState(), RoundState.ONGOING);
        try {
            r.resume();
            fail("Expected IllegalArgumentException to be thrown on pause");
        } catch (IllegalArgumentException ex) {
        }
    }
    
   @Test
    public void testFreezeRound() {
        assertEquals(r.getRoundState(), RoundState.NOT_STARTED);
        try {
            r.freeze();
            fail("Expected IllegalArgumentException to be thrown on freeze");
        } catch (IllegalArgumentException ex) {
        }
        assertEquals(r.getRoundState(), RoundState.NOT_STARTED);
        r.start();
        assertEquals(r.getRoundState(), RoundState.ONGOING);

        try {
            r.freeze();
        } catch (IllegalArgumentException ex) {
            fail("Unexpected IllegalArgumentException");
        }
        assertEquals(r.getRoundState(), RoundState.FROZEN);
        try {
            r.freeze();
            fail("Expected IllegalArgumentException to be thrown on freeze");
        } catch (IllegalArgumentException ex) {
        }
    }
    
    //@Test
    public void testStopRound() {
        assertEquals(r.getRoundState(), RoundState.NOT_STARTED);
        try {
            r.stop();
            fail("Expected IllegalArgumentException to be thrown on stop");
        } catch (IllegalArgumentException ex) {
        }
        assertEquals(r.getRoundState(), RoundState.NOT_STARTED);
        r.start();
        assertEquals(r.getRoundState(), RoundState.ONGOING);

        try {
            r.stop();
        } catch (IllegalArgumentException ex) {
            fail("Unexpected IllegalArgumentException");
        }
        assertEquals(r.getRoundState(), RoundState.ENDED);
        try {
            r.stop();
            fail("Expected IllegalArgumentException to be thrown on stop");
        } catch (IllegalArgumentException ex) {
        }
    }
}
