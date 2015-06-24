package tests;

import challenge.TestChallenge2;
import challenge.TestChallenge2Impl;
import java.util.logging.Logger;
import org.junit.Assert;
import org.testng.annotations.Test;

@Test(
    testName = "HelloWorldPlusTest",
    groups={TestGroups.USER}, 
    description = "this test checks if the helloWorldPlus method works"
)
public class HelloWorldPlusTest extends AbstractTest{

    private static final Logger LOG = Logger.getLogger(HelloWorldPlusTest.class.getName());


    @Override
    public void doTest() {
        logTestDetails();
        TestChallenge2 challenge = new TestChallenge2Impl();
        String input = "Barry";
        String expected = "hello world! Barry";
        Assert.assertEquals(challenge.helloWorldPlus(input), expected);
    }
}
