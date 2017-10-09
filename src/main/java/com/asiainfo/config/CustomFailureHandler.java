package com.asiainfo.config;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	/**  
	* @Title：CustomFailureHandler.java
	* @Author:
	* @Description:登录失败时处理逻辑  用法类似CustomSuccessHandel,此处暂不支持 有必要的话实现接口
	* @Date：2017年2月15日下午5:47:02  
	*/
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	

}