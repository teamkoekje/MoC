package it.pkg.tests;

import org.junit.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.AbstractTest;
import tests.TestGroups;

@Test(
    testName = "SystemTest",
    groups={TestGroups.SYSTEM}, 
    description = "Enter a description for this test"
)
public class SystemTest extends AbstractTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    @Override
    public void doTest() {
        logTestDetails();
        Assert.assertTrue(true);
    }
    
}
