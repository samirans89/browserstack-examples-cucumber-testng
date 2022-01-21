package com.company.stepdefs.pages;

import com.company.stepdefs.BaseTest;

import org.openqa.selenium.WebDriver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class CommonSteps {

    protected AndroidDriver<?> adriver;
    protected IOSDriver<?> idriver;

    public CommonSteps() {

        WebDriver driver = BaseTest.getDriver();

        if (driver instanceof AndroidDriver<?>) {
            adriver = (AndroidDriver<?>) BaseTest.getDriver();
        } else if (driver instanceof IOSDriver<?>)
            idriver = (IOSDriver<?>) BaseTest.getDriver();
    }

}
