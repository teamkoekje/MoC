package resources.annotatedProject.src.test.java.projectwithannotations.annotatedproject.tests;

import org.junit.Assert;
import org.testng.annotations.Test;
import tests.AbstractTest;
import tests.TestGroups;


@Test(
    testName = "AmbivalentTest",
    groups={TestGroups.USER,TestGroups.SYSTEM}, 
    description = "Enter a description for this test"
)
public class AmbivalentTest extends AbstractTest{

    @Override
    public void doTest() {
        logTestDetails();
        Assert.assertTrue(true);
    }
    
}
