package com.smartkaya.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.smartkaya.constant.Constant;


public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;


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
    private HashMap<String, Object> userMap = new HashMap<String, Object>();
    
    private List<HashMap<String, Object>> roles = new ArrayList<HashMap<String, Object>>();
	private List<HashMap<String, Object>> permissions = new ArrayList<HashMap<String, Object>>();
	
	
	//private List<HashMap<String,Object>> OrganizeInformation = new ArrayList<HashMap<String,Object>>();

	/**
	 * Get公司CODE
	 * @return
	 */
    public String getCompanyCode() {
    	return this.userMap.get(Constant.USER_COMPANYCODE).toString();
    }
    
    /**
     * Set公司CODE
     * @param companyCode
     */
    public void setCompanyCode(String companyCode) {
    	this.userMap.put(Constant.USER_COMPANYCODE,companyCode);
    }
    
    /**
     * 用户ID
     * @return
     */
	public String getUserId() {
		return this.userMap.get(Constant.USER_ID).toString();
	}

	public void setUserId(String userId) {
		this.userMap.put(Constant.USER_ID, userId);
	}

	/**
	 * 用户名
	 * @return
	 */
	public String getUserName() {
		return this.userMap.get(Constant.USER_NAME).toString();
	}

	public void setUserName(String userName) {
		this.userMap.put(Constant.USER_NAME,userName);
	}

	/**
	 * 用户密码
	 * @return
	 */
	public String getPassword() {
		return this.userMap.get(Constant.USER_PASSWORD).toString();
	}

	public void setPassword(String password) {
		this.userMap.put(Constant.USER_PASSWORD,password);
	}
	
	/**
	 * 用户角色（普通员工，经理等）
	 * @param role
	 */
	public List<HashMap<String, Object>> getRoles() {
		return roles;
	}

	public void setRoles(List<HashMap<String, Object>> roles) {
		this.roles = roles;
	}
	
	
	/**
	 * 权限管理（多重权限 Admin, Sys等特殊权限管理）
	 * @param permission
	 */

	public List<HashMap<String, Object>> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<HashMap<String, Object>> permissions) {
		this.permissions = permissions;
	}
	
	public Boolean getEnabled() {
		return (Boolean)this.userMap.get(Constant.USER_ENABLED);
	}



	public void setEnabled(Boolean enabled) {
		this.userMap.put(Constant.USER_ENABLED, enabled);
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

    public HashMap<String, Object> getUserMap() {
		return userMap;
	}

	public void setUserMap(HashMap<String, Object> userMap) {
		this.userMap = userMap;
	}
	
    public Object getUserProperty(String propertyKey) {
		return userMap.get(propertyKey);
	}

	public void setUserProperty(String propertyKey, Object propertyValue) {
		this.userMap.put(propertyKey, propertyValue);
	}
	
	
	
	// @TODO 

	public enum UserType {

		E1,E2,E3,EO,PEM,PM,GL,PL;
	};
	public SysUser initUserInfo(UserType userType) {
		//User user = new User();

		List<String> roleList = new ArrayList<String>();
		List<String> bumencodeList = new ArrayList<String>();
		List<String> permissions = new ArrayList<String>();
		//HashMap<String, Object> userMap = new HashMap<String, Object>();
		
		switch (userType){
		case E1:
			
			roleList.add("E");
			userMap.put("roles", roleList);
			this.setUserId("10001");
			permissions.add("E");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10004");
			bumencodeList.add("12");
			bumencodeList.add("13");
			userMap.put("BumenCode",bumencodeList);
			//userMap.put("YuanGongId", user.getUserId());
			
			userMap.put("ZhiweiCode","1");
			this.setUserMap(userMap);
			break;
		case E2:
			
			this.setUserId("10002");
			roleList.add("E");
			userMap.put("roles", roleList);

			permissions.add("E");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10004");
			bumencodeList.add("12");
			bumencodeList.add("13");
			userMap.put("BumenCode",bumencodeList);
			userMap.put("ZhiweiCode","1");
			this.setUserMap(userMap);
			break;
		case EO:
			
			this.setUserId("10003");
			roleList.add("EO");
			userMap.put("roles", roleList);
			userMap.put("YuanGongId", this.getUserId());
			permissions.add("E");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10004");
			userMap.put("BumenCode","20");
			userMap.put("ZhiweiCode","0");
			this.setUserMap(userMap);
			break;
		case PEM:
			
			this.setUserId("10004");
			roleList.add("E");
			roleList.add("PEM");
			userMap.put("roles", roleList);
			userMap.put("YuanGongId", this.getUserId());
			permissions.add("E");
			permissions.add("PEM");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10006");
			bumencodeList.add("12");
			bumencodeList.add("13");
			userMap.put("BumenCode",bumencodeList);
			userMap.put("ZhiweiCode","4");
			this.setUserMap(userMap);
			break;
		case PM:
			
			this.setUserId("10005");
			roleList.add("E");
			userMap.put("roles", roleList);
			userMap.put("YuanGongId", this.getUserId());
			permissions.add("E");
			permissions.add("PM");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10001");
			userMap.put("BumenCode","20");
			userMap.put("ZhiweiCode","3");
			this.setUserMap(userMap);
			break;
		case GL:
			
			this.setUserId("10006");
			roleList.add("E");
			roleList.add("EO");
			userMap.put("roles", roleList);
			userMap.put("YuanGongId", this.getUserId());
			permissions.add("E");
			permissions.add("PEM");
			permissions.add("GL");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10008");
			userMap.put("bumenids","20");
			userMap.put("ZhiweiCode","5");
			this.setUserMap(userMap);
			break;
		case PL:
			
			this.setUserId("10007");
			roleList.add("E");
			userMap.put("roles", roleList);
			userMap.put("YuanGongId", this.getUserId());
			permissions.add("E");
			userMap.put("permissions", permissions);
			
			userMap.put("PemId","10006");
			userMap.put("BumenCode","20");
			userMap.put("ZhiweiCode","2");
			this.setUserMap(userMap);
			break;
		default:
			break;
		}
		
		return this;
	}

}
