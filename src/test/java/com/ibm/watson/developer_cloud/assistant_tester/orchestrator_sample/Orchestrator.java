package com.ibm.watson.developer_cloud.assistant_tester.orchestrator_sample;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.assistant_tester.Conversation;

/**
 * Simple Watson Assistant orchestrator for working with the eCommerce intents.
 * This is NOT thread-safe.  Thread-safe instances would not contain specific conversation objects.
 * 
 * This is only a simple example.
 */
public class Orchestrator {
	
	private static String USERNAME = System.getProperty("ASSISTANT_USERNAME");
	private static String PASSWORD = System.getProperty("ASSISTANT_PASSWORD");
	private static String VERSION = "2018-02-16";
	private static String WORKSPACE_ID = System.getProperty("WORKSPACE_ID");
    
	/** Context key: Should user input be sent to Watson or handled internally in the orchestrator? */
	private static final String GO_TO_WATSON_KEY = "goToWatson";

	/** Context key: ID of Member we are talking to */
	private static final String MEMBER_ID_KEY = "memberId";

	/** Context key: What product order are we talking about? */
	private static final String ORDER_ID_KEY = "orderId";
	
    static {
    	if(USERNAME == null || PASSWORD == null || WORKSPACE_ID == null) {
    		System.err.println("Required environment variables are ASSISTANT_USERNAME, ASSISTANT_PASSWORD, and WORKSPACE_ID");
    		System.exit(-1);
    	}
    }

    private final IOrderManagement orderService;
    private final Conversation conversation;
    private String lastIntent;
    
    /**
     * Use of dependency injection is key.  Relying on an interface simplifies our unit testing.
     * @param orderService
     */
	public Orchestrator(IOrderManagement orderService) {
		Assistant service = new Assistant(VERSION);
	    service.setUsernameAndPassword(USERNAME, PASSWORD);
	    conversation = new Conversation(service, WORKSPACE_ID);
	    
	    conversation.getContext().put(GO_TO_WATSON_KEY, "true");
	    this.orderService = orderService;
	}
	
	/**
	 * Receive user input utterance and generate a textual response.
	 * Maintain state in conversation context and call external services as required
	 * @param input User input utterance as text string
	 * @return Textual response sent to user
	 */
	public String onInput(String input) {
		System.out.println("Received input: " + input);
		
		String toSend = "How can I help you next?";
		boolean goToWatson = Boolean.parseBoolean((String) conversation.getContext().get(GO_TO_WATSON_KEY));
		String memberId = (String) conversation.getContext().get(MEMBER_ID_KEY);
		if(goToWatson) {
			MessageResponse lastResponse = conversation.turn(input);
			lastIntent = lastResponse.getIntents().get(0).getIntent();
			toSend = String.join("\n", lastResponse.getOutput().getText());
		}
		switch(lastIntent) {

			//Cancellation requires custom orchestration.
			//This example uses a strict dialog flow where Watson Assistant is not asked to disambiguate.
			case "eCommerce_Cancel_Product_Order":
				if(memberId == null) {
					if(goToWatson) {
						toSend = "Sorry to hear that, what is your Member ID?";
						conversation.getContext().put(GO_TO_WATSON_KEY, "false");
					}
					else {
						//User is responding to Member ID prompt
						conversation.getContext().put(MEMBER_ID_KEY, input);
						String lastOrder = orderService.getLastOrder(input);
						conversation.getContext().put(ORDER_ID_KEY, lastOrder);
						toSend = String.format("Cancel order number %s?", lastOrder);
					}
				}
				else /* memberId != null*/{
					//User was just asked if they want to cancel
					if(input.toLowerCase().contains("yes")) {
						String orderId = (String) conversation.getContext().get(ORDER_ID_KEY);
						orderService.cancelOrder(orderId);
						toSend = String.format("I cancelled order number %s.", orderId);
						conversation.getContext().put(GO_TO_WATSON_KEY, "true");
					}
					else {
						//Handle that user does NOT want to cancel this order.
					}
				}
				
				break;
			default:
				//By default we use the response configured in Watson Assistant, stored in toSend earlier
		}
		
		sendResponse(toSend);
		
		return toSend;
	}
	
	/**
	 * Send response to user. (Stub implementation)
	 * @param response
	 */
	private void sendResponse(String response) {
		System.out.println("Sending response: " + response);
		conversation.getContext().put("lastResponse", response);
	}
	
	/* Non-thread-safe implementation */
	public Context getContext() {
		return conversation.getContext();
	}
}
