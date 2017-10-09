package com.asiainfo.controller.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.asiainfo.base.entity.system.CommonType;
import com.asiainfo.base.entity.system.Menu;
import com.asiainfo.common.StringTools;
import com.asiainfo.service.system.MenuService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@Controller
public class MenuController extends BaseController{
	@Autowired
	private MenuService menuService;
	@Value("#{'${default.pageSize}'}")
	private String pageSize;
	
	/**
     * 菜单首页分页查询
     * @param
     * @return
     */
	@RequestMapping("/menuList")
    public String list(ModelMap model,HttpServletRequest request,String page) 
	{
		int size = Integer.valueOf(pageSize);
		String pageNum = request.getParameter("page");
		pageNum = StringTools.isValid(pageNum) ? pageNum:"0";
		PageInfo<Menu> pageInfo= menuService.findAllPageList(Integer.valueOf(pageNum), size);
        model.addAttribute("page", pageInfo);
        model.addAttribute("menus", pageInfo.getList());
		
        return "/system/menuList";
    }
	/**
     * 获取所有菜单
     * @param
     * @return
     */
	@RequestMapping("/newMenu")
    public String newMenu(Model model) {
        List<Menu> menus = menuService.findAllList();
        JSONArray menuJson = new JSONArray();
        for (Menu menu : menus) {
            JSONObject node = new JSONObject();
            node.put("id", menu.getId());
            node.put("pId", menu.getParentId());
            node.put("name", menu.getName());
            node.put("open", true);
            menuJson.add(node);
        }
        CommonType[] getIsShowType = CommonType.getIsShowType();
        CommonType[] getDelFlagType = CommonType.getDelFlagType();
        List<CommonType> isShowList = Arrays.asList(getIsShowType);
        List<CommonType> delFlagList = Arrays.asList(getDelFlagType);
        model.addAttribute("isShowList", isShowList);
        model.addAttribute("delFlagList", delFlagList);
        model.addAttribute("menus", menuJson.toJSONString());
        Menu menu = new Menu();
        Menu pmenu = new Menu();
        menu.setParent(pmenu);
        model.addAttribute("menu", menu);
        model.addAttribute("newtext", true);
        return "/system/menuForm";
    }
	
	/**
     * 编辑权限
     * 
     * @param model
     * @return
     */
    @RequestMapping("/editMenu")
    public String editMenu(Model model, String id) {
        CommonType[] getIsShowType = CommonType.getIsShowType();
        CommonType[] getDelFlagType = CommonType.getDelFlagType();
        List<CommonType> isShowList = Arrays.asList(getIsShowType);
        List<CommonType> delFlagList = Arrays.asList(getDelFlagType);
        Menu menu=new Menu();
        menu.setId(id);
        menu.setSort(null);
        menu.setIsShow(null);
        menu.setDelFlag(CommonType.ENABLE.getStringValue());
        menu=menuService.selectMenu(menu);
        model.addAttribute("newtext", false);
        model.addAttribute("isShowList", isShowList);
        model.addAttribute("delFlagList", delFlagList);
        model.addAttribute("menu", menu);
        return "/system/menuForm";
    }
    /**
     * 保存权限
     * 
     * @param
     * @param model
     * @param
     * @return
     */
    @RequestMapping("/saveMenu")
    public String saveMenu(Menu menu, Model model,String isShowNew,String permissionNew,String iconNew,String parentId) {
        menu.setIsShow(isShowNew);
        menu.setPermission(permissionNew);
        menu.setIcon(iconNew);
        menu.getParent().setId(parentId);
        menuService.insertOrUpdateMenu(menu);
        return "redirect:/menuList";
    }
    /**
     * 查询父级权限信息
     * 
     * @return
     */
    @RequestMapping("/getParent" )
    @ResponseBody
    public Object getParent(String nodeHtml){
        Map<String, Object> result = errorMap();
        Menu menu=new Menu();
        menu.setName(nodeHtml);
        menu.setSort(null);
        menu.setIsShow(CommonType.ENSHOW.getStringValue());
        menu.setDelFlag(CommonType.ENABLE.getStringValue());
        menu=menuService.selectMenu(menu);
        List<String> icons = menuService.selectIcons();
        if(menu!=null){
            result.put("code", "1");
            result.put("msg", menu);
            result.put("icons", icons);
        }
        return result;
    } 
    public Map<String, Object> errorMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "-1");
        result.put("msg", "该权限不能创建子权限！");
        return result;
    }
}
