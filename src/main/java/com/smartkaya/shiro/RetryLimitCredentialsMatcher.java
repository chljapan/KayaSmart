package com.smartkaya.shiro;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;


public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher{
	 /**
     * 集群中可能会导致出现验证多过5次的现象，因为AtomicInteger只能保证单节点并发
     */
	 private static final int MAX_LOGIN_RETRY_TIMES = 2;
	 private static final Logger LOGGER = getLogger(RetryLimitCredentialsMatcher.class);
	    private Cache<String, AtomicInteger> passwordRetryCache;
		
	    public Cache<String, AtomicInteger> getPasswordRetryCache() {
			return passwordRetryCache;
		}
		public RetryLimitCredentialsMatcher(EhCacheManager ehCacheManager) {
	        passwordRetryCache = ehCacheManager.getCache("passwordRetryCache");
	    }
	    @Override
	    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws ExcessiveAttemptsException{
	        String userName = (String) token.getPrincipal();
	        //测试
	        UsernamePasswordToken upt=(UsernamePasswordToken) token;
	        
	        
	        char[] password = upt.getPassword();
	        System.out.print("缓存中的密码:");
	        for(char c:password) {
	        	System.out.print(c);
	        }
	        System.out.println();
	        
	        
	        AtomicInteger retryCount = passwordRetryCache.get(userName);

	        if (retryCount == null) {
	            // 高并发下使用的线程安全的int类
	            retryCount = new AtomicInteger(0);
	            passwordRetryCache.put(userName, retryCount);
		        System.out.println("密码重试次数:"+retryCount);
	        }
	        if (retryCount.incrementAndGet() > MAX_LOGIN_RETRY_TIMES) {
//	            throw new ExcessiveAttemptsException();
	        	LOGGER.error("登录次数超过限制");
	        	throw new ExcessiveAttemptsException("用户:" + userName + "登录次数已经超过限制");
	        }
	        //并将其保存到缓存中
	        passwordRetryCache.put(userName, retryCount);
	        //debug
	       // if (LOGGER.isDebugEnabled()) {
	            LOGGER.debug("用户:{},尝试登录次数:{}", userName, retryCount.get());
	       // }
	        
		
	        boolean match = super.doCredentialsMatch(token, info);
	        if (match) {
	            passwordRetryCache.remove(userName);
	        }
			
	        return match;
	    }
}
