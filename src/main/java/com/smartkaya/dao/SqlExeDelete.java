package com.smartkaya.dao;

import java.util.ArrayList;
import java.util.List;

import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;

/**
 * 删除处理
 * @author LiangChen
 *
 */
public final class SqlExeDelete extends AbstractSqlExE{
	SqlTextDeleteEdit sqlTextDeleteEdit = new SqlTextDeleteEdit();
	@Override
	public Integer exe(Paramater paramater) {
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)){
			return 0;
		}

		// 批量执行SQL文
		return dBConnection.executeBatch(sqlTextDeleteEdit.editDeleteSqlText(paramater));
	}

	@Override
	public Integer exe(Paramaters paramaters) {
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramaters)){
			return 0;
		}

		// 批量执行SQL文
		return dBConnection.executeBatch(sqlTextDeleteEdit.editDeleteSqlText(paramaters));
	}

	@Override
	public Integer exe(List<Paramaters> paramatersList) {
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();

		for (Paramaters paramaters:paramatersList) {
			// Table存在确认
			if (!KayaModelUtils.checkTableId(paramaters)){
				return 0;
			}
			sqlStringList.addAll(sqlTextDeleteEdit.editDeleteSqlText(paramaters));
		}

		return dBConnection.executeBatch(sqlStringList);
	}
}
