package com.wf.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {

	private static String epochTime = null;

	final static Logger logger = LoggerFactory.getLogger(Utility.class);

	private static final Object SYNCHRONIZER = new Object();

	public static String getEpochTime() {
		if (epochTime == null) {
			synchronized (SYNCHRONIZER) {
				if (epochTime == null) {
					epochTime = String.valueOf(Instant.now().toEpochMilli());
				}
			}
		}
		return epochTime;
	}

	public static void setSessionStatus(WebDriver webDriver, String status, String reason) {
		JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		jse.executeScript(String.format(
				"browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"%s\", \"reason\": \"%s\"}}",
				status, reason));
	}

	public static void setSessionName(WebDriver webDriver, String name) {
		JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		jse.executeScript(String.format(
				"browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\": \"%s\"}}", name));
	}

	public static JSONObject parseJSONFile(String filename) throws IOException, org.json.simple.parser.ParseException {
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(new String(Files.readAllBytes(Paths.get(filename))));
	}

	public static Object getJSONObject(JSONObject parent, String... keys) {

		Object temp = parent;
		try {

			for (String key : keys) {
				Object obj = ((JSONObject) temp).get(key);
				if (obj instanceof JSONObject) {
					temp = (JSONObject) obj;
				} else if (obj instanceof JSONArray) {
					temp = (JSONArray) obj;
				} else {
					temp = obj.toString();
				}
			}

		} catch (Exception e) {
			return null;
		}
		return temp;
	}

	public static Map<String, String> getCapabiltiesMapFromJSONObj(JSONObject parent, String... capKeyPathInJson) {
		@SuppressWarnings("unchecked")
		Map<String, String> capabilitiesMap = (Map<String, String>) Utility.getJSONObject(parent, capKeyPathInJson);
		return capabilitiesMap;
	}

	public static Map<String, String> getCapabiltiesMap(Object obj) {
		@SuppressWarnings("unchecked")
		Map<String, String> objMap = (Map<String, String>) obj;
		return objMap;
	}

	public static String getEnvOrDefault(String envKey, String defaultVal) {

		String envVal = System.getenv(envKey);
		return envVal != null ? envVal.trim() : defaultVal.trim();
	}

}
