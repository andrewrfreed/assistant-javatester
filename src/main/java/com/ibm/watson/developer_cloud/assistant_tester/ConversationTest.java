package com.ibm.watson.developer_cloud.assistant_tester;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates an entire conversation test case with one or more turns.
 */
public class ConversationTest {

	public final String name;
	public final List<Turn> turns;
	public final String[] initialContext;
	
	public ConversationTest(String name, String[] initialContext) {
		this.name = name;
		this.initialContext = initialContext;
		this.turns = new ArrayList<Turn>();
	}
	
	public String getName() {
		return name;
	}
	public String[] getInitialContext() {
		return initialContext;
	}
	public List<Turn> getTurns() {
		return turns;
	}
	
	public String toString() {
		return getName();
	}
}
