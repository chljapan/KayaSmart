/**
 * 服务实例工厂
 */
package com.smartkaya.service;

import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.constant.Constant;
import com.smartkaya.log.KayaLogManager;

/**
 * 动态生成服务实例
* @author LiangChen　2021/3/26
* @version 1.0.0
 *
 */
public class KayaFactory {

	// 全局日志
	private static KayaLogManager kayaLoger = KayaLogManager.getInstance();

	/**
	 * 创建KayaService实例
	 * @param paramater
	 * @return
	 */
	public static KayaBaseService createKayaService(Paramater paramater){
		KayaBaseService kayaBaseService=null; //定义一个类
		try {
			if (StringUtils.isEmpty(paramater.getServicename())) {
				// KayaBaseService
				// kayaBaseService = (KayaBaseService)Class.forName(AccessKayaModel.getKayaBaseBusinessName()).newInstance();
				kayaBaseService = (KayaBaseService)Class.forName(Constant.KAYABASEBUSINESS).newInstance();
			} else {
				kayaBaseService = (KayaBaseService)Class.forName(paramater.getServicename()).newInstance();
			}

			// 提供子类使用的变量
			kayaBaseService.setParamater(paramater);
			// 调用服务
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

	/**
	 * 创建KayaService实例
	 * @param paramaters
	 * @return
	 */
	public static KayaBaseService createKayaService(Paramaters paramaters){
		KayaBaseService kayaBaseService=null; //定义一个类
		try {
			if (StringUtils.isEmpty(paramaters.getServicename())) {
				// KayaBaseService
				// kayaBaseService = (KayaBaseService)Class.forName(AccessKayaModel.getKayaBaseBusinessName()).newInstance();
				kayaBaseService = (KayaBaseService)Class.forName(Constant.KAYABASEBUSINESS).newInstance();

			} else {
				kayaBaseService = (KayaBaseService)Class.forName(paramaters.getServicename()).newInstance();
			}

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

	/**
	 * 创建KayaService实例
	 * @param serviceName
	 * @param paramatersList
	 * @return
	 */
	public static KayaBaseService createKayaService(String serviceName,ArrayList<Paramaters> paramatersList){
		KayaBaseService kayaBaseService=null; //定义一个类
		try {

			if (StringUtils.isEmpty(serviceName)) {
				// KayaBaseService
				// kayaBaseService = (KayaBaseService)Class.forName(AccessKayaModel.getKayaBaseBusinessName()).newInstance();
				kayaBaseService = (KayaBaseService)Class.forName(Constant.KAYABASEBUSINESS).newInstance();

			} else {
				kayaBaseService = (KayaBaseService)Class.forName(serviceName).newInstance();
			}
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

}
