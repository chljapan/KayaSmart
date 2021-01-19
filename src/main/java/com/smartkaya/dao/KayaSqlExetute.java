package com.smartkaya.dao;

import java.util.List;

import com.smartkaya.bean.Message;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.bean.Message.Lever;
import com.smartkaya.constant.Constant;

public class KayaSqlExetute {

	private AbstractSqlExE abstractSqlExE;

	/**
	 * 执行SQL文
	 */
	public Object run(Paramater paramater) {
		switch (paramater.getCrud()) {
		case Constant.INSERT:
			this.abstractSqlExE = new SqlExeInsert();
			break;
		case Constant.UPDATE:
			this.abstractSqlExE = new SqlExeUpdate();
			break;
		case Constant.SELECT:
			this.abstractSqlExE = new SqlExeSelect();
			break;
		case Constant.DELETE:
			this.abstractSqlExE = new SqlExeDelete();
			break;
		default :
			abstractSqlExE.kayaLoger.error("This type of crud is not supported!");
			paramater.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("CRUD");
			message.setMesage("Make sure that the CRUD parameters are correct.");
			paramater.setMessages(message);
			abstractSqlExE.kayaLoger.warn(message);
		}
		return this.abstractSqlExE.exe(paramater);
	}

	public Object run(Paramaters paramaters) {
		switch (paramaters.getCrud()) {
		case Constant.INSERT:
			this.abstractSqlExE = new SqlExeInsert();
			break;
		case Constant.UPDATE:
			this.abstractSqlExE = new SqlExeUpdate();
			break;
		case Constant.SELECT:
			this.abstractSqlExE = new SqlExeSelect();
			break;
		case Constant.DELETE:
			this.abstractSqlExE = new SqlExeDelete();
			break;
		default :
			abstractSqlExE.kayaLoger.error("This type of crud is not supported!");
			paramaters.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("CRUD");
			message.setMesage("Make sure that the CRUD parameters are correct.");
			paramaters.setMessages(message);
			abstractSqlExE.kayaLoger.warn(message);
		}
		return this.abstractSqlExE.exe(paramaters);
	}

	public Object run(List<Paramaters> paramatersList) {
		this.abstractSqlExE = new SqlExeCRUD();
		return this.abstractSqlExE.exe(paramatersList);
	}

}
