package com.service;

import java.util.List;
import java.util.Map;

import com.pojo.Fly;
import com.pojo.User;

public interface FlyService {
	List<Fly> flyWhole();
	List<String> flyAllName();
	List<Map<String, Object>> flyRelationship(String flyname);
	List<Map<String, Object>> wholeday();
	List<Map<String, Object>> flyDay(String flyname);
	List<Map<String, Object>> dayFly(int day);
	List<Map<String, Object>> flyActive();
	List<Map<String, Object>> flyMarket(String flyname);
	List<Map<String, Object>> flyPredict(String flyname);
	List<Map<String, Object>> flyOriginal(String flyname);
}
