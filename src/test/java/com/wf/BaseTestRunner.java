package com.wf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wf.stepdefs.BaseTest;
import com.wf.utils.Utility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

public class BaseTestRunner {

    private static final String BUILD_NAME = "wf_browserstack_cucumber_testng_";
    private static final String DEVICE_CONFIG_FILE = "src/test/resources/config/device_config.json";
    private JSONObject configFileJSONObj;

    final static Logger logger = LoggerFactory.getLogger(BaseTestRunner.class);

    @Factory(dataProvider = "getAllEnvCapabilities")
    public Object[] testFactory(DesiredCapabilities capabilities) throws Exception {

        Object[] testObjs = new Object[] { new BaseTest(capabilities) };
        return testObjs;
    }

    @DataProvider(name = "getAllEnvCapabilities", parallel = true)
    private Object[][] getAllEnvCapabilities(ITestContext context) throws Exception {

        String testConfig = System.getProperty("profileId");
        List<DesiredCapabilities> capabilitiesList = new ArrayList<DesiredCapabilities>();

        try {

            if (configFileJSONObj == null) {
                configFileJSONObj = Utility.parseJSONFile(DEVICE_CONFIG_FILE);
            }

            JSONArray envArr = (JSONArray) Utility.getJSONObject(configFileJSONObj, "tests", testConfig, "env_caps");

            if (envArr != null) {
                for (Object env : envArr) {
                    DesiredCapabilities capabilities = new DesiredCapabilities();
                    capabilities.merge(new DesiredCapabilities(Utility.getCapabiltiesMapFromJSONObj(configFileJSONObj,
                            "tests", testConfig, "common_caps")));

                    Map<String, String> localCapabilities = Utility.getCapabiltiesMapFromJSONObj(configFileJSONObj,
                            "tests", testConfig, "local_binding_caps");
                    if (localCapabilities != null && localCapabilities.size() != 0) {
                        capabilities.merge(new DesiredCapabilities(localCapabilities));
                    }

                    capabilities.setCapability("browserstack.user", Utility.getEnvOrDefault("BROWSERSTACK_USERNAME",
                            Utility.getJSONObject(configFileJSONObj, "user").toString().trim()));
                    capabilities.setCapability("browserstack.key", Utility.getEnvOrDefault("BROWSERSTACK_ACCESS_KEY",
                            Utility.getJSONObject(configFileJSONObj, "key").toString().trim()));
                    capabilities.setCapability("build",
                            Utility.getEnvOrDefault("BROWSERSTACK_BUILD_NAME", BUILD_NAME + Utility.getEpochTime()));

                    String googleUsername = System.getenv("GOOGLE_USERNAME");
                    String googlePassword = System.getenv("GOOGLE_PASSWORD");
                    if (googleUsername != null && googlePassword != null) {
                        capabilities.setCapability("browserstack.appStoreConfiguration", "{ \"username\" :\""
                                + googleUsername + "\", \"password\" : \"" + googlePassword + "\" }");
                    }

                    capabilities.merge(new DesiredCapabilities(Utility.getCapabiltiesMap(env)));
                    capabilitiesList.add(capabilities);
                }
            }

        } catch (IOException | org.json.simple.parser.ParseException e) {
            logger.error(e.getMessage());
        }

        if (capabilitiesList.size() == 0) {
            throw new Exception("No test capabilities found in " + DEVICE_CONFIG_FILE + " for the given test profile");
        }

        return capabilitiesList.stream().map(capabilities -> new Object[] { capabilities }).toArray(Object[][]::new);

    }
}