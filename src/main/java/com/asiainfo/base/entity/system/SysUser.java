package com.asiainfo.base.entity.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SysUser {
    private String id;
    private String username;
    private String password;
    private String characterName;
    private String email;
    private String phone;
    private int isEnable = 1;
    private String login_ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateDate;
    private String remarks;
    private String delFlag = "0";
    private List<SysRole> roles = new ArrayList<SysRole>();
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLogin_ip() {
		return login_ip;
	}

	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public boolean isAdmin(){
		return this.id.equals("1");
	}
	
	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = new ArrayList<String>();
		for (SysRole role : roles) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roles = new ArrayList<SysRole>();
		for (String roleId : roleIdList) {
			SysRole role = new SysRole();
			role.setId(roleId);
			roles.add(role);
		}
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		List<String> roleNames = new ArrayList<String>();
		for(SysRole role : roles) {
			roleNames.add(role.getName());
			roleNames.add(",");
		}
		if(roleNames.size() > 0)//移除最后一个','
			roleNames.remove(roleNames.size() - 1);

		return roleNames.toString();
	}
	
	public static boolean isAdmin(String id){
		return id != null && "1".equals(id);
	}
}
