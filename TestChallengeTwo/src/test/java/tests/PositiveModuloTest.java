package tests;

import challenge.TestChallenge2;
import challenge.TestChallenge2Impl;
import java.util.logging.Logger;
import org.junit.Assert;
import org.testng.annotations.Test;

@Test(
        testName = "PositiveModuloTest",
        groups = {TestGroups.USER, TestGroups.SYSTEM},
        description = "this test checks if the positiveModuloTest method works"
)
public class PositiveModuloTest extends AbstractTest {

    private static final Logger LOG = Logger.getLogger(PositiveModuloTest.class.getName());

    @Override
    public void doTest() {
        logTestDetails();
        TestChallenge2 challenge = new TestChallenge2Impl();
        int a = -13;
        int b = 64;
        int expected = 51;
        Assert.assertEquals(challenge.positiveModulo(a, b), expected);

        a = 10;
        b = -15;
        expected = 5;
        Assert.assertEquals(challenge.positiveModulo(a, b), expected);

        a = 23;
        b = 15;
        expected = 8;
        Assert.assertEquals(challenge.positiveModulo(a, b), expected);

        try {
            a = 0;
            b = 0;
            expected = challenge.positiveModulo(a, b); // / by 0 error
            Assert.assertEquals(true, false); // if devided by 0 is allowed, fail test
        } catch (Exception e) {
            Assert.assertEquals(true, true); // true if devided by 0 is giving an error
        }

    }
}
