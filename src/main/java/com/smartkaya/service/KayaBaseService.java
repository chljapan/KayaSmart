package com.smartkaya.service;

import java.util.ArrayList;
import java.util.List;

import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;

public abstract class KayaBaseService {
	private Paramaters paramaters;
	private List<Paramaters> paramatersList;
	private Paramater paramater;
	private List<Paramater> paramaterList;
	
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
		this.paramaters = paramaters;
	}
	
	public Paramaters getParamaters() {
		return paramaters;
	}
	
	public List<Paramaters> getParamatersList() {
		return paramatersList;
	}

	public void setParamatersList(List<Paramaters> paramatersList) {
		this.paramatersList = paramatersList;
	}

	public Paramater getParamater() {
		return paramater;
	}

	public void setParamater(Paramater paramater) {
		this.paramater = paramater;
	}

	public List<Paramater> getParamaterList() {
		return paramaterList;
	}

	public void setParamaterList(List<Paramater> paramaterList) {
		this.paramaterList = paramaterList;
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
