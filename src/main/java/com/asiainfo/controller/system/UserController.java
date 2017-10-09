package com.asiainfo.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.asiainfo.base.entity.system.SysUser;
import com.asiainfo.common.MD5Util;
import com.asiainfo.common.StringTools;
import com.asiainfo.common.UUIDUtil;
import com.asiainfo.service.system.SysRoleService;
import com.asiainfo.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class UserController {
	@Autowired
    private UserService userService;
	@Autowired
    private SysRoleService sysRoleService;
	/**
	 * 检查用户名是否已存在
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("/checkUserName")
    @ResponseBody
    public Map<String, Object> checkUserName(String username) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<SysUser> user = userService.findByUserName(username);
		if(user==null || user.size() < 1){
			result.put("result", "0");
		}else{
			result.put("result", "1");
			result.put("msg", "用户名系统已存在！ username: " + username);
		}
		return result;
	}
	/**
	 * 新增用户页面返回所有角色
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/newUser")
	public String newUser(Model model) {
		model.addAttribute("roles", sysRoleService.findAllRole());
		model.addAttribute("user", new SysUser());
		return "/system/userForm";
	}
	/**
	 * 编辑用户
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/editUser")
	public String editUser(String id, Model model) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		SysUser user = null;
		try {
			user = userService.findByMap(paramMap);
			if(user == null)
			{
				user = userService.queryById(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("roles", sysRoleService.findAllRole());
		model.addAttribute("user", user);
		return "/system/userForm";
	}
	/**
	 * 用户列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/userList")
	public String list(ModelMap model) {
		List<SysUser> users = userService.findAllList();
		model.addAttribute("users", users);
		return "/system/userList";
	}

	/**
	 * 删除用户
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/deleteUser")
	public String deleteUser(Model model,String id) {
		//删除用户
		userService.deleteById(id);
		//删除用户角色关系
		userService.deleteUserRole(id);
		model.addAttribute("message", "删除用户成功");
		return "redirect:/userList";
	}
	/**
	 * 禁用用户
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/enableUser")
	public String enableUser(SysUser user,Model model) {
		userService.updateUser(user);
		model.addAttribute("message", "用户数据更新成功");
		return "redirect:/userList";
	}
	/**
	 * 保存用户
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/saveUser")
	public String saveUser(@Valid SysUser sysUser,BindingResult bindingResult,Model model,String hiddenMd5) {
		if(!StringTools.isValid(hiddenMd5)){
			//如果隐藏密码为空，则一定是新增用户
			sysUser.setId(UUIDUtil.getUUIDKey());
			sysUser.setPassword(MD5Util.encode(sysUser.getPassword()));
			userService.insertUserAndRole(sysUser);
		}else{
			if(!hiddenMd5.equals(sysUser.getPassword())){
				sysUser.setPassword(MD5Util.encode(sysUser.getPassword()));
			}
			userService.updateUserAndRole(sysUser);
		}
		model.addAttribute("message", "用户数据保存成功！");
		//处理角色
		return "redirect:/userList";
	}
}
