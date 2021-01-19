package com.smartkaya.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.smartkaya.bean.Message;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.bean.Message.Lever;
import com.smartkaya.constant.Constant;

/**
 * 增删改事务处理
 * @author LiangChen　2020/8/18
 * @version 1.0.0
 */
public final class SqlExeCRUD extends AbstractSqlExE{

	SqlTextInsertEdit sqlTextInsertEdit = new SqlTextInsertEdit();
	SqlTextUpdateEdit sqlTextUpdateEdit = new SqlTextUpdateEdit();
	SqlTextSelectEdit sqlTextSelectEdit = new SqlTextSelectEdit();
	SqlTextDeleteEdit sqlTextDeleteEdit = new SqlTextDeleteEdit();
	
	@Override
	public Object exe(Paramater paramater) {
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)){
			return 0;
		}

		List<String> sqlStringList = new ArrayList<String>();

		switch (paramater.getCrud()) {
		case Constant.INSERT:
			dBConnection.executeBatch(sqlTextInsertEdit.editInsertSqlText(paramater));
			break;
		case Constant.UPDATE:
			dBConnection.executeBatch(sqlTextUpdateEdit.editUpdateSqlText(paramater));
			break;
		case Constant.SELECT:
			paramater.setOrientationKeySet(new HashSet<String>());
			
			// 任意字段，全文检索
			return dBConnection.executeQuery(sqlTextSelectEdit.selectByFullText(paramater), paramater.getOrientationKeySet());
		case Constant.DELETE:
			dBConnection.executeBatch(sqlTextDeleteEdit.editDeleteSqlText(paramater));
			break;
		default :
			kayaLoger.error("This type of crud is not supported!");
			paramater.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("CRUD");
			message.setMesage("Make sure that the CRUD parameters are correct.");
			paramater.setMessages(message);
			kayaLoger.warn(message);
			return 1;
		}

		// 批量执行SQL文
		return dBConnection.executeBatch(sqlStringList);
	}

	@Override
	public Object exe(Paramaters paramaters) {
		// Table存在确认 子表的时候需要处理
		if (!KayaModelUtils.checkTableId(paramaters)){
			return 0;
		}

		switch (paramaters.getCrud()) {
		case Constant.INSERT:
			dBConnection.executeBatch(sqlTextInsertEdit.editInsertSqlText(paramaters));
			break;
		case Constant.UPDATE:
				
			dBConnection.executeBatch(sqlTextUpdateEdit.editUpdateSqlText(paramaters));
			break;
		case Constant.SELECT:

			break;
		case Constant.DELETE:
			dBConnection.executeBatch(sqlTextDeleteEdit.editDeleteSqlText(paramaters));
			break;
		default :
			kayaLoger.error("This type of crud is not supported!");
			paramaters.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("CRUD");
			message.setMesage("Make sure that the CRUD parameters are correct.");
			paramaters.setMessages(message);
			kayaLoger.warn(message);
			return 1;
		}
		// 批量执行SQL文
		return 0;
	}

	@Override
	public Object exe(List<Paramaters> paramatersList) {
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();

		for (Paramaters paramaters: paramatersList) {
			// Table存在确认
			if (!KayaModelUtils.checkTableId(paramaters)){
				return 0;
			}

			switch (paramaters.getCrud()) {
			case Constant.INSERT:
				sqlStringList.addAll(sqlTextInsertEdit.editInsertSqlText(paramaters));
				break;
			case Constant.UPDATE:
				sqlStringList.addAll(sqlTextUpdateEdit.editUpdateSqlText(paramaters));
				break;
			case Constant.SELECT:
				
				break;
			case Constant.DELETE:
				sqlStringList.addAll(sqlTextDeleteEdit.editDeleteSqlText(paramaters));
				break;
			default :
				kayaLoger.error("This type of crud is not supported!");
				paramaters.setError(true);
				Message message = new Message();
				message.setLever(Lever.ERROR);
				message.setCode("CRUD");
				message.setMesage("Make sure that the CRUD parameters are correct.");
				paramaters.setMessages(message);
				kayaLoger.warn(message);
				return 1;
			}
			
		}
		// 批量执行SQL文
		return dBConnection.executeBatch(sqlStringList);
	}


}
