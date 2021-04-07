package com.smartkaya.shiro;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.smartkaya.bean.Paramater;
import com.smartkaya.constant.Constant;
import com.smartkaya.dao.KayaSQLExecute;

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

		//数据库数据
		String username;
		String password;

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
			throw new UnknownAccountException();
		}else {
			
			username=userInfoMapList.get(0).get("EmployeeId").toString();
			password=userInfoMapList.get(0).get("Password").toString();
			//credentialsSalt = ByteSource.Util.bytes(username);
		}




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
		return new SimpleAuthenticationInfo(username, password, getName());
	}
	
	
//	org.apache.shiro.authc.IncorrectCredentialsException ：
//	Submitted credentials for token [org.apache.shiro.authc.UsernamePasswordToken - admin, rememberMe=false] did not match the expected credentials.
//	原因：
//
//	数据库的密码是加密的,而页面传过来的是数据未加密。
//	解决办法：
//
//	自己写的类继承AuthorizingRealm，重写setCredentialsMatcher方法

//	@Override
//	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
//		// 构建凭证匹配对象
//		HashedCredentialsMatcher cMatcher = new HashedCredentialsMatcher();
//		// 设置加密算法  Matcher
//		cMatcher.setHashAlgorithmName("MD5");
//		// 设置加密次数
//		cMatcher.setHashIterations(1);
//		super.setCredentialsMatcher(cMatcher);
//	}
//	/**
//	 * 重写shiro的密码验证，让shiro用我自己的验证.
//	 */
//	@PostConstruct
//	public void initCredentialsMatcher() {
//		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Constant.HASH_ALGORITHM);
//		matcher.setHashIterations(Constant.HASH_INTERATIONS);
//		setCredentialsMatcher(matcher);
//	}
}
