/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Astrid
 */
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
        Hint h1 = new Hint("Hint1", 5);
        Hint h2 = new Hint("Hint1", 2);
        Hint h3 = new Hint("Hint1", 6);

        assertFalse(c.hintsIterator().hasNext());
        c.addHint(h1);
        assertTrue(c.hintsIterator().hasNext());
        assertEquals(c.hintsIterator().next(), h1);

        c.addHint(h2);
        Iterator i = c.hintsIterator();
        assertEquals(i.next(), h2);
        assertEquals(i.next(), h1);

        c.addHint(h3);
        i = c.hintsIterator();
        assertEquals(i.next(), h2);
        assertEquals(i.next(), h1);
        assertEquals(i.next(), h3);
    }

}
