package com.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.AgentMapper;
import com.pojo.Agent;
import com.pojo.User;

@Service
public class AgentServiceImpl implements AgentService {
	@Autowired
	private AgentMapper agentMapper;

	@Override
	public List<Agent> agentWhole() {
		// TODO Auto-generated method stub
		return agentMapper.agentWhole();
	}

	@Override
	public List<String> agentAllName() {
		// TODO Auto-generated method stub
		return agentMapper.agentAllName();
	}
	
	@Override
	public List<Map<String, Object>> agentSaleRelationship(String agentname) {
		// TODO Auto-generated method stub
		return agentMapper.agentSaleRelationship(agentname);
	}

	@Override
	public List<Map<String, Object>> agentBuyRelationship(String agentname) {
		// TODO Auto-generated method stub
		return agentMapper.agentBuyRelationship(agentname);
	}

	@Override
	public List<Map<String, Object>> agentDay(String agentname) {
		// TODO Auto-generated method stub
		return agentMapper.agentDay(agentname);
	}

	@Override
	public List<Map<String, Object>> dayAgent(int day) {
		// TODO Auto-generated method stub
		return agentMapper.dayAgent(day);
	}

	@Override
	public List<Map<String, Object>> saleCapability(String agentname) {
		// TODO Auto-generated method stub
		return agentMapper.saleCapability(agentname);
	}

	@Override
	public List<Map<String, Object>> fundCapability(String agentname) {
		// TODO Auto-generated method stub
		return agentMapper.fundCapability(agentname);
	}

	@Override
	public List<Map<String, Object>> agentActive() {
		// TODO Auto-generated method stub
		return agentMapper.agentActive();
	}

	@Override
	public List<Map<String, Object>> agentMarket(String agentname) {
		// TODO Auto-generated method stub
		return agentMapper.agentMarket(agentname);
	}

	@Override
	public List<Map<String, Object>> agentSalePredict(String agentname) {
		// TODO Auto-generated method stub
		return agentMapper.agentSalePredict(agentname);
	}

	@Override
	public List<Map<String, Object>> agentBuyPredict(String agentname) {
		// TODO Auto-generated method stub
		return agentMapper.agentBuyPredict(agentname);
	}

	@Override
	public List<Map<String, Object>> agentOriginal(String agentname) {
		// TODO Auto-generated method stub
		return agentMapper.agentOriginal(agentname);
	}

}
