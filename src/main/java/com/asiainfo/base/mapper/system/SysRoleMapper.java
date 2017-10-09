package com.asiainfo.base.mapper.system;

import java.util.List;
import java.util.Map;

import com.asiainfo.base.entity.system.Menu;
import com.asiainfo.base.entity.system.SysRole;
import org.apache.ibatis.annotations.Param;




public interface SysRoleMapper {
    public int insertRole(SysRole item);
    public int updateRole(SysRole item);
    public int deleteRoleMenu(String id);
    public int deleteRole(String id);
    public int deleteRoleUser(String id);
    public List<SysRole> selectByMap(Map<String, Object> map);
    public SysRole getById(String id);
    void insertRoleMenu(@Param("id") String id, @Param("menuList") List<Menu> menuList);
    /**
	 * 获取所有角色信息
	 * @return
	 */
	public List<SysRole> findAllList();
}
