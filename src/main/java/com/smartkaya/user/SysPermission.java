package com.smartkaya.user;

import java.io.Serializable;

public class SysPermission implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String permissionId;

	/**
	 * 名称，如 用户删除、角色添加、商品修改等类似的文字
	 */
	private String permissionName;

	/**
	 * Shiro权限标识符，如: user:delete、role:add、goods:update。
	 * 格式为Shiro框架规定的格式，名称可以随意定义。 需注意有一个特别的写法为: user:* 。 其中 * 代表拥有 user下所有的权限
	 */
	private String permission;

	/**
	 * 资源类型，menu(菜单)、button(按钮) (可选，不加该字段不影响)
	 */
	// private String resourceType;

	/**
	 * 资源路径
	 */
	private String url;

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
