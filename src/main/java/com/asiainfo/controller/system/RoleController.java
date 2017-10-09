package com.asiainfo.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asiainfo.base.entity.system.Menu;
import com.asiainfo.base.entity.system.SysRole;
import com.asiainfo.base.entity.system.SysUser;
import com.asiainfo.common.StringTools;
import com.asiainfo.service.system.MenuService;
import com.asiainfo.service.system.SysRoleService;
import com.asiainfo.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;



@Controller
public class RoleController {
	@Autowired
    private SysRoleService sysRoleService;
	@Autowired
    private MenuService menuService;
	@Autowired
    private UserService userService;
	
	@RequestMapping("/roleList")
	public String list(SysUser user, ModelMap model) {
		List<SysRole> roles = sysRoleService.findAllRole();
		model.addAttribute("roles", roles);
		return "/system/roleList";
	}
	
    /**
	 * 新增角色
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/newRole")
	public String newRole(Model model) {
		List<Menu> menus = menuService.findAllShowList();
		JSONArray menuJson = new JSONArray();
		for (Menu menu : menus) {
			JSONObject node = new JSONObject();
			node.put("id", menu.getId());
			node.put("pId", menu.getParentId());
			node.put("name", menu.getName());
			node.put("open", true);
			menuJson.add(node);
		}
		model.addAttribute("menus", menuJson.toJSONString());
		model.addAttribute("role", new SysRole());
		return "/system/roleForm";
	}
	
	/**
	 * 编辑角色
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/editRole")
	public String editRole(Model model, String id) {
		SysRole role = sysRoleService.selectById(id);
		List<Menu> menuList = role.getMenuList();
		//只有admin才有权限管理权限
		List<Menu> menus = menuService.findAllShowList();
		JSONArray menuJson = new JSONArray();
		for (Menu menu : menus) {
			JSONObject node = new JSONObject();
			node.put("id", menu.getId());
			node.put("pId", menu.getParentId());
			node.put("name", menu.getName());
			node.put("open", true);
			node.put("checked", menuList.contains(menu));
			menuJson.add(node);
		}
		model.addAttribute("menus", menuJson.toJSONString());
		model.addAttribute("role", role);
		return "/system/roleForm";
	}
	/**
	 * 保存角色
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/saveRole")
	public String saveRole(SysRole role, Model model)  
	{
		String message = "";
		if(!StringTools.isValid(role.getId()))
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name", role.getName());
			List<SysRole> roleList = sysRoleService.selectByMap(map);
			if(roleList != null && !roleList.isEmpty())
			{
				message = "保存角色失败!name 已存在！";
			}
		}
		int result = 1;
		try
		{
			result = sysRoleService.insertRole(role);
		}catch(Exception e)
		{
			model.addAttribute("message", "保存角色失败");
		}
		 
		if(result==0){
			model.addAttribute("message", "保存角色成功");
			return "redirect:/roleList";
		} else {
			message = StringTools.isValid(message) ? message : "保存角色失败";
			model.addAttribute("message", message);
		}
		// 编辑和添加跳到不同页面
		if (!StringTools.isValid(role.getId())) {
			model.addAttribute("role", role);
			return newRole(model);
		} else {
			return editRole(model, role.getId());
		}
	}
	
	@ResponseBody
	@RequestMapping("/deleteRole")
	public Map<String,String> disableRole(String roleID) {
		Map<String,String> result = new HashMap<String,String>();
		try {
			sysRoleService.deleteRole(roleID);
			sysRoleService.deleteRoleUser(roleID);
			sysRoleService.deleteRoleMenu(roleID);
			userService.deleteUserWithoutRole();
			result.put("Success","数据清除成功！");
		} catch (Exception e) {
			result.put("errorMsg","数据清除失败！");
		}
		return result;
	}
}