![Logo](https://www.browserstack.com/images/static/header-logo.jpg)

# BrowserStack Examples Cucumber TestNG <a href="https://cucumber.io"><img src="https://brandslogos.com/wp-content/uploads/images/large/cucumber-logo.png" alt="Cucumber" height="22" /></a> <a href="https://testng.org/"><img src="https://e7.pngegg.com/pngimages/640/776/png-clipart-testng-logo-software-testing-software-framework-computer-icons-automation-testing-angle-text.png" alt="TestNG" height="22" /></a>

## Introduction

TestNG is a testing framework designed to simplify a broad range of testing needs, from unit testing (testing a class in isolation of the others) to integration testing (testing entire systems made of several classes, several packages and even several external frameworks, such as application servers). Cucumber is a software tool that supports behavior-driven development (BDD).

---

## Repository setup

- Clone the repository, switch to branch 'wf_app'

- Ensure you have the following dependencies installed on the machine

  - Java >= 8
  - Maven >= 3.1+

  Maven:

  ```sh
  mvn install -DskipTests
  ```

## About the tests in this repository

This repository contains the following Appium profiles:

| Profiles      | Description                                                                                                                                                                        |
| ------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| prod_smoke    | Runs Cucumber test scenarios with tags: @prod and @smoke. This profile will download the app from Google PlayStore and hence, a Google account is needed for running this profile. |
| qa_regression | Runs Cucumber test scenarios with tags: @qa and @regression. This profile activates BrowserStack Local testing considering an internal QA environment.                             |
| qa_functional | Runs Cucumber test scenarios with tags: @qa and @functional. This profile activates BrowserStack Local testing considering an internal QA environment.                             |
| uat_critical  | Runs Cucumber test scenarios with tags: @uat and @critical.                                                                                                                        |

---

## Test infrastructure environments

- [BrowserStack](#browserstack)

## Configuring the maximum parallel test threads for this repository

- BrowserStack

  [testng_runner.xml](src/test/resources/conf/testng_runner.xml)
  Defaults:
  thread-count = 5
  data-provider-thread-count = 5

## Test Reporting

- [Allure reports](#generating-allure-reports)

---

# BrowserStack

[BrowserStack](https://browserstack.com) provides instant access to 2,000+ real mobile devices and browsers on a highly reliable cloud infrastructure that effortlessly scales as testing needs grow.

## Prerequisites

- Create a new [BrowserStack account](https://www.browserstack.com/users/sign_up) or use an existing one.
- Identify your BrowserStack username and access key from the [BrowserStack Automate Dashboard](https://automate.browserstack.com/) and export them as environment variables using the below commands.

  - For \*nix based and Mac machines:

  ```sh
  export BROWSERSTACK_USERNAME=<browserstack-username> &&
  export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
  ```

  - For Windows:

  ```shell
  set BROWSERSTACK_USERNAME=<browserstack-username>
  set BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
  ```

  Alternatively, you can also hardcode username and access_key objects in the [device_config.json](src/test/resources/config/device_config.json) file.

Note:

- We have configured a list of test capabilities in the [device_config.json](src/test/resources/config/device_config.json) file. You can certainly update them based on your device / browser test requirements.
- The exact test capability values can be easily identified using the [Browserstack Capability Generator](https://browserstack.com/automate/capabilities)

## BrowserStack App Upload

- Upload your app on BrowserStack using cUrl or a REST Client.

  cUrl Example:

  ```sh
    curl -u $BROWSERSTACK_USERNAME:$BROWSERSTACK_ACCESS_KEY \
    -X POST "https://api-cloud.browserstack.com/app-automate/upload" \
    -F "file=@<DIR_PATH>/browserstack-examples-cucumber-testng/app/wf_app.apk" \
    -F "custom_id=WF_App"
  ```

  where, <DIR_PATH> = Directory path to the repossitory on the machine.

  Note: For the 'prod_smoke' test profile, the app will be downloaded from the Google PlayStore. In this case, we can upload any sample app for starting the app automate test session, except for the same app id which would be downloaded from the Google PlayStore.

  cURL for Sample app:

  ```sh
    curl -u $BROWSERSTACK_USERNAME:$BROWSERSTACK_ACCESS_KEY \
    -X POST "https://api-cloud.browserstack.com/app-automate/upload" \
    -F "url=https://www.browserstack.com/app-automate/sample-apps/android/WikipediaSample.apk"
    -F "custom_id=WikipediaSample"
  ```

## Running Your Tests

### Run different test profiles on BrowserStack

- How to run a test profile?

  1. Regression tests on QA.

  Maven:

  ```sh
  mvn clean test -P qa_regression
  ```

  Note: This profile activates BrowserStack Local testing considering an internal QA environment.

  2. Smoke tests on Prod.

  Maven:

  ```sh
  GOOGLE_USERNAME=<YOUR_GMAIL_ID> GOOGLE_PASSWORD=<YOUR_GOOGLE_PASSWORD> mvn clean test -P prod_smoke
  ```

  Note: This profile will download the app from Google PlayStore and hence, a Google account is needed for running this profile.

  3. Functional tests on QA.

  Maven:

  ```sh
  mvn clean test -P qa_functional
  ```

  Note: This profile activates BrowserStack Local testing considering an internal QA environment.

  4. Critical tests on UAT.

  Maven:

  ```sh
  mvn clean test -P uat_critical
  ```

  These run profiles execute parallel tests on BrowserStack, as per the configurations and relevant cucumber tags in the feature files. Please refer to your [BrowserStack dashboard](https://app-automate.browserstack.com/) for test results.

- Note: By default, this execution would run maximum 10 test threads in parallel on BrowserStack. Refer to the section ["Configuring the maximum parallel test threads for this repository"](#Configuring-the-maximum-parallel-test-threads-for-this-repository) for updating the parallel thread count based on your requirements.

## Generating Allure Reports

In this section, we will generate and serve allure reports for maven test runs.

- Generate Report using the following command: `mvn io.qameta.allure:allure-maven:report`
- Serve the Allure report on a server: `mvn io.qameta.allure:allure-maven:serve`

## Additional Resources

- View your test results on the [BrowserStack App Automate dashboard](https://www.browserstack.com/app-automate)
- Documentation for writing [App Automate test scripts in Java](https://www.browserstack.com/docs/app-automate/appium/getting-started/java)
- Customizing your tests capabilities on BrowserStack using our [test capability generator](https://www.browserstack.com/app-automate/capabilities)
- [List of Browsers & mobile devices](https://www.browserstack.com/list-of-browsers-and-platforms?product=app-automate) for native app automation testing on BrowserStack
- [Using App Automate REST API](https://www.browserstack.com/app-automate/rest-api) to access information about your tests via the command-line interface
- For testing public web applications behind IP restriction, [Inbound IP Whitelisting](https://www.browserstack.com/local-testing/inbound-ip-whitelisting) can be enabled with the [BrowserStack Enterprise](https://www.browserstack.com/enterprise) offering

## Open Issues

- N/A
