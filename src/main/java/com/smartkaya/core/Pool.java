/*
 * Created on 2017/12
 * KaYa 数据库连接池接口
 * @author LiangChen  
 * https://github.com/chljapan
 */
package com.smartkaya.core;

import java.sql.Connection;
/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public interface Pool {
	/**
	 * 获取 最初初始化的 连接数
	 * @return
	 */
	public int getInitialConnections() ;
	
	/**
	 * 设置 初始化连接数
	 * @param initialConnections
	 */
	public void setInitialConnections(int initialConnections);
	/**
	 * 每次增长值
	 * @return
	 */
	public int getIncrementalConnections() ;
	/**
	 * 设置 每次增长值
	 * @param incrementalConnections
	 */
	 public void setIncrementalConnections(int incrementalConnections);
	 
	 /**
	  * 获取最大连接数
	  * @return
	  */
	 public int getMaxConnections();
 
	 /**
	  * 设置最大连接数
	  *
	  */
	 public void setMaxConnections(int maxConnections);
	 
	 /**
	  * 初始化池
	  */
	 public  void initPool();
	 
	 /**
	  * 获取连接
	  * @return
	  */
	 public  Connection getConnection();
	 
	 /**
	  * 释放(返还)连接到 池子
	  * @param conn
	  */
	 public void returnConnection(Connection conn);
	 
	 /**
	  * 刷新 池子
	  * 相当于重启
	  */
	 public  void refreshConnections();
	 
	 /**
	  * 关闭连接池
	  */
	 public  void closeConnectionPool();
}
