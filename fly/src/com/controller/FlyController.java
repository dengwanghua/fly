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
import org.springframework.validation.BindingResult;
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

import com.dao.AgentMapper;
import com.pojo.Fly;
import com.pojo.JsonInfo;
import com.pojo.User;
import com.service.AgentService;
import com.service.FlyService;
import com.service.UserService;

@Controller
public class FlyController {
	@Autowired
	private FlyService flyService;
	@Autowired
	private AgentMapper agentService;
	
	@RequestMapping(value="/flywhole.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo flywhole() throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.get("flywhole");
		if(resultJson==null){
			List<Fly> infoList=flyService.flyWhole();
			float ratesum=0;
			List<String>  flyList=new ArrayList<String>();
			List<Integer> cntList=new ArrayList<Integer>();
			List<Double>   roundList=new ArrayList<Double>();
			List<Map<String,Object>>   rateList=new ArrayList<Map<String,Object>>();
			for(int i=0;i<infoList.size();i++){
				Fly fly=infoList.get(i);
				flyList.add(fly.getSale_nbr());
				cntList.add(fly.getCnt());
				roundList.add(fly.getRound()/10000);
				if(i<5){
					Map<String,Object> topfive=new HashMap<String,Object>();
					ratesum+=fly.getCnt_rate()*100;
					topfive.put("value", fly.getCnt_rate()*100);
					topfive.put("name", fly.getSale_nbr());
					rateList.add(topfive);
				}
			}
			Map<String,Object> topfive=new HashMap<String,Object>();
			topfive.put("value", 100-ratesum);
			topfive.put("name", "其它");
			rateList.add(topfive);
			resultJson="";
			resultJson+="{\"count\":"+infoList.size()+",\"topfive\":"+JSONArray.fromObject(rateList)+",\"infolist\":"+JSONArray.fromObject(infoList)+",\"flylist\":"+JSONArray.fromObject(flyList)+",\"cntlist\":"+JSONArray.fromObject(cntList)+",\"roundlist\":"+JSONArray.fromObject(roundList)+"}";
			jedis.set("flywhole", resultJson);
			jedis.close();
		}
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/flyrelationship.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo flyrelationship(String flyname) throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.hget("flyrelationship", flyname);
		if(resultJson==null){
			List<String> selectList=flyService.flyAllName();
			List<Map<String, Object>> infoList=flyService.flyRelationship(flyname);
			List<String> nameList=new ArrayList<String>();
			List<Long> cntList=new ArrayList<Long>();
			List<Double> roundList=new ArrayList<Double>();
			for(int i=0;i<infoList.size();i++){
				Map<String, Object> map=infoList.get(i);
				nameList.add((String)map.get("buy_nbr"));
				cntList.add((Long) map.get("cnt"));
				roundList.add((Double) map.get("round")/10000);
			}
			resultJson="";
			resultJson+="{\"selectlist\":"+JSONArray.fromObject(selectList)+",\"namelist\":"+JSONArray.fromObject(nameList)+",\"cntlist\":"+JSONArray.fromObject(cntList)+",\"roundlist\":"+JSONArray.fromObject(roundList)+"}";
			jedis.hset("flyrelationship", flyname, resultJson);
			jedis.close();
		}
			jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/wholeday.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo wholeday() throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.get("wholeday");
		if(resultJson==null){
			List<Map<String, Object>> infoList=flyService.wholeday();
			List<Integer> dayList=new ArrayList<Integer>();
			List<Long> flyList=new ArrayList<Long>();
			List<Long> saleList=new ArrayList<Long>();
			List<Long> buyList=new ArrayList<Long>();
			List<Long> travellList=new ArrayList<Long>();
			for(int i=0;i<infoList.size();i++){
				Map<String, Object> map=infoList.get(i);
				dayList.add((Integer) map.get("day_id"));
				flyList.add((Long) map.get("flycnt"));
				saleList.add((Long) map.get("agentsalecnt"));
				buyList.add((Long) map.get("agentbuycnt"));
				travellList.add((Long) map.get("travellercnt"));
			}
			resultJson="";
			resultJson+="{\"daylist\":"+JSONArray.fromObject(dayList)+",\"flylist\":"+JSONArray.fromObject(flyList)+",\"salelist\":"+JSONArray.fromObject(saleList)+",\"buylist\":"+JSONArray.fromObject(buyList)+",\"travelllist\":"+JSONArray.fromObject(travellList)+"}";
			jedis.set("wholeday", resultJson);
			jedis.close();
		}
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}	
	@RequestMapping(value="/flyday.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo flyday(String flyname) throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.hget("flyday", flyname);
		if(resultJson==null){
			List<String> selectList=flyService.flyAllName();
			List<Map<String, Object>> infoList=flyService.flyDay(flyname);
			List<Integer> dayList=new ArrayList<Integer>();
			List<Long> cntList=new ArrayList<Long>();
			List<Double> roundList=new ArrayList<Double>();
			for(int i=0;i<infoList.size();i++){
				Map<String, Object> map=infoList.get(i);
				dayList.add((Integer) map.get("day_id"));
				cntList.add((Long) map.get("cnt"));
				roundList.add((Double) map.get("round")/10000);
			}
			resultJson="";
			resultJson+="{\"selectlist\":"+JSONArray.fromObject(selectList)+",\"daylist\":"+JSONArray.fromObject(dayList)+",\"cntlist\":"+JSONArray.fromObject(cntList)+",\"roundlist\":"+JSONArray.fromObject(roundList)+"}";
			jedis.hset("flyday", flyname, resultJson);
			jedis.close();
		}
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/dayfly.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo dayfly(int day) throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.hget("dayfly", String.valueOf(day));
		if(resultJson==null){
			List<Map<String, Object>> infoList=flyService.dayFly(day);
			List<String> flyList=new ArrayList<String>();
			List<Long> cntList=new ArrayList<Long>();
			List<Double> roundList=new ArrayList<Double>();
			for(int i=0;i<infoList.size();i++){
				Map<String, Object> map=infoList.get(i);
				flyList.add((String) map.get("sale_nbr"));
				cntList.add((Long) map.get("cnt"));
				roundList.add((Double) map.get("round")/10000);
			}
			resultJson="";
			resultJson+="{\"flylist\":"+JSONArray.fromObject(flyList)+",\"cntlist\":"+JSONArray.fromObject(cntList)+",\"roundlist\":"+JSONArray.fromObject(roundList)+"}";
			jedis.hset("dayfly",  String.valueOf(day), resultJson);
			jedis.close();
		}
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/active.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo active() throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.get("active");
		if(resultJson==null){
			List<Map<String, Object>> flyInfoList=flyService.flyActive();
			List<Map<String, Object>> agentInfoList=agentService.agentActive();
			List<String> flyList=new ArrayList<String>();
			List<Integer> flyActiveList=new ArrayList<Integer>();
			List<String> agentList=new ArrayList<String>();
			List<Integer> agentActiveList=new ArrayList<Integer>();
			for(int i=0;i<flyInfoList.size();i++){
				Map<String, Object> map=flyInfoList.get(i);
				flyList.add((String) map.get("airname"));
				flyActiveList.add((Integer) map.get("active"));
			}
			for(int i=0;i<agentInfoList.size();i++){
				Map<String, Object> map=agentInfoList.get(i);
				agentList.add((String) map.get("agentname"));
				agentActiveList.add((Integer) map.get("active"));
			}
			resultJson="";
			resultJson+="{\"flylist\":"+JSONArray.fromObject(flyList)+",\"flyactivelist\":"+JSONArray.fromObject(flyActiveList)+",\"agentlist\":"+JSONArray.fromObject(agentList)+",\"agentactivelist\":"+JSONArray.fromObject(agentActiveList)+"}";
			jedis.set("active",resultJson);
			jedis.close();
		}
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/market.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo market(String flyname,String agentname) throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		List<String> selectFlyList=flyService.flyAllName();
		List<String> selectAgentList=agentService.agentAllName();
		List<Map<String, Object>> flyInfoList=flyService.flyMarket(flyname);
		List<Map<String, Object>> agentInfoList=agentService.agentMarket(agentname);
		List<Integer> flyDayList=new ArrayList<Integer>();
		List<Float> flyMarketList=new ArrayList<Float>();
		List<Integer> agentDayList=new ArrayList<Integer>();
		List<Float> agentMarketList=new ArrayList<Float>();
		for(int i=0;i<flyInfoList.size();i++){
			Map<String, Object> map=flyInfoList.get(i);
			flyDayList.add((Integer) map.get("day_id"));
			flyMarketList.add((Float) map.get("cnt_rate")*100);
		}
		for(int i=0;i<agentInfoList.size();i++){
			Map<String, Object> map=agentInfoList.get(i);
			agentDayList.add((Integer) map.get("day_id"));
			agentMarketList.add((Float) map.get("sale_cnt_persentage")*100);
		}
		String resultJson="";
		resultJson+="{\"selectflylist\":"+JSONArray.fromObject(selectFlyList)+",\"selectagentlist\":"+JSONArray.fromObject(selectAgentList)+",\"flydaylist\":"+JSONArray.fromObject(flyDayList)+",\"flymarketlist\":"+JSONArray.fromObject(flyMarketList)+",\"agentdaylist\":"+JSONArray.fromObject(agentDayList)+",\"agentmarketlist\":"+JSONArray.fromObject(agentMarketList)+"}";
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
	@RequestMapping(value="/flypredict.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo flypredict(String flyname) throws IOException{
		JsonInfo jsonInfo=new JsonInfo();
		Jedis jedis=JedisPoolUtils.getJedis();
		String resultJson=jedis.hget("flypredict", flyname);
		if(resultJson==null){
			List<String> selectList=flyService.flyAllName();
			List<Map<String, Object>> flyOriginalList=flyService.flyOriginal(flyname);
			List<Map<String, Object>> flyInfoList=flyService.flyPredict(flyname);
			List<Integer> dayList=new ArrayList<Integer>();
			List<Long> cntList=new ArrayList<Long>();
			List<Double> roundList=new ArrayList<Double>();
			for(int i=0;i<flyInfoList.size();i++){
				Map<String, Object> map=flyInfoList.get(i);
				dayList.add((Integer) map.get("day_id"));
				cntList.add((Long) map.get("cnt_sum"));
				roundList.add((Double) map.get("round_sum")/10000);
			}
			List<Long> cntOriginalList=new ArrayList<Long>();
			List<Double> roundOriginalList=new ArrayList<Double>();
			for(int i=0;i<flyOriginalList.size();i++){
				Map<String, Object> map=flyOriginalList.get(i);
				cntOriginalList.add((Long) map.get("cnt"));
				roundOriginalList.add((Double) map.get("round")/10000);
			}
			resultJson="";
			resultJson+="{\"selectlist\":"+JSONArray.fromObject(selectList)+",\"daylist\":"+JSONArray.fromObject(dayList)+",\"cntlist\":"+JSONArray.fromObject(cntList)+",\"roundlist\":"+JSONArray.fromObject(roundList)+",\"cntoriginallist\":"+JSONArray.fromObject(cntOriginalList)+",\"roundoriginallist\":"+JSONArray.fromObject(roundOriginalList)+"}";
			
			jedis.hset("flypredict",flyname, resultJson);
			jedis.close();
		}
		jsonInfo.setData(resultJson);
		return jsonInfo;
	}
}
