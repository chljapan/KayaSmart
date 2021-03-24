package com.smartkaya.service;

import com.smartkaya.bean.Paramaters;
import com.smartkaya.log.KayaLogManager;

public class KayaFactory {
	//private static HashMap<String,KayaBaseService> KayaBaseServices = new HashMap<String,KayaBaseService>();
	private static KayaLogManager kayaLoger = KayaLogManager.getInstance();
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
}
