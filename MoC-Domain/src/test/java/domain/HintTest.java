package domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import domain.Hint;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Astrid
 */
public class HintTest {

    public HintTest() {
    }

    @Test
    public void testCreateHint() {
        Hint h;
        try {
            h = new Hint("Hint text", 0);
            fail("Expected IllegalArgumentException to be thrown on time");
        } catch (IllegalArgumentException ex) {
        }

        try {
            h = new Hint("Hint text", -1);
            fail("Expected IllegalArgumentException to be thrown on time");
        } catch (IllegalArgumentException ex) {
        }

        try {
            h = new Hint(null, 1);
            fail("Expected IllegalArgumentException to be thrown on content");
        } catch (IllegalArgumentException ex) {
        }

        try {
            h = new Hint("Hint text", 1);
            assertEquals(h.getContent(), "Hint text");
            assertEquals(h.getTime(), 1);
            assertFalse(h.isPublished());
        } catch (IllegalArgumentException ex) {
            fail("Unexpected IllegalArgumentException");
        }

    }
}
