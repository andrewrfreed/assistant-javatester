package com.ibm.watson.developer_cloud.assistant_tester;

/**
 * Encapsulates a single conversational turn with user utterance, 
 * expected text output (substring), and expected context variable name/value pairs.
 */
public class Turn {

	public final String utterance;
	public final String expectedOutput;
	public final String[] context;
	
	public Turn(String utterance, String expectedOutput, String[] context) {
		super();
		this.utterance = utterance;
		this.expectedOutput = expectedOutput;
		this.context = context;
	}

	public String getUtterance() {
		return utterance;
	}
	
	public String getExpectedOutput() {
		return expectedOutput;
	}
	
	public String[] getContext() {
		return context;
	}
}
