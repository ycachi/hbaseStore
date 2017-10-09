package com.asiainfo.base.entity.system;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;



public class Menu extends BasePojo {

	private static final long serialVersionUID = 1L;
	private Menu parent;	// 父级菜单
	private String name; 	// 名称
	private String href; 	// 链接
	private String icon; 	// 图标
	private Integer sort; 	// 排序
	private String isShow="1"; 	// 是否在菜单中显示（1：显示；0：不显示）
	private String permission; // 权限标识
	public Menu(){
		super();
		this.sort = 30;
		this.isShow = "1";
	}
	
	public Menu(String id){
		super(id);
	}

	@JsonBackReference
	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}

	public static void sortList(List<Menu> list, List<Menu> sourcelist, String parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			Menu e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						Menu child = sourcelist.get(j);
						if (child.getParent()!=null && child.getParent().getId()!=null
								&& child.getParent().getId().equals(e.getId())){
							sortList(list, sourcelist, e.getId(), true);
							break;
						}
					}
				}
			}
		}
	}

	public static String getRootId(){
		return "1";
	}

	@Override
	public String toString()
	{
		return "Menu [parent=" + parent + ", name=" + name + ", href=" + href + ", icon="
				+ icon + ", sort=" + sort + ", isShow=" + isShow + ", permission="
				+ permission + "]";
	}

	

	

}