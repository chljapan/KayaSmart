package com.smartkaya.dao;

import java.util.ArrayList;
import java.util.List;

import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;

public final class SqlExeUpdate extends AbstractSqlExE{
	SqlTextUpdateEdit sqlTextUpdateEdit = new SqlTextUpdateEdit();
	
	@Override
	public Integer exe(Paramater paramater) {

		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)){
			return 0;
		}
		
		return dBConnection.executeBatch(sqlTextUpdateEdit.editUpdateSqlText(paramater));
	}

	@Override
	public Integer exe(Paramaters paramaters) {
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramaters)){
			return 0;
		}

		return dBConnection.executeBatch(sqlTextUpdateEdit.editUpdateSqlText(paramaters));
	}

	@Override
	public Integer exe(List<Paramaters> paramatersList) {
		List<String> sqlStringList = new ArrayList<String>();
		for (Paramaters paramaters:paramatersList) {
			// Table存在确认
			if (!KayaModelUtils.checkTableId(paramaters)){
				return 0;
			}

			sqlStringList.addAll(sqlTextUpdateEdit.editUpdateSqlText(paramaters));
		}
		return dBConnection.executeBatch(sqlStringList);
	}
}
