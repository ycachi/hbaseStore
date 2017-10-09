package com.asiainfo.service.system;

import com.asiainfo.base.entity.system.SysUser;

import java.util.List;
import java.util.Map;


public interface UserService {
	/**
	 * 1. paran:map   id    
	 * 2. param:map   username
	 * 3. param:null  return all
	*/
	public SysUser findByMap(Map<String, Object> map) throws Exception;
	public int insertUser(SysUser user);
	int insertUserRole(SysUser user);
    public int updateUser(SysUser user);
    public int deleteById(String id);
    int deleteUserRole(String id);
    public List<SysUser> findByUserName(String username);
    List<SysUser> findAllList();
    public int updateUserAndRole(SysUser user);
    public int insertUserAndRole(SysUser user);
    public SysUser queryById(String id) ;
    public int deleteUserWithoutRole();
}
