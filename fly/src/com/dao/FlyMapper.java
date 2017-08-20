package com.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pojo.Fly;

public interface FlyMapper {
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
