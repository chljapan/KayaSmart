package com.smartkaya.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smartkaya.bean.Mapping;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.log.KayaLogManager;
import com.smartkaya.model.KayaMetaModel;

public class SqlTextUpdateEdit {
	// KayaLogManager
	private KayaLogManager kayaLoger = KayaLogManager.getInstance();
	
	protected SqlTextUpdateEdit() {
	}

	
	/**
	 * 单表单行删除（ByBusinessKey）
	 * @param paramater
	 * @return
	 */
	protected List<String> editUpdateSqlText(Paramater paramater) {

		List<String> sqlStringList = new ArrayList<String>();
		
		String kayaModelId= paramater.getId();
		getUpdateSqlString(paramater.getMapping(),kayaModelId,sqlStringList,paramater.getOrientationKey());
		return sqlStringList;
	}

	/**
	 * 单表多行删除（ByBusinessKeys）
	 * @param paramater
	 * @return
	 */
	protected List<String> editUpdateSqlText(Paramaters paramaters) {
		// Table存在确认
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();
		String kayaModelId= paramaters.getId();

		for(Mapping mapping:paramaters.getMappings()) {
			getUpdateSqlString(mapping,kayaModelId,sqlStringList,paramaters.getOrientationKey());
		}

		return sqlStringList;
	}
	
	
	/**
	 * 更新用SQL文编辑处理
	 * @param maping
	 * @param kayaModelId
	 * @param sqlStringList
	 * @param orientationKey
	 */
	private void getUpdateSqlString(Mapping maping,String kayaModelId,List<String> sqlStringList,String orientationKey){

		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
		StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET kindvalue = (CASE kind");
		StringBuilder updateBusinessIdSQL = new StringBuilder("");
		StringBuilder updateOrientationKeySQL = new StringBuilder("");
		StringBuilder updateUserInfoSQL = new StringBuilder("");

		// 如果是Role主节点
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			updateBusinessIdSQL.append(",businessid = (CASE kind");
		} else {
			updateBusinessIdSQL.append(",businesssubid = (CASE kind");
		}

		updateOrientationKeySQL.append(",orientationkey = (CASE kind");
		updateUserInfoSQL.append(",updatedate = {ts '" + (new Timestamp(System.currentTimeMillis())) + "'}");
		updateUserInfoSQL.append(",updateuser = '" + "kaya" + "'");

		StringBuilder updateWhere = new StringBuilder(" WHERE kind IN (");
		StringBuilder updateKeyOnlyWhere = new StringBuilder(" WHERE kind IN (");
		boolean flg = true;
		boolean keyOnlyflg = true;
		NewBusinessKey newBusinessKey = new NewBusinessKey();
		newBusinessKey.setFlg(false);
		for (KayaMetaModel kayaModel: kayaModelList) {
			// 参照的情况下，取参照元的KindKey，设置的时候利用参照本身的KindKey
			if (Constant.PROPERTYREF.equals(kayaModel.getMetaModelType())){
				// 只处理更新字段
				if (maping.getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))) {
					updateSQL.append(" WHEN '" 
							+ kayaModel.get(Constant.KINDKEY)
							+ "' THEN '" + maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "'");
					if(keyOnlyflg) {
						updateKeyOnlyWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
						keyOnlyflg = false;
					}else {
						updateKeyOnlyWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
					}

				}
				updateBusinessIdSQL.append(" WHEN '"   
						+ kayaModel.get(Constant.KINDKEY)  
						+ "' THEN '"+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey) + "'");
				updateOrientationKeySQL.append(" WHEN '"   
						+ kayaModel.get(Constant.KINDKEY)  
						+ "' THEN replace(orientationkey,"+ "'" + KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "'," 
						+ "'" + getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey)  + "')");
				if(flg) {
					updateWhere.append("'"  
							+ kayaModel.get(Constant.KINDKEY) 
							+ "'");
					flg = false;
				}else {
					updateWhere.append(",'"  
							+ kayaModel.get(Constant.KINDKEY) 
							+ "'");
				}
			} else {
				// 只处理更新字段
				if (maping.getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))) {
					updateSQL.append(" WHEN '" 
							+ kayaModel.get(Constant.KINDKEY) 
							+ "' THEN '" 
							+ maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "'");
					if(keyOnlyflg) {
						updateKeyOnlyWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
						keyOnlyflg = false;
					}else {
						updateKeyOnlyWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
					}
				}
				updateBusinessIdSQL.append(" WHEN '" 
						+ kayaModel.get(Constant.KINDKEY) 
						+ "' THEN '" 
						+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey) + "'");
				updateOrientationKeySQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY) 
						+ "' THEN replace(orientationkey,"+ "'" + KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "'," 
						+ "'" + getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey)  + "')");

				if(flg) {
					updateWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
					flg = false;
				}else {
					updateWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
				}
			}
		}
		updateSQL.append(" ELSE kindvalue END)");
		// 如果是Role主节点
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {			
			// 主键被更新
			if (newBusinessKey.isFlg()){
				updateSQL.append(updateBusinessIdSQL.toString() + " END)");
				updateSQL.append(updateOrientationKeySQL.toString() + " END)");
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(updateWhere.append(") AND businessid = '" 
						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
			} else {
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(updateKeyOnlyWhere.append(") AND businessid = '" 
						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
			}

		} else {			
			if (newBusinessKey.isFlg()){
				updateSQL.append(updateBusinessIdSQL.toString() + " END)");
				updateSQL.append(updateOrientationKeySQL.toString() + " END)");
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(updateWhere.append(") AND businessid = '" 
						//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),maping.getPropertys()) 
						+ orientationKey
						+ "' AND businesssubid = '" 
						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
			} else {
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(updateKeyOnlyWhere.append(") AND businessid = '" 
						//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),maping.getPropertys()) 
						+ orientationKey
						+ "' AND businesssubid = '" 
						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
			}
		}
		updateSQL.append(" AND parentid = '" + kayaModelId + "';");
		// 以多条SQL方式执行操作
		sqlStringList.add(updateSQL.toString());

		// OrientationKey更新（主键被更新时）
		if (newBusinessKey.isFlg()) {
			String _updateOrientationKeySQL = "UPDATE " + tableName + " SET orientationkey = REPLACE(orientationkey,'" 
					+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "','"
					+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey)
					+ "'),businessid = (CASE businessid WHEN '" + KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) 
					+ "' THEN '" + getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey) + "' ELSE businessid END)"
					+ updateUserInfoSQL.toString()
					+ " WHERE orientationkey LIKE '" + KayaModelUtils.getOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "%'";
			kayaLoger.info(_updateOrientationKeySQL);
			sqlStringList.add(_updateOrientationKeySQL);
		}
	}

	// Business文字列取得
	private String getNewBusinessKey(KayaMetaModel kayaMetaModel,Mapping mapping ,NewBusinessKey newBusinessKey){
		StringBuilder businessValue = new StringBuilder("^");
		Map<String,Object> subEntity = mapping.getPropertys();
		for (String businessStr:kayaMetaModel.getBusinessKeys()) {
			if (subEntity.containsKey(businessStr)) {
				businessValue.append(subEntity.get(businessStr) + "^");

				newBusinessKey.setFlg(true);;
			} else {
				businessValue.append(mapping.getKeys().get(businessStr) + "^");
			}
		}
		return businessValue.toString();
	}

	class NewBusinessKey{
		private boolean flg;
		private String businessKey;
		public boolean isFlg() {
			return flg;
		}
		public void setFlg(boolean flg) {
			this.flg = flg;
		}
		public String getBusinessKey() {
			return businessKey;
		}
		public void setBusinessKey(String businessKey) {
			this.businessKey = businessKey;
		}
	}
}
