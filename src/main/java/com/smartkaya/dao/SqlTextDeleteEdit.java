package com.smartkaya.dao;

import java.util.ArrayList;
import java.util.List;

import com.smartkaya.bean.Mapping;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.log.KayaLogManager;

public class SqlTextDeleteEdit {
	// KayaLogManager
	private KayaLogManager kayaLoger = KayaLogManager.getInstance();
	
	protected SqlTextDeleteEdit() {
	}

	
	/**
	 * 单表单行删除（ByBusinessKey）
	 * @param paramater
	 * @return
	 */
	protected List<String> editDeleteSqlText(Paramater paramater) {

		List<String> sqlStringList = new ArrayList<String>();
		
		String kayaModelId= paramater.getId();
		editSqlTextdelete(paramater.getMapping(),kayaModelId,sqlStringList,paramater.getOrientationKey());
		return sqlStringList;
	}

	/**
	 * 单表多行删除（ByBusinessKeys）
	 * @param paramater
	 * @return
	 */
	protected List<String> editDeleteSqlText(Paramaters paramaters) {
		// Table存在确认
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();
		String kayaModelId= paramaters.getId();

		for(Mapping mapping:paramaters.getMappings()) {
			editSqlTextdelete(mapping,kayaModelId,sqlStringList,paramaters.getOrientationKey());
		}

		return sqlStringList;
	}
	
	
	/**
	 * Delete SQL文编辑
	 * @param paramater
	 * @return
	 */
	private void editSqlTextdelete(Mapping maping,String kayaModelId,List<String> sqlStringList,String orientationKey) {
		
		
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
		StringBuilder deleteSQL = new StringBuilder("DELETE "
				+ "FROM " + tableName);
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())){
			// orientationkey
			orientationKey =  KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys());
			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
		} else {
			// orientationkey
			orientationKey =   KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),orientationKey,maping.getPropertys());
			
		}
		
		deleteSQL.append(" WHERE orientationkey like '" + orientationKey + "%' AND parentid='" + kayaModelId + "';");
		kayaLoger.info(deleteSQL);
		sqlStringList.add(deleteSQL.toString());
	}

}
