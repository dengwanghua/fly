package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;






























import redis.JedisPoolUtils;
import redis.clients.jedis.Jedis;

import com.pojo.Agent;
import com.pojo.Fly;
import com.pojo.JsonInfo;
import com.pojo.User;
import com.service.AgentService;
import com.service.UserService;

@Controller
public class AgentController {
	@Autowired
	private AgentService agentService;
	@RequestMapping(value="/agentwhole.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo agentWhole() throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.get("agentwhole");
		if(resultJson==null){
			List<Agent> infoList=agentService.agentWhole();
			float saleratesum=0;
			float buyratesum=0;
			List<String>  agentList=new ArrayList<String>();
			List<Integer> salecntList=new ArrayList<Integer>();
			List<Double>   saleroundList=new ArrayList<Double>();
			List<Integer> buycntList=new ArrayList<Integer>();
			List<Double>   buyroundList=new ArrayList<Double>();
			List<Map<String,Object>>   salerateList=new ArrayList<Map<String,Object>>();
			List<Map<String,Object>>   buyrateList=new ArrayList<Map<String,Object>>();
			for(int i=0;i<infoList.size();i++){
				Agent agent=infoList.get(i);
				agentList.add(agent.getProxy());
				salecntList.add(agent.getSale_cnt());
				saleroundList.add(agent.getSale_round()/10000);
				buycntList.add(agent.getBuy_cnt());
				buyroundList.add(agent.getBuy_round()/10000);
				if(i<5){
					Map<String,Object> saletopfive=new HashMap<String,Object>();
					Map<String,Object> buytopfive=new HashMap<String,Object>();
					saleratesum+=agent.getSale_cnt_rate()*100;
					buyratesum+=agent.getBuy_cnt_rate()*100;
					saletopfive.put("value", agent.getSale_cnt_rate()*100);
					saletopfive.put("name", agent.getProxy());
					salerateList.add(saletopfive);
					buytopfive.put("value", agent.getBuy_cnt_rate()*100);
					buytopfive.put("name", agent.getProxy());
					buyrateList.add(buytopfive);
					
				}
			}
			
			Map<String,Object> saletopfive=new HashMap<String,Object>();
			saletopfive.put("value", 100-saleratesum);
			saletopfive.put("name", "其它");
			salerateList.add(saletopfive);
			Map<String,Object> buytopfive=new HashMap<String,Object>();
			buytopfive.put("value", 100-buyratesum);
			buytopfive.put("name", "其它");
			buyrateList.add(buytopfive);
			resultJson="";
			resultJson+="{\"count\":"+infoList.size()+",\"infolist\":"+JSONArray.fromObject(infoList)+",\"saletopfive\":"+JSONArray.fromObject(salerateList)+",\"buytopfive\":"+JSONArray.fromObject(buyrateList)+",\"agentlist\":"+JSONArray.fromObject(agentList)+",\"salecntlist\":"+JSONArray.fromObject(salecntList)+",\"saleroundlist\":"+JSONArray.fromObject(saleroundList)+",\"buycntlist\":"+JSONArray.fromObject(buycntList)+",\"buyroundlist\":"+JSONArray.fromObject(buyroundList)+"}";
			jedis.set("agentwhole", resultJson);
			jedis.close();
		}
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/agentrelationship.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo agentrelationship(String agentname) throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.hget("agentrelationship", agentname);
		if(resultJson==null){
			List<String> selectList=agentService.agentAllName();
			List<Map<String, Object>> saleinfoList=agentService.agentSaleRelationship(agentname);
			List<Map<String, Object>> buyinfoList=agentService.agentBuyRelationship(agentname);
			
			List<String> saleNameList=new ArrayList<String>();
			List<Long> saleCntList=new ArrayList<Long>();
			List<Double> saleRoundList=new ArrayList<Double>();
			for(int i=0;i<saleinfoList.size();i++){
				Map<String, Object> map=saleinfoList.get(i);
				saleNameList.add((String)map.get("buy_nbr"));
				saleCntList.add((Long) map.get("cnt"));
				saleRoundList.add((Double) map.get("round")/10000);
			}
			
			
			List<String> buyNameList=new ArrayList<String>();
			List<Long> buyCntList=new ArrayList<Long>();
			List<Double> buyRoundList=new ArrayList<Double>();
			for(int i=0;i<buyinfoList.size();i++){
				Map<String, Object> map=buyinfoList.get(i);
				buyNameList.add((String)map.get("sale_nbr"));
				buyCntList.add((Long) map.get("cnt"));
				buyRoundList.add((Double) map.get("round")/10000);
			}
			
			resultJson="";
			resultJson+="{\"selectlist\":"+JSONArray.fromObject(selectList)+",\"salenamelist\":"+JSONArray.fromObject(saleNameList)+",\"salecntlist\":"+JSONArray.fromObject(saleCntList)+",\"saleroundlist\":"+JSONArray.fromObject(saleRoundList)+",\"buynamelist\":"+JSONArray.fromObject(buyNameList)+",\"buycntlist\":"+JSONArray.fromObject(buyCntList)+",\"buyroundlist\":"+JSONArray.fromObject(buyRoundList)+"}";
			jedis.hset("agentrelationship", agentname, resultJson);
			jedis.close();
		}
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/agentday.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo agentday(String agentname) throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.hget("agentday", agentname);
		if(resultJson==null){
			List<String> selectList=agentService.agentAllName();
			List<Map<String, Object>> infoList=agentService.agentDay(agentname);
			List<Integer> dayList=new ArrayList<Integer>();
			List<Long> saleCntList=new ArrayList<Long>();
			List<Double> saleRoundList=new ArrayList<Double>();
			List<Long> buyCntList=new ArrayList<Long>();
			List<Double> buyRoundList=new ArrayList<Double>();
			for(int i=0;i<infoList.size();i++){
				Map<String, Object> map=infoList.get(i);
				dayList.add((Integer) map.get("day_id"));
				saleCntList.add((Long) map.get("sale_cnt"));
				saleRoundList.add((Double) map.get("sale_round")/10000);
				buyCntList.add((Long) map.get("buy_cnt"));
				buyRoundList.add((Double) map.get("buy_round")/10000);
			}
			resultJson="";
			resultJson+="{\"selectlist\":"+JSONArray.fromObject(selectList)+",\"daylist\":"+JSONArray.fromObject(dayList)+",\"salecntlist\":"+JSONArray.fromObject(saleCntList)+",\"saleroundlist\":"+JSONArray.fromObject(saleRoundList)+",\"buycntlist\":"+JSONArray.fromObject(buyCntList)+",\"buyroundlist\":"+JSONArray.fromObject(buyRoundList)+"}";
			jedis.hset("agentday", agentname, resultJson);
			jedis.close();
		}
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/dayagent.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo dayagent(int day) throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.hget("dayagent", String.valueOf(day));
		if(resultJson==null){
			List<Map<String, Object>> infoList=agentService.dayAgent(day);
			List<String> agentList=new ArrayList<String>();
			List<Long> saleCntList=new ArrayList<Long>();
			List<Double> saleRoundList=new ArrayList<Double>();
			List<Long> buyCntList=new ArrayList<Long>();
			List<Double> buyRoundList=new ArrayList<Double>();
			for(int i=0;i<infoList.size();i++){
				Map<String, Object> map=infoList.get(i);
				agentList.add((String) map.get("proxy"));
				saleCntList.add((Long) map.get("sale_cnt"));
				saleRoundList.add((Double) map.get("sale_round")/10000);
				buyCntList.add((Long) map.get("buy_cnt"));
				buyRoundList.add((Double) map.get("buy_round")/10000);
			}
			resultJson="";
			resultJson+="{\"agentlist\":"+JSONArray.fromObject(agentList)+",\"salecntlist\":"+JSONArray.fromObject(saleCntList)+",\"saleroundlist\":"+JSONArray.fromObject(saleRoundList)+",\"buycntlist\":"+JSONArray.fromObject(buyCntList)+",\"buyroundlist\":"+JSONArray.fromObject(buyRoundList)+"}";
			jedis.hset("dayagent", String.valueOf(day), resultJson);
			jedis.close();
		}
		
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/salecapability.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo salecapability(String agentname) throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.hget("salecapability", agentname);
		if(resultJson==null){
			List<String> selectList=agentService.agentAllName();
			List<Map<String, Object>> infoList=agentService.saleCapability(agentname);
			List<Integer> dayList=new ArrayList<Integer>();
			List<Long> saleCntList=new ArrayList<Long>();
			List<Long> buyCntList=new ArrayList<Long>();
			List<Long> differenceList=new ArrayList<Long>();
			for(int i=0;i<infoList.size();i++){
				Map<String, Object> map=infoList.get(i);
				dayList.add((Integer) map.get("day_id"));
				saleCntList.add((Long) map.get("sale_cnt"));
				buyCntList.add((Long) map.get("buy_cnt"));
				differenceList.add((Long) map.get("cnt_difference"));
			}
			resultJson="";
			resultJson+="{\"selectlist\":"+JSONArray.fromObject(selectList)+",\"daylist\":"+JSONArray.fromObject(dayList)+",\"salecntlist\":"+JSONArray.fromObject(saleCntList)+",\"buycntlist\":"+JSONArray.fromObject(buyCntList)+",\"differencelist\":"+JSONArray.fromObject(differenceList)+"}";
			jedis.hset("salecapability", agentname, resultJson);
			jedis.close();
		}
		
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/fundcapability.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo fundcapability(String agentname) throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.hget("fundcapability", agentname);
		if(resultJson==null){
			List<String> selectList=agentService.agentAllName();
			List<Map<String, Object>> infoList=agentService.fundCapability(agentname);
			List<Integer> dayList=new ArrayList<Integer>();
			List<Double> saleRoundList=new ArrayList<Double>();
			List<Double> buyRoundList=new ArrayList<Double>();
			List<Double> differenceList=new ArrayList<Double>();
			for(int i=0;i<infoList.size();i++){
				Map<String, Object> map=infoList.get(i);
				dayList.add((Integer) map.get("day_id"));
				saleRoundList.add((Double) map.get("sale_round")/10000);
				buyRoundList.add((Double) map.get("buy_round")/10000);
				differenceList.add((Double) map.get("round_difference"));
			}
			resultJson="";
			resultJson+="{\"selectlist\":"+JSONArray.fromObject(selectList)+",\"daylist\":"+JSONArray.fromObject(dayList)+",\"saleroundlist\":"+JSONArray.fromObject(saleRoundList)+",\"buyroundlist\":"+JSONArray.fromObject(buyRoundList)+",\"differencelist\":"+JSONArray.fromObject(differenceList)+"}";
			jedis.hset("fundcapability",agentname, resultJson);
			jedis.close();
		}
		
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/agentpredict.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo agentpredict(String agentname) throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.hget("agentpredict", agentname);
		if(resultJson==null){
			List<String> selectList=agentService.agentAllName();
			List<Map<String, Object>> agentSalePredictList=agentService.agentSalePredict(agentname);
			List<Map<String, Object>> agentBuyPredictList=agentService.agentBuyPredict(agentname);
			List<Integer> dayList=new ArrayList<Integer>();
			
			List<Long> predictSaleCntList=new ArrayList<Long>();
			List<Double> predictSaleRoundList=new ArrayList<Double>();
			for(int i=0;i<agentSalePredictList.size();i++){
				Map<String, Object> map=agentSalePredictList.get(i);
				dayList.add((Integer) map.get("day_id"));
				predictSaleCntList.add((Long) map.get("cnt_sum"));
				predictSaleRoundList.add((Double) map.get("round_sum")/10000);
				
			}
			
			List<Long> predictBuyCntList=new ArrayList<Long>();
			List<Double> predictBuyRoundList=new ArrayList<Double>();
			for(int i=0;i<agentBuyPredictList.size();i++){
				Map<String, Object> map=agentBuyPredictList.get(i);
				predictBuyCntList.add((Long) map.get("cnt_sum"));
				predictBuyRoundList.add((Double) map.get("round_sum")/10000);
				
			}
			
			resultJson="";
			resultJson+="{\"selectlist\":"+JSONArray.fromObject(selectList)+",\"daylist\":"+JSONArray.fromObject(dayList)+",\"predictsalecntlist\":"+JSONArray.fromObject(predictSaleCntList)+",\"predictsaleroundlist\":"+JSONArray.fromObject(predictSaleRoundList)+",\"predictbuycntlist\":"+JSONArray.fromObject(predictBuyCntList)+",\"predictbuyroundlist\":"+JSONArray.fromObject(predictBuyRoundList)+"}";
			jedis.hset("agentpredict",agentname, resultJson);
			jedis.close();
		}
		
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
}
