package com.asiainfo.base.mapper.system;

import com.asiainfo.base.entity.system.SysUser;

import java.util.List;
import java.util.Map;



public interface UserMapper {
    public SysUser findByMap(Map<String, Object> map);
    public int insertUser(SysUser user);
    int insertUserRole(SysUser user);
    public int updateUser(SysUser user);
    public int deleteById(String id);
    public int deleteUserRole(String id);
    List<SysUser> findAllList();
    public List<SysUser> findByUserName(String username);
    public SysUser queryById(String id);
    public int deleteUserWithoutRole();
}
