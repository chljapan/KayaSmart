package com.smartkaya.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.bean.Type;

/**
 * KaYamodel桥类
 * 
 * @author Wuzy 2020/9/21
 * @version 1.0.0
 */
public class BusinessProcessBridge {
	// 定义一个实行对象，抽象的了，不知道具体是什么
	private BusinessProcess implement;

	// 构造函数，由子类定义传递具体的进来
	public BusinessProcessBridge(BusinessProcess implement) {
		this.implement = implement;
	}

	/**
	 * 多表多条实行
	 * 
	 * @param paramatersList
	 */
	public void exe(List<Paramaters> paramatersList) {
		// 每个都是一样，先进行模型验证
		boolean checkModel = this.implement.checkModel(paramatersList);
		// 然后验证业务信息，子类可以不实装
		boolean checkInput = this.implement.checkInput(paramatersList);

		if (checkModel && checkInput) {
			// 数据库操作
			this.implement.exeSQL(paramatersList);
		}
	}

	/**
	 * 单表多条实行
	 * 
	 * @param paramatersList
	 */
	public void exe(Paramaters paramaters) {
		// 每个都是一样，先进行模型验证
		boolean checkModel = this.implement.checkModel(paramaters);
		// 然后验证业务信息，子类可以不实装
		boolean checkInput = this.implement.checkInput(paramaters);

		if (checkModel && checkInput) {
			// 数据库操作
			this.implement.exeSQL(paramaters);
		}
	}

	/**
	 * 单表单条实行
	 * 
	 * @param paramatersList
	 */
	public void exe(Paramater paramater) {
		// 每个都是一样，先进行模型验证
		boolean checkModel = this.implement.checkModel(paramater);
		// 然后验证业务信息，子类可以不实装
		boolean checkInput = this.implement.checkInput(paramater);

		if (checkModel && checkInput) {
			// 数据库操作
			this.implement.exeSQL(paramater);
		}
	}

	/**
	 * 检索实行
	 * 
	 * @param paramatersList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T search(Paramater paramater) {

		// 每个都是一样，先进行模型验证
		boolean checkModel = this.implement.checkModel(paramater);
		// 然后验证业务信息，子类可以不实装
		boolean checkInput = this.implement.checkInput(paramater);

		if (checkModel && checkInput) {
			// 数据库操作
			return this.implement.searchSQL(paramater);
		}
		if (paramater.getType().equals(Type.MUILTKEYINSUB)) {
			return (T) new HashMap<String, Object>();
		} else {
			return (T) new ArrayList<Map<String, String>>();
		}
	}

	/**
	 * WorkFlow单表单条实行
	 * 
	 * @param paramatersList
	 */
	public void exeWorkFlow(Paramater paramater) {
		// 每个都是一样，先进行模型验证
		boolean checkModel = this.implement.checkModel(paramater);
		// 然后验证业务信息，子类可以不实装
		boolean checkInput = this.implement.checkInput(paramater);

		if (checkModel && checkInput) {
			// 数据库操作
			this.implement.exeWorkFlow(paramater);
		}
	}

	/**
	 * WorkFlow单表多条实行
	 * 
	 * @param paramatersList
	 */
	public void exeWorkFlow(Paramaters paramaters) {
		// 每个都是一样，先进行模型验证
		boolean checkModel = this.implement.checkModel(paramaters);
		// 然后验证业务信息，子类可以不实装
		boolean checkInput = this.implement.checkInput(paramaters);

		if (checkModel && checkInput) {
			// 数据库操作
			this.implement.exeWorkFlow(paramaters);
		}
	}
}