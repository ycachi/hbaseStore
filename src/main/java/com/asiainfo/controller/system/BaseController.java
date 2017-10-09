package com.asiainfo.controller.system;

import com.asiainfo.base.entity.system.SysUser;

import javax.servlet.http.HttpServletRequest;



public class BaseController {
	/**
	 * 获取Session中保存的当前User Info
	*/
	public SysUser getCurrentUser(HttpServletRequest request){
		SysUser currentUser= (SysUser)request.getSession().getAttribute("currentUser");
		if(currentUser==null){
			currentUser = new SysUser();
		}
		return currentUser;
	}
}
