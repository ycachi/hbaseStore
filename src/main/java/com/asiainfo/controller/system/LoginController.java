package com.asiainfo.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.asiainfo.base.entity.system.Menu;
import com.asiainfo.base.entity.system.SysUser;
import com.asiainfo.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




@Controller
public class LoginController {

	@Autowired
    private MenuService menuService;

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        return "login";
    }
	@RequestMapping(value="/logout")
    public String logout(){
       return "logout";
    }
    @RequestMapping(value="/deny")
    public String handleDeny(){
       return "deny";
    }
    @RequestMapping("/index")
    public String tosso(ModelMap model,String mainFrame,HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	SysUser currentUser = (SysUser)session.getAttribute("currentUser");
    	List<Menu> menus = menuService.getBackGroundShowMenuList(currentUser);
		model.addAttribute("menus", menus);
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("mainFrame", mainFrame);
		return "index";
    }
}