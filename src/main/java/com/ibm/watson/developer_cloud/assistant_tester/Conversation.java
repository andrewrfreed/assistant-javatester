package com.ibm.watson.developer_cloud.assistant_tester;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;

/**
 * Wrapper for having conversations with a Watson Assistant instance
 */
public class Conversation {

	private final Assistant service;
	private final String workspaceId;
	private Context context;
	
	public Conversation(Assistant assistant, String workspaceId) {
		this.service = assistant;
		this.workspaceId = workspaceId;
		reset();
	}
	
	/** Starts a new conversation */
	public void reset() {
		this.context = new Context();
	}
	
	/**
	 * Performs a conversational turn
	 * @param utterance Input utterance from the user
	 * @return Full response from Watson Assistant
	 */
	public MessageResponse turn(String utterance) {
	    InputData input = new InputData.Builder(utterance).build();
	    MessageOptions options = new MessageOptions.Builder(workspaceId)
	        .input(input)
	        .context(context)
	        .build();

	    // synchronous request
	    MessageResponse response = service.message(options).execute();	
	    context = response.getContext();
	    return response;
	}
	
	/**
	 * Gets the state for the current Watson Assistant conversation
	 * @return Watson Assistant context object
	 */
	public Context getContext() {
		return context;
	}
}
