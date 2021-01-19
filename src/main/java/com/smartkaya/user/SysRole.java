package com.smartkaya.user;

import java.io.Serializable;
import java.util.List;

public class SysRole implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * 数据库主键
	 */
	private String roleId;

	/**
	 * 角色标识符，如: admin
	 */
	private String role;

	/**
	 * 描述，如: 管理员
	 */
	private String describe;

	/**
	 * 是否启用角色，当禁用时该角色以及对应的权限则无法被使用
	 */
	private Boolean enabled;

	/**
	 * 该角色拥有的权限
	 */
	private List<SysPermission> permissions;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<SysPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<SysPermission> permissions) {
		this.permissions = permissions;
	}

}
