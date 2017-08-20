package com.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.FlyMapper;
import com.pojo.Fly;
import com.pojo.User;

@Service
public class FlyServiceImpl implements FlyService {

	@Autowired
	private FlyMapper flyMapper;

	@Override
	public List<Fly> flyWhole() {
		// TODO Auto-generated method stub
		return flyMapper.flyWhole();
	}

	@Override
	public List<String> flyAllName() {
		// TODO Auto-generated method stub
		return flyMapper.flyAllName();
	}
	
	@Override
	public List<Map<String, Object>> flyRelationship(String flyname) {
		// TODO Auto-generated method stub
		return flyMapper.flyRelationship(flyname);
	}

	@Override
	public List<Map<String, Object>> wholeday() {
		// TODO Auto-generated method stub
		return flyMapper.wholeday();
	}

	@Override
	public List<Map<String, Object>> flyDay(String flyname) {
		// TODO Auto-generated method stub
		return flyMapper.flyDay(flyname);
	}

	@Override
	public List<Map<String, Object>> dayFly(int day) {
		// TODO Auto-generated method stub
		return flyMapper.dayFly(day);
	}

	@Override
	public List<Map<String, Object>> flyActive() {
		// TODO Auto-generated method stub
		return flyMapper.flyActive();
	}

	@Override
	public List<Map<String, Object>> flyMarket(String flyname) {
		// TODO Auto-generated method stub
		return flyMapper.flyMarket(flyname);
	}

	@Override
	public List<Map<String, Object>> flyPredict(String flyname) {
		// TODO Auto-generated method stub
		return flyMapper.flyPredict(flyname);
	}

	@Override
	public List<Map<String, Object>> flyOriginal(String flyname) {
		// TODO Auto-generated method stub
		return flyMapper.flyOriginal(flyname);
	}

	
	
	
	
	
}
