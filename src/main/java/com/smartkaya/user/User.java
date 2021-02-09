package com.smartkaya.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


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

//    /**
//     * 一个用户对应多个角色的情况
//     * 该用户的角色列表
//     */
//    private List<String> roleList = new ArrayList<String>();
//
//    /**
//     * 一个用户对应多个权限的情况
//     * 该用户的权限列表
//     */
//    private List<String> permissions = new ArrayList<String>();
 
    
    /**
     * 密码
     */
    private Map<String, Object> userMap = new HashMap<String, Object>();


    
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

//	public List<String> getRoleList() {
//		return roleList;
//	}
//
//	public void setRoleList(List<String> roleList) {
//		this.roleList = roleList;
//	}
//
//    /**
//     * 还可以拓展其他的用户信息字段，这里尽量只添加最基本的字段信息
//     */
//	public List<String> getPermissions() {
//		return permissions;
//	}
//
//	public void setPermissions(List<String> permissions) {
//		this.permissions = permissions;
//	}

    public Map<String, Object> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, Object> userMap) {
		this.userMap = userMap;
	}
	
	
	
	// @TODO 

	public enum UserType {

		E1,E2,E3,EO,PEM,PM,GL,PL;
	};
	public User initUserInfo(UserType userType) {
		User user = new User();

		List<String> roleList = new ArrayList<String>();
		List<String> bumencodeList = new ArrayList<String>();
		List<String> permissions = new ArrayList<String>();
		Map<String, Object> userMap = new HashMap<String, Object>();
		
		switch (userType){
		case E1:
			
			roleList.add("E");
			userMap.put("roles", roleList);
			user.setUserId("10001");

			permissions.add("E");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10004");
			bumencodeList.add("12");
			bumencodeList.add("13");
			userMap.put("BumenCode",bumencodeList);
			userMap.put("ZhiweiCode","1");
			user.setUserMap(userMap);
			break;
		case E2:
			
			user.setUserId("10002");
			roleList.add("E");
			userMap.put("roles", roleList);

			permissions.add("E");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10004");
			bumencodeList.add("12");
			bumencodeList.add("13");
			userMap.put("BumenCode",bumencodeList);
			userMap.put("ZhiweiCode","1");
			user.setUserMap(userMap);
			break;
		case EO:
			
			user.setUserId("10003");
			roleList.add("EO");
			userMap.put("roles", roleList);

			permissions.add("E");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10004");
			userMap.put("BumenCode","20");
			userMap.put("ZhiweiCode","0");
			user.setUserMap(userMap);
			break;
		case PEM:
			
			user.setUserId("10004");
			roleList.add("E");
			roleList.add("PEM");
			userMap.put("roles", roleList);

			permissions.add("E");
			permissions.add("PEM");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10006");
			bumencodeList.add("12");
			bumencodeList.add("13");
			userMap.put("BumenCode",bumencodeList);
			userMap.put("ZhiweiCode","4");
			user.setUserMap(userMap);
			break;
		case PM:
			
			user.setUserId("10005");
			roleList.add("E");
			userMap.put("roles", roleList);

			permissions.add("E");
			permissions.add("PM");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10001");
			userMap.put("BumenCode","20");
			userMap.put("ZhiweiCode","3");
			user.setUserMap(userMap);
			break;
		case GL:
			
			user.setUserId("10006");
			roleList.add("E");
			roleList.add("EO");
			userMap.put("roles", roleList);

			permissions.add("E");
			permissions.add("PEM");
			permissions.add("GL");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10008");
			userMap.put("bumenids","20");
			userMap.put("ZhiweiCode","5");
			user.setUserMap(userMap);
			break;
		case PL:
			
			user.setUserId("10007");
			roleList.add("E");
			userMap.put("roles", roleList);

			permissions.add("E");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10006");
			userMap.put("BumenCode","20");
			userMap.put("ZhiweiCode","2");
			user.setUserMap(userMap);
			break;
		default:
			break;
		}
		
		return user;
	}

}
