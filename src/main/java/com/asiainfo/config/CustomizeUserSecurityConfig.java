package com.asiainfo.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asiainfo.base.entity.system.SysRole;
import com.asiainfo.base.entity.system.SysUser;
import com.asiainfo.common.DateUtil;
import com.asiainfo.service.system.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;



@Component
public class CustomizeUserSecurityConfig implements UserDetailsService{
	/**  
	* @Title：CustomizeUserSecurityConfig.java
	* @Author:
	* @Description:登录验证逻辑，核心模块
	* @Date：2017年2月15日下午5:47:02  
	*/
	private Logger logger = Logger.getLogger(CustomizeUserSecurityConfig.class);
	@Autowired
	private UserService userService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser user = null;
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("username", username);
		try {
			user = userService.findByMap(map);
			if(user != null){
				//用于添加用户的权限。只要把用户权限添加到authorities 就万事大吉。
				for(SysRole role:user.getRoles())
		        {
		            authorities.add(new SimpleGrantedAuthority(role.getName()));
		        }
				logger.info(DateUtil.formatDate(new Date(), "yyyy:MM:dd hh:mm:ss")+"#####Info:已获取" + user.getUsername()+"实例！");
	        }else{
	        	//throw new UsernameNotFoundException("用户" + username + " 不存在");
	        	logger.info(DateUtil.formatDate(new Date(), "yyyy:MM:dd hh:mm:ss")+"#####Info:用户" + username + " 不存在");
	        	return new org.springframework.security.core.userdetails.User("",
	                    "", authorities);
	        } 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new MyUserDetail(user.getUsername(), user.getPassword(), authorities, user.getId(), user.getRoles(), user.getCharacterName());
	}
	
}
