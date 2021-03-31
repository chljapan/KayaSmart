package com.smartkaya.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.dao.KayaSQLExecute;
import com.smartkaya.log.KayaLogManager;

/**
 * 业务处理抽象类
 * @author LiangChen　2021/3/26
 * @version 1.0.0
 *
 */
public abstract class KayaBaseService {
	public Paramaters paramaters;
	public ArrayList<Paramaters> paramatersList;
	public Paramater paramater;

	public KayaLogManager kayaLoger = KayaLogManager.getInstance();
	private int paramaterType = 0;// 识别参数临时变量
	
	public KayaSQLExecute dao = new KayaSQLExecute();
	
	/**
	 * 执行业务处理
	 * @param paramater
	 * @return
	 */
	public static KayaBaseService excuteService(Paramater paramater) {
		return KayaFactory.createKayaService(paramater);
	}
	
	/**
	 * 执行业务处理
	 * @param paramaters
	 * @return
	 */
	public static KayaBaseService excuteService(Paramaters paramaters) {
		return KayaFactory.createKayaService(paramaters);
	}
	/**
	 * 执行业务处理
	 * @param serviceName
	 * @param paramatersList
	 * @return
	 */
	public static KayaBaseService excuteService(String serviceName, ArrayList<Paramaters> paramatersList) {
		return KayaFactory.createKayaService(serviceName, paramatersList);
	}

	public void setParamaters(Paramaters paramaters) {
		this.paramaterType = 2;
		this.paramaters = paramaters;
	}
	
	public Paramaters getParamaters() {
		return this.paramaters;
	}
	
	public ArrayList<Paramaters> getParamatersList() {
		return this.paramatersList;
	}

	public void setParamatersList(ArrayList<Paramaters> paramatersList) {
		this.paramaterType = 3;
		this.paramatersList = paramatersList;
	}

	public Paramater getParamater() {
		return this.paramater;
	}

	public void setParamater(Paramater paramater) {
		this.paramaterType = 1;
		this.paramater = paramater;
	}
	

	/**
	 * 获得检索结果
	 * @return
	 */
	public List<HashMap<String, Object>> getQueryresult() {
		return dao.getResultList();
	}
	/**
	 * 获得执行后的结果件数
	 * @return
	 */
	public int getCount() {
		return dao.getCount();
	}

	/**
	 * 业务规则（数据库操作前处理）
	 */
	public abstract void before();
	
	/**
	 * 执行数据库相关的业务处理（可覆盖此方法）
	 */
	public void excutBusiness(){

		switch (paramaterType) {
		case 1:
			dao.execute(paramater);
			break;
		case 2:
			dao.execute(paramaters);
			break;
		case 3:
			dao.execute(paramatersList);
			break;
		default :
			kayaLoger.error("This type of database is not supported!\n");
			break;
		}
	}
	
	/**
	 * 调用整体服务
	 */
	final public void operate() {
		before();
		excutBusiness();
		after();
	}
	
	/**
	 * 数据库操作后处理
	 */
	public abstract void after();
}
