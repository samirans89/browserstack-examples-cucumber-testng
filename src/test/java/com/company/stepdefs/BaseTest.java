package com.company.stepdefs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.browserstack.local.Local;
import com.company.utils.Utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;

@CucumberOptions(features = "src/test/resources/com/wf", glue = "com.wf.stepdefs", plugin = {
        "io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm", "pretty", "json:target/cucumber-report/report.json" })
public class BaseTest {

    public static Local local;
    protected DesiredCapabilities capabilities;
    private static final String PASSED = "passed";
    private static final String FAILED = "failed";
    protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    private final String BSTACK_HUB_URL = "https://hub-cloud.browserstack.com/wd/hub";
    private static final Object SYNCHRONIZER = new Object();

    final static Logger logger = LoggerFactory.getLogger(BaseTest.class);

    private TestNGCucumberRunner testNGCucumberRunner;

    public BaseTest() {
        // default constructor needed by Cucumber
    }

    public BaseTest(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    @BeforeSuite
    public void beforeSuite() throws Exception {

        if (capabilities == null) {
            throw new Exception(
                    "No test capabilities found for the given maven test profile. Please define appropriate profile in pom.xml.");
        }
        synchronized (SYNCHRONIZER) {
            if (capabilities.getCapability("browserstack.local") != null && local == null) {
                local = new Local();
                Map<String, String> options = new HashMap<String, String>();
                String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
                options.put("key", accessKey);
                local.start(options);
            }
        }
    }

    @BeforeClass
    public void setUpClass() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Before
    public void startup(Scenario scenario) throws Exception {

        Utility.setSessionName(getDriver(), scenario.getName());

    }

    @Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "scenarios")
    public void feature(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {

        try {

            switch (capabilities.getCapability("os").toString()) {

                case "ios":
                    IOSDriver<IOSElement> iOSDriver = new IOSDriver<IOSElement>(new URL(BSTACK_HUB_URL), capabilities);
                    iOSDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                    threadLocalDriver.set(iOSDriver);
                    break;
                case "android":
                    AndroidDriver<AndroidElement> androidDriver = new AndroidDriver<AndroidElement>(
                            new URL(BSTACK_HUB_URL), capabilities);
                    androidDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                    threadLocalDriver.set(androidDriver);
                default:
                    break;

            }

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        }
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());

    }

    @DataProvider(parallel = true)
    public Object[][] scenarios() {

        return testNGCucumberRunner.provideScenarios();

    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (testNGCucumberRunner == null) {
            return;
        }
        testNGCucumberRunner.finish();
    }

    @After
    public void teardown(Scenario scenario) throws Exception {
        try {
            if (System.getProperty("environment").equalsIgnoreCase("remote")) {
                if (scenario.isFailed()) {
                    Utility.setSessionStatus(getDriver(), FAILED, String.format("%s failed.", scenario.getName()));
                } else {
                    Utility.setSessionStatus(getDriver(), PASSED, String.format("%s passed.", scenario.getName()));
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            getDriver().quit();
        }
    }

    @AfterSuite
    public void tearDown() throws Exception {

        if (local != null) {
            local.stop();
        }

    }

    public synchronized static void setDriver(WebDriver driver) {
        threadLocalDriver.set(driver);
    }

    public synchronized static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

}