package com.asiainfo.config;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.asiainfo.base.entity.system.SysUser;
import com.asiainfo.common.StringTools;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

public class SessionStatusInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		SysUser currentUser = (SysUser)request.getSession().getAttribute("currentUser");
		String requestType = request.getHeader("X-Requested-With");
		JSONObject result = new JSONObject();
		if(currentUser==null && StringTools.isValid(requestType) && requestType.equalsIgnoreCase("XMLHttpRequest")){
			result.put("sessionStatus", 540);
			sendJSONMsg(response, result.toString());
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	public void sendJSONMsg(HttpServletResponse response, String json){
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
	    try {
	      PrintWriter out = response.getWriter();
	      out.write(json);
	      out.flush();
	      out.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
}
