package com.ibm.watson.developer_cloud.assistant_tester;

import static com.ibm.watson.developer_cloud.assistant_tester.util.Assert.assertContains;
import static com.ibm.watson.developer_cloud.assistant_tester.util.Assert.assertContext;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.ibm.watson.developer_cloud.assistant_tester.orchestrator_sample.IOrderManagement;
import com.ibm.watson.developer_cloud.assistant_tester.orchestrator_sample.Orchestrator;

public class ExampleTest_Orchestrator {
	
	/** 
	 * Has the orchestrator called the order cancellation function yet? 
	 * We set this in a test double.  Alternatively you could use a mocking framework to detect that a function was called.
	 */
	private boolean orderCancelled = false;
	Orchestrator orchestrator = null;
	
	@Before
	public void setup() {
		orderCancelled = false;
		
		/*
		 * Set up a test double for simple verification of the orchestrator.
		 * Alternatively, use a system like Mockito which provides greater function and less verbosity.
		 */
		orchestrator = new Orchestrator(new IOrderManagement() {
			@Override
			public String getLastOrder(String memberId) {
				return "456";
			}
			
			@Override
			public void cancelOrder(String orderId) {
				System.out.println("Cancelling order #" + orderId);
				orderCancelled = true;
			}
		});
	}
	
	@Test
	public void test_order_return() {
	   String utterance1 = "I would like to cancel my order";
	   String utterance2 = "Yes, cancel that order";
	   String memberId = "ABC123";
	   
	   //Conversation turn 1
	   //response = "Sorry to hear that, what is your Member ID?"
	   String resp1 = orchestrator.onInput(utterance1);
	   assertContains(resp1, "Member ID");

	   //Conversation turn 2
	   //response = "Cancel order number 456?"      
	   String resp2 = orchestrator.onInput(memberId);
	   assertContext(orchestrator.getContext(), "memberId", memberId);
	   assertContext(orchestrator.getContext(), "orderId", "456");
	   assertContains(resp2, "Cancel");
	   assertFalse(orderCancelled);

	   //Conversation turn 3
	   //response = "I cancelled order number 456."      
	   String resp3 = orchestrator.onInput(utterance2);
	   assertTrue(orderCancelled);
	   assertContains(resp3, "cancelled");
	}

}
