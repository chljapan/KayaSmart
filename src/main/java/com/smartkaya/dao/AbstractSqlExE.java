package com.smartkaya.dao;

import java.util.List;

import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.core.DbConnection;
import com.smartkaya.log.KayaLogManager;

public abstract class AbstractSqlExE {

	public AbstractSqlExE() {
		// TODO Auto-generated constructor stub
	}

	/*** DBConnection ***/
	DbConnection dBConnection = AccessKayaModel.getDbConnection();
	/*** KayaLogManager ***/
	KayaLogManager kayaLoger = KayaLogManager.getInstance();

	/**.
	 * 执行SQL文（单表单条）
	 * @param paramater
	 * @return
	 */
	public abstract Object exe(Paramater paramater);

	/**
	 * 执行SQL文（单表多条）
	 * @param paramaters
	 * @return
	 */
	public abstract Object exe(Paramaters paramaters);

	/**
	 * 执行SQL文（多表多条）
	 * @param paramatersList
	 * @return
	 */
	public abstract Object exe(List<Paramaters> paramatersList);


}
