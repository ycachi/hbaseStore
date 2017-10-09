package com.asiainfo.config;

import java.util.Collection;
import java.util.List;

import com.asiainfo.base.entity.system.SysRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class MyUserDetail extends User{
	/**  
	* @Title：MyUserDetail.java
	* @Author:
	* @Description:
	* @Date：2017年3月2日下午5:14:16  
	*/
	private String userId;
	private String charactorName;
	private List<SysRole> roleList;
	public MyUserDetail(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		// TODO Auto-generated constructor stub
	}
	public MyUserDetail(String userId,String username, String password,
			Collection<? extends GrantedAuthority> authorities){
		super(username,password,authorities);
		this.userId=userId;
	}
	
	public MyUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities,
			String userId, List<SysRole> roleList, String charactorName) {
		super(username, password, authorities);
		this.userId = userId;
		this.roleList = roleList;
		this.charactorName = charactorName;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<SysRole> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<SysRole> roleList) {
		this.roleList = roleList;
	}
	public String getCharactorName() {
		return charactorName;
	}
	public void setCharactorName(String charactorName) {
		this.charactorName = charactorName;
	}
	
	

}
