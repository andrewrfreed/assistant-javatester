package com.ibm.watson.developer_cloud.assistant_tester.util;

import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import static org.junit.Assert.*;

public class Assert {

	public static void assertContains(String response, String searchText) {
		assertContains("", response, searchText);
	}
	
	public static void assertContains(String message, String response, String searchText) {
		assertTrue(message, response.contains(searchText));
	}
	
	public static void assertContains(MessageResponse response, String searchText) {
		assertContains("", response, searchText);
	}
	
	public static void assertContains(String message, MessageResponse response, String searchText) {
		String outputText = String.join(", ", response.getOutput().getText());
		assertTrue(message, outputText.contains(searchText));
	}
	
	public static void assertContext(MessageResponse response, String key, String value) {
		assertContext("", response, key, value);
	}
	
	public static void assertContext(String message, MessageResponse response, String key, String value) {
		Object variableFromContext = response.getContext().get(key);
		assertTrue(message + " " + key + " does not exist in response", variableFromContext != null);
		assertEquals(message, value, variableFromContext.toString());
	}
	
	public static void assertContext(Context context, String key, String value) {
		assertContext("", context, key, value);
	}
	
	public static void assertContext(String message, Context context, String key, String value) {
		Object variableFromContext = context.get(key);
		assertTrue(message + " " + key + " does not exist in response", variableFromContext != null);
		assertEquals(message, value, variableFromContext.toString());
	}
}
