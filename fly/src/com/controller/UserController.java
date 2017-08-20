package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;






import com.pojo.JsonInfo;
import com.pojo.User;
import com.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@RequestMapping(value="/register.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo register(User user,HttpServletResponse response) throws IOException{
		JsonInfo jsoninfo=new JsonInfo();
		User usr=userService.hasUser(user);
		if(usr!=null){
			jsoninfo.setState("2");
			
		}else{
			int result=userService.registerUser(user);
			if(result>0){
				jsoninfo.setState("1");
			}else{
				jsoninfo.setState("0");
			}
		}
		//response.sendRedirect(jsoninfo.getUrl());
		return jsoninfo;
	}
	@RequestMapping(value="/login.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo login(@RequestBody User user,HttpServletRequest request) throws IOException{
		JsonInfo jsoninfo=new JsonInfo();
		User usr=userService.loginUser(user);
		if(usr!=null){
			HttpSession session=request.getSession();
			session.setAttribute("user",usr);
			jsoninfo.setState("1");
			
		}else{
			jsoninfo.setState("0");
		}
		
		return jsoninfo;
	}
	
	@RequestMapping(value="/userinfo.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo userInfo(HttpServletRequest request) throws IOException{
		JsonInfo jsoninfo=new JsonInfo();
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		if(user==null){
			jsoninfo.setState("0");
			return jsoninfo;
		}else{
			JSONObject jsonUser = JSONObject.fromObject(user);
			jsoninfo.setState("1");
			jsoninfo.setData(jsonUser.toString());
			return jsoninfo;
		}	
	}
	@RequestMapping(value="/updateinfo.action",method=RequestMethod.POST)
	public @ResponseBody JsonInfo updateinfo(HttpServletRequest request,String oldpw,String newpw) throws IOException{
		JsonInfo jsoninfo=new JsonInfo();
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		if(user==null){
			jsoninfo.setState("0");
			return jsoninfo;
		}else if(!oldpw.equals(user.getPassword())){
			jsoninfo.setState("1");
			
			return jsoninfo;	
	    }else{
			User usr=new User();
			usr.setUsername(user.getUsername());
			usr.setPassword(newpw);
			int result=userService.updatePassword(usr);
			if(result>0){
				user.setPassword(newpw);
				session.setAttribute("user",user);
				jsoninfo.setState("3");
				
				return jsoninfo;
			}else{
					jsoninfo.setState("2");
					return jsoninfo;
			}
			
	    }
	}
}
