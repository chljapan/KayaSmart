package com.smartkaya.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysUser implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 数据表
     */
    private String userTableId;

	/**
     * 数据库主键
     */
    private String userId;

    /**
     * 用户名（登录名）
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 改账户是否启用
     * true为启用 false为禁用
     */
    private Boolean enabled;

    /**
     * 一个用户对应一个角色的情况
     * 该用户对应的角色
     * 这个与下方的情况 二选一即可
     */
    //private SysRole role;

    /**
     * 一个用户对应多个角色的情况
     * 该用户的角色列表
     */
    private List<SysRole> roleList = new ArrayList<SysRole>();

    /**
     * 一个用户对应多个权限的情况
     * 该用户的权限列表
     */
    private List<SysPermission> permissions = new ArrayList<SysPermission>();

    /**
     * 密码
     */
    private Map<String, Object> userMap = new HashMap<String, Object>();

	/**
     * 加密过程中使用的 盐
     * 通常为: 用户名 + 随机的一些数值（改数值必须和你注册时密码加密使用的数值是一致的）。
     * 在这里我就使用固定的，如： layjava
     */
    public String getCredentialsSalt() {
        return userName + "layjava";
    }


	public String getUserTableId() {
		return userTableId;
	}

	public void setUserTableId(String userTableId) {
		this.userTableId = userTableId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<SysRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SysRole> roleList) {
		this.roleList = roleList;
	}

    /**
     * 还可以拓展其他的用户信息字段，这里尽量只添加最基本的字段信息
     */
	public List<SysPermission> getPermissions() {
		return permissions;
	}
	
	public List<String> getPermissionList() {
		List<String> perList = new ArrayList<String>();
		permissions.forEach(item -> {
			perList.add(item.getPermissionId());
		});
		return perList;
	}

	public void setPermissions(List<SysPermission> permissions) {
		this.permissions = permissions;
	}

    public Map<String, Object> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, Object> userMap) {
		this.userMap = userMap;
	}

}
