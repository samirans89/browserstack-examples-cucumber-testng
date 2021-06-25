package com.wf.stepdefs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.cucumber.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GooglePlaySteps extends CommonSteps {

    String PLAYSTORE_PACKAGE_NAME = "com.android.vending";
    String PLAYSTORE_ACTIVITY_NAME = "com.android.vending.AssetBrowserActivity";
    String PIXEL_PLAYSTORE_ACTIVITY_NAME = "com.google.android.finsky.activities.MainActivity";
    String APP_SEARCH_ON_PLAYSTORE = "Wells Fargo Mobile";
    final static Logger logger = LoggerFactory.getLogger(GooglePlaySteps.class);

    @Given("I download app from Google Play Store")
    public void iDownloadAppFromPlayStore() {

        try {

            switchToPlayStoreApp();
            skipOfferAdvertisements();
            searchAppOnPlayStore(APP_SEARCH_ON_PLAYSTORE);
            skipAccountSetup();
            openAppAfterDownload();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.debug(adriver.getPageSource());
        }

    }

    private void openAppAfterDownload() {

        String openButtonPath = "//android.widget.Button[@text='Open']";

        WebDriverWait wait = new WebDriverWait(adriver, 300);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(openButtonPath)));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(openButtonPath)));
        adriver.findElementByXPath(openButtonPath).click();

    }

    private void skipAccountSetup() {

        String continueButton = "//android.widget.Button[@text='Continue']";
        String skipButton = "//android.widget.Button[@text='Skip']";

        try {
            adriver.findElementByXPath(continueButton).click();
            adriver.findElementByXPath(skipButton).click();
            // adriver.findElementByXPath("//android.widget.Button[@text='Install']").click();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.debug("Continue element does not exist: " + ex.getMessage());
        }

    }

    private void switchToPlayStoreApp() {

        String device = adriver.getCapabilities().getCapability("device").toString();

        if (device.startsWith("Samsung")) {
            startApp(PLAYSTORE_PACKAGE_NAME, PLAYSTORE_ACTIVITY_NAME);
        } else if (device.startsWith("Google")) {
            startApp(PLAYSTORE_PACKAGE_NAME, PIXEL_PLAYSTORE_ACTIVITY_NAME);
        } else {
            startApp(PLAYSTORE_PACKAGE_NAME, PLAYSTORE_ACTIVITY_NAME);
        }

    }

    private void skipOfferAdvertisements() {

        String offerButtonOptions = "//android.widget.Button[@text='Not now'] | //android.widget.Button[@text='Got it'] | //android.widget.Button[@text='No thanks']";
        try {
            WebDriverWait wait = new WebDriverWait(adriver, 10);
            By skipElementLocator = By.xpath(offerButtonOptions);
            wait.until(ExpectedConditions.presenceOfElementLocated(skipElementLocator)).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(skipElementLocator)).click();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.debug("Skip elements do not exist: " + ex.getMessage());
        }

    }

    private void searchAppOnPlayStore(String appName) {

        String searchAppsField = "//*[contains(@text, 'Search for apps ')]";
        String searchAppsText = "//android.widget.EditText";
        String searchButton = "//android.widget.Button";

        adriver.findElementByXPath(searchAppsField).click();
        // adriver.findElementById("com.android.vending:id/search_bar_hint").click();

        adriver.findElementByXPath(searchAppsText).sendKeys(appName);
        adriver.pressKey(new KeyEvent(AndroidKey.ENTER));
        adriver.findElementByXPath(searchButton).click();

    }

    private void startApp(String appPackage, String appActivity) {
        try {
            adriver.startActivity(new Activity(appPackage, appActivity));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

}
