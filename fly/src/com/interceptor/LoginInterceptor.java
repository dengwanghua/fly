package com.interceptor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.pojo.JsonInfo;
import com.pojo.User;

public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
			JsonInfo jsonInfo=new JsonInfo();
	        HttpSession session = request.getSession();
	        User user=(User) session.getAttribute("user");
	        if(user!=null){
	            return true;
	        }else{
	        	jsonInfo.setIsLogin(false);
	        	JSONObject json=JSONObject.fromObject(jsonInfo);
	        	response.getWriter().write(json.toString());
	            return false;
	        }

	}

}
