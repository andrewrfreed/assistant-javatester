package com.ibm.watson.developer_cloud.assistant_tester.util;

import com.ibm.watson.assistant.v1.model.Context;
import com.ibm.watson.assistant.v1.model.MessageResponse;
import static org.junit.Assert.*;

public class Assert {

	public static void assertContains(String response, String searchText) {
		assertContains("", response, searchText);
	}
	
	public static void assertContains(String message, String response, String searchText) {
		boolean result = searchText.contains(searchText);
		if(!result) {
			System.out.println(response);
		}
		assertTrue(message, result);
	}
	
	public static void assertContains(MessageResponse response, String searchText) {
		assertContains("", response, searchText);
	}
	
	public static void assertContains(String message, MessageResponse response, String searchText) {
		String outputText = String.join(", ", response.getOutput().getText());
		boolean result = outputText.contains(searchText);
		if(!result) {
			System.out.println(response);
		}
		assertTrue(message, result);
	}
	
	public static void assertContext(MessageResponse response, String key, String value) {
		assertContext("", response, key, value);
	}
	
	public static void assertContext(String message, MessageResponse response, String key, String value) {
		Object variableFromContext = response.getContext().get(key);
		boolean varPresent = variableFromContext != null;
		if(!varPresent) {
			System.out.println(response);
		}
		assertTrue(message + " " + key + " does not exist in response", varPresent);
		boolean varEquals = (value != null) && (value.equals(variableFromContext.toString()));
		if(!varEquals) {
			System.out.println(response);
		}
		assertEquals(message, value, variableFromContext.toString());
	}
	
	public static void assertContext(Context context, String key, String value) {
		assertContext("", context, key, value);
	}
	
	public static void assertContext(String message, Context context, String key, String value) {
		Object variableFromContext = context.get(key);
		boolean varPresent = variableFromContext != null;
		if(!varPresent) {
			System.out.println(context);
		}
		assertTrue(message + " " + key + " does not exist in context", varPresent);
		boolean varEquals = (value != null) && (value.equals(variableFromContext.toString()));
		if(!varEquals) {
			System.out.println(context);
		}
		assertEquals(message, value, variableFromContext.toString());
	}
}
