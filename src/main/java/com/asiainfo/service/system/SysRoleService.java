package com.asiainfo.service.system;

import com.asiainfo.base.entity.system.SysRole;

import java.util.List;
import java.util.Map;


public interface SysRoleService {
	public int insertRole(SysRole item);
    public int updateRole(SysRole item);
    public int deleteRoleMenu(String id);
    /**
     * the result will be all when param map is null
    */
    public List<SysRole> selectByMap(Map<String, Object> map);
    public SysRole selectById(String id);
    public List<SysRole> findAllRole();
    public int deleteRole(String id);
    public int deleteRoleUser(String id);
}
