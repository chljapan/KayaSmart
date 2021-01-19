package com.smartkaya.dao;

import java.util.ArrayList;
import java.util.List;

import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;

/**
 * Insert处理
 * @author LiangChen　2020/8/18
 * @version 1.0.0
 */
public final class SqlExeInsert extends AbstractSqlExE{

	SqlTextInsertEdit sqlTextInsertEdit = new SqlTextInsertEdit();
	@Override
	public Object exe(Paramater paramater) {
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)){
			return 0;
		}
		// 批量执行SQL文
		return dBConnection.executeBatch(sqlTextInsertEdit.editInsertSqlText(paramater));
	}

	@Override
	public Object exe(Paramaters paramaters) {
		// Table存在确认 子表的时候需要处理
		if (!KayaModelUtils.checkTableId(paramaters)){
			return 0;
		}
		// 批量执行SQL文
		return dBConnection.executeBatch(sqlTextInsertEdit.editInsertSqlText(paramaters));
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
			
			sqlStringList.addAll(sqlTextInsertEdit.editInsertSqlText(paramaters));
		}
		// 批量执行SQL文
		return dBConnection.executeBatch(sqlStringList);
	}


}
