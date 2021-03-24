package com.smartkaya.service;

import com.smartkaya.bean.Paramaters;

public abstract class KayaBaseService {
	private Paramaters paramaters;
	//这个参数是各个基本方法执行的顺序
	public static KayaBaseService excuteService(Paramaters paramaters) {
		return KayaFactory.createKayaService(paramaters);
	}
	
	public void setParamaters(Paramaters paramaters) {
		this.paramaters = paramaters;
	}
	
	public Paramaters getParamaters() {
		return paramaters;
	}
	
	public abstract void before();
	
	public void excutBusiness(){
		System.out.println("Super excutBusiness!");
	}
	
	public abstract void after();
	
	public void operate() {
		before();
		excutBusiness();
		after();
	}
}
