package com.asiainfo.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import com.asiainfo.base.entity.system.Menu;
import com.asiainfo.base.entity.system.SysUser;
import com.asiainfo.base.mapper.system.MenuMapper;
import com.asiainfo.common.StringTools;
import com.asiainfo.service.system.MenuService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;


@Transactional
@Service("menuService")
public class MenuServiceImpl implements MenuService
{

	@Autowired
	private MenuMapper menuMapper;

	@Override
	public List<Menu> getDataShowMenuList(SysUser nowUser) {
		// TODO Auto-generated method stub
		return menuMapper.findDataShowList();
	}
	@Override
	public List<Menu> getBackGroundShowMenuList(SysUser currentUser) {
		List<Menu> menuList = null;
		if(currentUser.isAdmin()){
			menuList = menuMapper.findAllList();
		}else{
			menuList = menuMapper.findShowListByUser(currentUser.getId());
		}
		return menuList;
	}
	@Override
	public int update(Menu menu) {
		return menuMapper.update(menu);
	}
	@Override
	public List<Menu> findAllShowList() {
		return menuMapper.findAllShowList();
	}
	@Override
	public List<Menu> findShowListByUser(String id) {
		return menuMapper.findAllShowList();
	}
	@Override
	public int insert(Menu menu) {
		return menuMapper.insert(menu);
	}
	@Override
	public int insertRoleMenu(Menu menu) {
		return menuMapper.insertRoleMenu(menu);
	}
	@Override
	public List<String> selectIcons() {
		return menuMapper.selectIcons();
	}
	@Override
	public PageInfo<Menu> findAllPageList(int pageNumber, int pageSize) {
		List<Menu> menuList = new ArrayList<Menu>();
		PageHelper.startPage(pageNumber, pageSize);
		menuList = menuMapper.findAllList();
		PageInfo<Menu> pageInfo = new PageInfo<Menu>(menuList);
		return pageInfo;
	}
	public List<Menu> findAllList(){
		return menuMapper.findAllList();
	}
	@Override
	public Menu selectMenu(Menu menu) {
		return menuMapper.selectMenu(menu);
	}
	@Override
	public int insertOrUpdateMenu(Menu menu) {
		if(StringTools.isValid(menu.getId())){
            menuMapper.update(menu);
        }else{
        	menu.preInsert();
            menuMapper.insert(menu);
            menuMapper.insertRoleMenu(menu);
        }
		return 1;
	}

}
