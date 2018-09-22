package com.ibm.watson.developer_cloud.assistant_tester.orchestrator_sample;

public interface IOrderManagement {

	public String getLastOrder(String memberId);
	public void cancelOrder(String orderId);
}
