package com.smartkaya.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.dao.KayaSQLExecute;
import com.smartkaya.log.KayaLogManager;

public abstract class KayaBaseService {
	private Paramaters paramaters;
	private ArrayList<Paramaters> paramatersList;
	private Paramater paramater;
//	private List<HashMap<String, Object>> queryresult;// 查询结果
//	private int count = 0;
	public KayaLogManager kayaLoger = KayaLogManager.getInstance();
	private int paramaterType = 0;// 识别参数临时变量
	
	public KayaSQLExecute dao = new KayaSQLExecute();
	//这个参数是各个基本方法执行的顺序
	public static KayaBaseService excuteService(Paramaters paramaters) {
		return KayaFactory.createKayaService(paramaters);
	}
	public static KayaBaseService excuteService(String serviceName, ArrayList<Paramaters> paramatersList) {
		return KayaFactory.createKayaService(serviceName, paramatersList);
	}
	public static KayaBaseService excuteService(Paramater paramater) {
		return KayaFactory.createKayaService(paramater);
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
	


	public List<HashMap<String, Object>> getQueryresult() {
		return dao.getResultList();
	}
//	void setQueryresult(List<HashMap<String, Object>> queryresult) {
//		this.queryresult = queryresult;
//	}
	public int getCount() {
		return dao.getCount();
	}
//	void setCount(int count) {
//		this.count = count;
//	}
	/**
	 * 业务规则（数据库操作前处理）
	 */
	public abstract void before();
	
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
	 * 
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
