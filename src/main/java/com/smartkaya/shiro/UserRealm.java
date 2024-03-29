package com.smartkaya.shiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
//import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.smartkaya.bean.Paramater;
import com.smartkaya.constant.Constant;
import com.smartkaya.dao.KayaSQLExecute;
import com.smartkaya.user.SysPermission;
import com.smartkaya.user.SysRole;
import com.smartkaya.user.SysUser;


public class UserRealm extends AuthorizingRealm{

	/**
	 *执行授权逻辑
	 *
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		System.out.println("执行授权逻辑--------------------");
		//		给资源进行授权
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
//		//		添加授权内容
//		//		info.addStringPermission("user:add");
//		Subject subject = SecurityUtils.getSubject();
//		String principal = (String) subject.getPrincipal();
//
//		Paramater paramater2 = new Paramater();
//		paramater2.setId("");
//		HashMap<String, Object> propertys2  = new HashMap<String, Object>();
//		propertys2.put("Password", "123456");
//		propertys2.put("EmployeeId", "10001");
//		paramater2.setCrud(Constant.SELECT);
//		paramater2.setPropertys(propertys2);
//		
//		//List<HashMap<String, Object>> resultList2 = dao.selectOrientationkey(paramater2);
//		// 查询当前用户
//		KayaSQLExecute dao = new KayaSQLExecute();
//		HashMap<String, Object> userInfoMap = dao.selectOrientationkey(paramater2).get(0);

		
//		System.out.println("MyShiroRealm的doGetAuthorizationInfo授权方法执行");
//		 
//		// User user=(User)
//		// principals.fromRealm(this.getClass().getName()).iterator().next();//获取session中的用户
//		// System.out.println("在MyShiroRealm中AuthorizationInfo（授权）方法中从session中获取的user对象:"+user);
// 
//		// 从PrincipalCollection中获得用户信息
//		Object principal = principals.getPrimaryPrincipal();
//		System.out.println("ShiroRealm  AuthorizationInfo:" + principal.toString());
// 
//		// 根据用户名来查询数据库赋予用户角色,权限（查数据库）
//		Set<String> roles = new HashSet<>();
//		Set<String> permissions = new HashSet<>();
////		2018.09.14更新
//		//		给用户添加user权限 (没有进行判断、对所有的用户给user权限)
//		if("user".equals(principal)){
//			roles.add("user");
//			permissions.add("user:query");
//		}
////		当用户名为admin时 为用户添加权限admin  两个admin可以理解为连个字段
//		if ("admin".equals(principal)) {
//			roles.add("admin");
//			permissions.add("admin:query");
//		}
////		为用户添加visit游客权限，在url中没有为visit权限，所以，所有的操作都没权限
//		if("visit".equals(principal)){
//			roles.add("visit");
//			permissions.add("visit:query");
//		}
////              更新以上代码
//		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
//		//添加权限
//		info.setStringPermissions(permissions);
//		return info;
		// return null;

		//info.addObjectPermissions(Collection<Permission> permissions);
		return info;
	}

	/**
	 * 执行认证逻辑
	 *
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("执行认证逻辑----------------------");
		/**
		String principal=(String) authcToken.getPrincipal();
		String credentials=(String) authcToken.getCredentials();
		//以上方法获取不到数据
		 */
		UsernamePasswordToken token=(UsernamePasswordToken) authcToken;
		
		System.out.println("用户登录认证：验证当前Subject时获取到token为：" + ReflectionToStringBuilder
				.toString(token, ToStringStyle.MULTI_LINE_STYLE));
		String principal =token.getUsername();
//		char[] credentials = token.getPassword();
//		ByteSource credentialsSalt;
		Map<String, Object> userMap = new HashMap<String,Object>();
		//数据库数据
		String username;
		//String userid;
		String password;
		SysUser sysuser = new SysUser();
		Paramater paramater2 = new Paramater();
		paramater2.setId("id-0065-00000003");
		HashMap<String, Object> propertys2  = new HashMap<String, Object>();
		propertys2.put("EmployeeId", principal);
		paramater2.setCrud(Constant.SELECT);
		paramater2.setPropertys(propertys2);
		
