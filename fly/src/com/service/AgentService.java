package com.service;

import java.util.List;
import java.util.Map;

import com.pojo.Agent;

public interface AgentService {
	List<Agent> agentWhole();
	List<String> agentAllName();
	List<Map<String, Object>>agentSaleRelationship(String agentname);
	List<Map<String, Object>>agentBuyRelationship(String agentname);
	List<Map<String, Object>>agentDay(String agentname);
	List<Map<String, Object>>dayAgent(int day);
	List<Map<String, Object>>saleCapability(String agentname);
	List<Map<String, Object>>fundCapability(String agentname);
	List<Map<String, Object>>agentActive();
	List<Map<String, Object>>agentMarket(String agentname);
	List<Map<String, Object>>agentSalePredict(String agentname);
	List<Map<String, Object>>agentBuyPredict(String agentname);
	List<Map<String, Object>>agentOriginal(String agentname);
}
