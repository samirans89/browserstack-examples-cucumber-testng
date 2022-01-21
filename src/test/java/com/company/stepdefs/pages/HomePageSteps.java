package com.company.stepdefs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class HomePageSteps extends CommonSteps {

    final static Logger logger = LoggerFactory.getLogger(HomePageSteps.class);

    @And("I start the app")
    public void startApp() {

        try {

            By approveElement = By.xpath(
                    "//android.widget.Button[@text='Allow only while using the app'] | //android.widget.Button[@text='ALLOW ONLY WHILE USING THE APP'] | //android.widget.Button[@text='While using the app'] | //android.widget.Button[@text='WHILE USING THE APP'] | //android.widget.Button[@text='Allow'] | //android.widget.Button[@text='ALLOW']");
            allowPermissions(approveElement, approveElement, approveElement);

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.debug(adriver.getPageSource());
        }
    }

    private void allowPermissions(By... byList) {

        for (By by : byList) {
            try {
                WebDriverWait wait = new WebDriverWait(adriver, 10);
                wait.until(ExpectedConditions.presenceOfElementLocated(by)).click();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                logger.debug(adriver.getPageSource());
            }
        }

    }

    @Given("I enter app credentials {string} {string}")
    public void iEnterAppCredentials(String user, String password) {

        String usernameField = "//android.widget.EditText[@text='Username']";
        String passwordField = "//android.widget.EditText[@text='Password']";
        adriver.findElementByXPath(usernameField).sendKeys(user);
        adriver.findElementByXPath(passwordField).sendKeys(password);

    }

    @And("I press Log In Button")
    public void login() {
        String signInWFAccount = "//android.widget.Button[@text='Sign On']";
        adriver.findElementByXPath(signInWFAccount).click();
    }

    @Then("I should see login error {string}")
    public void validateLoginResponse(String message) {

        String validateSignInError = "//android.widget.TextView[@text=\"%s\"]";

        WebDriverWait wait = new WebDriverWait(adriver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format(validateSignInError, message))));
    }
}