		//List<HashMap<String, Object>> resultList2 = dao.selectOrientationkey(paramater2);
		// 查询当前用户
		KayaSQLExecute dao = new KayaSQLExecute();
		List<HashMap<String, Object>> userInfoMapList = dao.selectOrientationkey(paramater2);

		if (userInfoMapList == null || userInfoMapList.size() == 0) {
			// 没找到帐号
			//throw new UnknownAccountException();
		}else {
			sysuser.setUserId(userInfoMapList.get(0).get("EmployeeId").toString());
			sysuser.setPassword(userInfoMapList.get(0).get("Password").toString());
			// roleList
			List<SysRole> sysRoleList =new ArrayList<SysRole>();
			@SuppressWarnings("unchecked")
			List<HashMap<String,Object>> userRoles = (ArrayList<HashMap<String,Object>>)userInfoMapList.get(0).get("RoleInfo");
			if (userRoles == null || userRoles.size() == 0) {
				// 没找到帐号
				//throw new UnknownAccountException();
			} else {
				List<String> userRoleList = new ArrayList<String>();
				userRoles.forEach(item -> {
					SysRole sysrole = new SysRole();
					sysrole.setRoleId(item.get("RoleType").toString());
					sysrole.setRole(item.get("Other").toString());
					sysRoleList.add(sysrole);
					userRoleList.add(item.get("RoleType").toString());
					});
				sysuser.setRoleList(sysRoleList);
				userMap.put("RoluInfo", userRoleList);// Workflow验证用
			}
			
			
			List<SysPermission> sysPerList =new ArrayList<SysPermission>();
			@SuppressWarnings("unchecked")
			List<HashMap<String,Object>> userPers = (ArrayList<HashMap<String,Object>>)userInfoMapList.get(0).get("PermissionsInfo");
			if (userPers == null || userPers.size() == 0) {
				// 没找到帐号
				//throw new UnknownAccountException();
			} else {
				List<String>  userPerList = new ArrayList<String>();
				userPers.forEach(item -> {
					SysPermission sysper = new SysPermission();
					sysper.setPermissionId(item.get("Permission").toString());
					sysper.setPermission(item.get("Other").toString());
					sysPerList.add(sysper);
					userPerList.add(item.get("Permission").toString());
					});
				sysuser.setPermissions(sysPerList);
				
				userMap.put("PermissionInfo", userPerList);// Workflow验证用
			}
			
			
			
			//credentialsSalt = ByteSource.Util.bytes(username);
		}

		username=userInfoMapList.get(0).get("EmployeeId").toString();
		//userid = userInfoMapList.get(0).get("EmployeeName").toString();
				
				
		password=userInfoMapList.get(0).get("Password").toString();
		sysuser.setUserMap(userMap);


//
//		if(user==null) {
//			return null;
//		}else {
//			username=user.getUsername();
//			password=user.getPassword();
//		}
		/**
		 * 不在需要加密,在retryLimitCredentialsMatcher已经给我们加密了
		 */
		//		Md5Hash str=new Md5Hash(credentials);
		//		String string=str.toString();
		//		token.setPassword(string.toCharArray()); 

		UsernamePasswordToken token2=(UsernamePasswordToken) authcToken;

		System.out.println("数据库密码:");
		for(char c:token2.getPassword()) {
			System.out.print(c);
		}
		System.out.println();
		 //4.盐值
        //ByteSource credentialSalt= ByteSource.Util.bytes(username); 

		if(!principal.equals(username)) {
			return null;//shiro底层抛出UnknownAccountException异常
		}
		return new SimpleAuthenticationInfo(sysuser, password, getName());
	}

}
