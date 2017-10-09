package com.asiainfo.service.system.impl;

import java.util.List;
import java.util.Map;

import com.asiainfo.base.entity.system.SysRole;
import com.asiainfo.base.mapper.system.SysRoleMapper;
import com.asiainfo.common.StringTools;
import com.asiainfo.service.system.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleMapper sysRoleMapper;

	@Override
	public int insertRole(SysRole item) {
		// 判断是更新还是新增
		if (!StringTools.isValid(item.getId())) {
			item.preInsert();
			sysRoleMapper.insertRole(item);
			if(item.getMenuList().size() > 0 ){
				sysRoleMapper.insertRoleMenu(item.getId(), item.getMenuList());
			}
		} else {
			// 更新角色信息
			sysRoleMapper.updateRole(item);
			// 清除现有菜单权限后重新插入
			sysRoleMapper.deleteRoleMenu(item.getId());
			if(item.getMenuList().size() > 0 )
			{
				sysRoleMapper.insertRoleMenu(item.getId(), item.getMenuList());
			}
		}
		return 0;
	}

	@Override
	public int updateRole(SysRole item) {
		return sysRoleMapper.updateRole(item);
	}

	@Override
	public int deleteRoleMenu(String id) {
		return sysRoleMapper.deleteRoleMenu(id);
	}

	@Override
	public List<SysRole> selectByMap(Map<String, Object> map) {
		return sysRoleMapper.selectByMap(map);
	}
	public SysRole selectById(String id){
		return sysRoleMapper.getById(id);
	}
	public List<SysRole> findAllRole(){
		return sysRoleMapper.findAllList();
	}

	@Override
	public int deleteRole(String id) {
		// TODO Auto-generated method stub
		return sysRoleMapper.deleteRole(id);
	}

	@Override
	public int deleteRoleUser(String id) {
		// TODO Auto-generated method stub
		return sysRoleMapper.deleteRoleUser(id);
	}
}
