package domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Test;

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
        } catch (IllegalArgumentException ex) {
            fail("Unexpected IllegalArgumentException");
        }

    }
}
