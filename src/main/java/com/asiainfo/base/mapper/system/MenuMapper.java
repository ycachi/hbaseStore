package com.asiainfo.base.mapper.system;

import com.asiainfo.base.entity.system.Menu;

import java.util.List;




public interface MenuMapper {

	List<Menu> findAllList();
	
	List<Menu> findDataShowList();
	
    /**
     * 根据id查询权限信息
     * @param id
     * @return
     */
    Menu getMenu(String id);
    /**
     * 根据条件查询权限信息
     * @param menu
     * @return 
     */
    Menu selectMenu(Menu menu);
    /**
     * 更新权限信息
     * @param menu
     */
    int update(Menu menu);
    /**
     * 查询所有展示权限
     * @return
     */
    List<Menu> findAllShowList();

    /**
     * 根据用户查询相应的展示权限
     * @param id
     * @return
     */
    List<Menu> findShowListByUser(String id);
    /**
     * 新增权限
     * @param menu
     */
    int insert(Menu menu);

    /**
     * 给系统管理员添加权限
     * @param menu
     */
    int insertRoleMenu(Menu menu);

    /**
     * 查询已使用的图标
     */
    List<String> selectIcons();
}
