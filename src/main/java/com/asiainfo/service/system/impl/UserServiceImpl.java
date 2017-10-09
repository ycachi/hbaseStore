package com.asiainfo.service.system.impl;

import java.util.List;
import java.util.Map;

import com.asiainfo.base.entity.system.SysUser;
import com.asiainfo.base.mapper.system.UserMapper;
import com.asiainfo.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Transactional
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserMapper userMapper;
	@Override
	public SysUser findByMap(Map<String,Object> map) throws Exception {
		return userMapper.findByMap(map);
	}
	@Override
	public int insertUser(SysUser user) {
		return userMapper.insertUser(user);
	}
	@Override
	public int updateUser(SysUser user) {
		return userMapper.updateUser(user);
	}
	@Override
	public int deleteById(String id) {
		return userMapper.deleteById(id);
	}
	@Override
	public SysUser queryById(String id) {
		return userMapper.queryById(id);
	}
	public List<SysUser> findByUserName(String username){
		return userMapper.findByUserName(username);
	}
	public List<SysUser> findAllList(){
		return userMapper.findAllList();
	}
	public int deleteUserRole(String id){
		return userMapper.deleteUserRole(id);
	}
	public int insertUserRole(SysUser user){
		return userMapper.insertUserRole(user);
	}
	@Override
	public int updateUserAndRole(SysUser user)
	{
		//删除老的角色关系
		deleteUserRole(user.getId());
		return updateUser(user) + insertUserRole(user);
		
	}
	@Override
	public int insertUserAndRole(SysUser user)
	{
		return insertUser(user) + insertUserRole(user);
	}
	@Override
	public int deleteUserWithoutRole() {
		// TODO Auto-generated method stub
		return userMapper.deleteUserWithoutRole();
	}
	
}
