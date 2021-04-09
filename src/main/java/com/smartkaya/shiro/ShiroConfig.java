package com.smartkaya.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

@Configuration
public class ShiroConfig{
	/**
	 *创建shirofilterfactorybean
	 *
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager")DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
		filterFactoryBean.setSecurityManager(securityManager);
		/**
		 * 常用的内置过滤器
		 * 	anon:无需认证(登录)可以访问
		 * 	authc:必须认证才可以访问
		 * 	user:如果使用rememberme功能可以访问
		 * 	perms:该资源必须得到资源权限才可以访问
		 * 	role:该资源必须得到角色权限才可以访问
		 */
		Map<String, String> filterMap =new LinkedHashMap<>();
		filterMap.put("/genCaptcha","anon");
		filterMap.put("/X-admin/*","anon");
		filterMap.put("/Mini/lib/*","anon");
		
		filterMap.put("/layui/*","anon");
		filterMap.put("/Mini/css/*","anon");
		filterMap.put("/Mini/js/*","anon");
		filterMap.put("/Mini/images/*","anon");
		filterMap.put("/favicon.icn","anon");
		// 验证码匿名访问
		filterMap.put("/login/main","anon");
		filterMap.put("/Mini/*","authc");
//		filterMap.put("/user/*","user");
//		filterMap.put("/add", "perms[user:add]");
//		filterMap.put("/update", "perms[user:update]");
		filterFactoryBean.setLoginUrl("/Mini/page/login-1.html");
		filterFactoryBean.setFilterChainDefinitionMap(filterMap);
		//未授权页面的跳转
		filterFactoryBean.setUnauthorizedUrl("/Unauthor");
		return filterFactoryBean;

	}

	/*
	 * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
	 * 所以我们需要修改下doGetAuthenticationInfo中的代码; )
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("MD5");// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(1);// 散列的次数，比如散列两次，相当于md5(md5(""));
		return hashedCredentialsMatcher;
	}
	/**
	 * 创建realm
	 *
	 */
	@Bean
	public UserRealm userRealm() {
		UserRealm userRealm=new UserRealm();
		userRealm.setCredentialsMatcher(this.retryLimitCredentialsMatcher());
		//userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return userRealm;
	}

	/**
	 * 创建defaultwebsecuritymanager
	 *
	 */
	@Bean(name="defaultWebSecurityManager")
	public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(userRealm);

		// 注入缓存管理器;
		securityManager.setCacheManager(ehCacheManager());
		return securityManager;

	}

	@Bean
	public ShiroDialect getShiroDialect() {
		return new ShiroDialect();
	}

	/**
	 * EnterpriseCacheSessionDAO shiro sessionDao层的实现；
	 * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
	 */
	@Bean
	public EnterpriseCacheSessionDAO enterCacheSessionDAO() {
		EnterpriseCacheSessionDAO enterCacheSessionDAO = new EnterpriseCacheSessionDAO();
		//添加缓存管理器
		//enterCacheSessionDAO.setCacheManager(ehCacheManager());
		//添加ehcache活跃缓存名称（必须和ehcache缓存名称一致）
		enterCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
		return enterCacheSessionDAO;
	}

	/**
	 * remenberMe记住我
	 */
	/**
	 *Cookie
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		//这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		//如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
		simpleCookie.setHttpOnly(true);
		//记住我cookie生效时间,单位是秒
		simpleCookie.setMaxAge(600000);
		return simpleCookie;
	}
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		//rememberme cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度（128 256 512 位），通过以下代码可以获取
		//KeyGenerator keygen = KeyGenerator.getInstance("AES");
		//SecretKey deskey = keygen.generateKey();
		//System.out.println(Base64.encodeToString(deskey.getEncoded()));
		byte[] cipherKey = Base64.decode("wGiHplamyXlVB11UXWol8g==");
		cookieRememberMeManager.setCipherKey(cipherKey);
		cookieRememberMeManager.setCookie(rememberMeCookie());
		return cookieRememberMeManager;
	}
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(this.userRealm());
		securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}


	/**
	 *
	 * @描述：sessionManager添加session缓存操作DAO
	 * @创建人：wyait
	 * @创建时间：2018年4月24日 下午8:13:52
	 * @return
	 */
	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		//sessionManager.setCacheManager(ehCacheManager());
		sessionManager.setSessionDAO(enterCacheSessionDAO());
		sessionManager.setSessionIdCookie(rememberMeCookie());
		return sessionManager;
	}

	//设置缓存机制
	@Bean
	public EhCacheManager ehCacheManager() {
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
		return ehCacheManager;
	}
	//设置密码重试次数
	 @Bean
	    public CredentialsMatcher retryLimitCredentialsMatcher() {
	        RetryLimitCredentialsMatcher retryLimitCredentialsMatcher = new RetryLimitCredentialsMatcher(this.ehCacheManager());
	        retryLimitCredentialsMatcher.setHashAlgorithmName("MD5");
//	        retryLimitCredentialsMatcher.setHashIterations(MD5Util.HASH_ITERATIONS);
	        retryLimitCredentialsMatcher.setStoredCredentialsHexEncoded(true);
	        return retryLimitCredentialsMatcher;
	    }
//	/**
//	 * 
//	 * @描述：开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
//	 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
//	 * </br>Enable Shiro Annotations for Spring-configured beans. Only run after the lifecycleBeanProcessor(保证实现了Shiro内部lifecycle函数的bean执行) has run
//	 * </br>不使用注解的话，可以注释掉这两个配置
//	 * @创建人：wyait
//	 * @创建时间：2018年5月21日 下午6:07:56
//	 * @return
//	 */
//	@Bean
//	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
//		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//		advisorAutoProxyCreator.setProxyTargetClass(true);
//		return advisorAutoProxyCreator;
//	}
//
//	@Bean
//	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
//		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
//		return authorizationAttributeSourceAdvisor;
//	}
}
