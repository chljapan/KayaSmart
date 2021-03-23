package com.smartkaya.service;

import com.smartkaya.bean.Paramaters;

public class KayaFactory {
	//private static HashMap<String,KayaBaseService> KayaBaseServices = new HashMap<String,KayaBaseService>();
	
	//定一个烤箱，泥巴塞进去，人就出来，这个太先进了
	public static KayaBaseService createKayaService(Paramaters paramaters){
		KayaBaseService kayaBaseService=null; //定义一个类型的人类
		try {
			String className = paramaters.getId().replace("-", "");
			kayaBaseService = (KayaBaseService)Class.forName("com.smartkaya.business." + className).newInstance(); //产生一个人类
			kayaBaseService.setParamaters(paramaters);
			
		} catch (InstantiationException e) {//你要是不说个人类颜色的话，没法烤，要白的黑，你说话了才好烤
			System.out.println("必须指定服务类");
		} catch (IllegalAccessException e) { //定义的人类有问题，那就烤不出来了，这是...

			System.out.println("服务类定义错误！");
		} catch (ClassNotFoundException e) { //你随便说个人类，我到哪里给你制造去？！
			System.out.println("找不到服务类！");
		}
		return kayaBaseService;
	}
}
