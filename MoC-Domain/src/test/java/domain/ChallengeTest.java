package domain;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChallengeTest {

    public ChallengeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateChallenge() {
        Challenge c;
        try {
            c = new Challenge(null);
            fail("Expected IllegalArgumentException to be thrown on name");
        } catch (IllegalArgumentException ex) {
        }
        try {
            c = new Challenge("Challenge1");
            assertEquals(c.getName(), "Challenge1");
        } catch (IllegalArgumentException ex) {
            fail("Unexpected IllegalArgumentException");

        }
    }

    @Test
    public void testAddHint() {
        Challenge c = new Challenge("Challenge1");
        List<Hint> hintsCopy;
        Hint h1 = new Hint("Hint1", 5);
        Hint h2 = new Hint("Hint1", 2);
        Hint h3 = new Hint("Hint1", 6);

        hintsCopy = c.getHintsCopy();
        assertFalse(hintsCopy.size() > 0);
        
        c.addHint(h1);
        hintsCopy = c.getHintsCopy();                
        assertTrue(hintsCopy.size() > 0);
        assertEquals(hintsCopy.get(0), h1);

        c.addHint(h2);
        hintsCopy = c.getHintsCopy();         
        assertEquals(hintsCopy.get(0), h2);
        assertEquals(hintsCopy.get(1), h1);

        c.addHint(h3);
        hintsCopy = c.getHintsCopy(); 
        assertEquals(hintsCopy.get(0), h2);
        assertEquals(hintsCopy.get(1), h1);
        assertEquals(hintsCopy.get(2), h3);
    }

}
