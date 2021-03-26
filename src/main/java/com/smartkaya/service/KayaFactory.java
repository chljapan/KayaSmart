package com.smartkaya.service;

import java.util.ArrayList;

import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.log.KayaLogManager;

public class KayaFactory {
	//private static HashMap<String,KayaBaseService> KayaBaseServices = new HashMap<String,KayaBaseService>();
	private static KayaLogManager kayaLoger = KayaLogManager.getInstance();
	
	public static KayaBaseService createKayaService(Paramater paramater){
		KayaBaseService kayaBaseService=null; //定义一个类
		try {

			String className = paramater.getServicename();
			if (Constant.EMPTY.equals(paramater.getServicename().trim())) {
				// KayaBaseService
				kayaBaseService = (KayaBaseService)Class.forName(AccessKayaModel.getKayaBaseBusinessName()).newInstance();

			} else {
				kayaBaseService = (KayaBaseService)Class.forName(className).newInstance(); //TODO:Packagepath 需要在参数中动态提取
			}

			kayaBaseService.setParamater(paramater);
			kayaBaseService.operate();
		} catch (InstantiationException e) {//必须指定服务类
			kayaLoger.error("The service class must be specified!");
		} catch (IllegalAccessException e) { //服务类定义错误

			kayaLoger.error("The service class is incorrectly defined!");
		} catch (ClassNotFoundException e) { //找不到服务类
			kayaLoger.error("The service class could not be found!");
		}

		return kayaBaseService;
	}
	
	public static KayaBaseService createKayaService(Paramaters paramaters){
		KayaBaseService kayaBaseService=null; //定义一个类
		try {
			String className = paramaters.getServicename();
			kayaBaseService = (KayaBaseService)Class.forName(className).newInstance(); //TODO:Packagepath 需要在参数中动态提取
			kayaBaseService.setParamaters(paramaters);
			kayaBaseService.operate();
			
		} catch (InstantiationException e) {//必须指定服务类
			kayaLoger.error("The service class must be specified!");
		} catch (IllegalAccessException e) { //服务类定义错误

			kayaLoger.error("The service class is incorrectly defined!");
		} catch (ClassNotFoundException e) { //找不到服务类
			kayaLoger.error("The service class could not be found!");
		}
		return kayaBaseService;
	}

	
	public static KayaBaseService createKayaService(String serviceName,ArrayList<Paramaters> paramatersList){
		KayaBaseService kayaBaseService=null; //定义一个类
		try {

			kayaBaseService = (KayaBaseService)Class.forName(serviceName).newInstance(); //TODO:Packagepath 需要在参数中动态提取
			kayaBaseService.setParamatersList(paramatersList);
			kayaBaseService.operate();
			
		} catch (InstantiationException e) {//必须指定服务类
			kayaLoger.error("The service class must be specified!");
		} catch (IllegalAccessException e) { //服务类定义错误

			kayaLoger.error("The service class is incorrectly defined!");
		} catch (ClassNotFoundException e) { //找不到服务类
			kayaLoger.error("The service class could not be found!");
		}
		return kayaBaseService;
	}
	

	
//	public static KayaBaseService createKayaService(String servicename, ArrayList<Paramater> paramaterList){
//		KayaBaseService kayaBaseService=null; //定义一个类
//		try {
//
//			kayaBaseService = (KayaBaseService)Class.forName(servicename).newInstance(); //TODO:Packagepath 需要在参数中动态提取
//			kayaBaseService.setParamaterList(paramaterList);
//			kayaBaseService.operate();
//			
//		} catch (InstantiationException e) {//必须指定服务类
//			kayaLoger.error("The service class must be specified!");
//		} catch (IllegalAccessException e) { //服务类定义错误
//
//			kayaLoger.error("The service class is incorrectly defined!");
//		} catch (ClassNotFoundException e) { //找不到服务类
//			kayaLoger.error("The service class could not be found!");
//		}
//		return kayaBaseService;
//	}
}
