package com.smartkaya.dao;
import java.util.HashMap;

import com.smartkaya.bean.Message;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.bean.Message.Lever;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.model.KayaMetaModel;
/**
 * KaYamodel通用工具
 * @author LiangChen　2019/6/9
 * @version 1.0.0
 */
public class KayaModelUtils {
	public static final String selectString="SELECT gmeid,name,kind,kindtype,kindvalue,parentid,relid,orientationkey,flowcode,flowsubcode,startdate,enddate,withdrawaldate,createdate,createuser,updatedate,updateuser,lockflg,lockuser FROM ";
	public static final String selectWorkFlowString="SELECT gmeid,name,kind,kindtype,kindvalue,parentid,relid,orientationkey,flowcode,flowsubcode,startdate,enddate,withdrawaldate,createdate,createuser,updatedate,updateuser,lockflg,lockuser FROM ";
	public static final String selectStringMain="SELECT m.gmeid,m.name,m.kind,m.kindtype,m.kindvalue,m.parentid,m.businessid,m.businesssubid,m.orientationkey,m.startdate,m.enddate,m.withdrawaldate,m.createdate,m.createuser,m.updatedate,m.updateuser,m.lockflg,m.lockuser FROM ";
	/**
	 * Business文字列取得
	 * @param kayaMetaModel
	 * @param subEntity
	 * @return
	 */
	public static String getBusinessKey(KayaMetaModel kayaMetaModel,HashMap<String,Object> subEntity){
		StringBuilder businessValue = new StringBuilder("^");
		for (String businessStr:kayaMetaModel.getBusinessKeys()) {
			businessValue.append(subEntity.get(businessStr) + "^");
		}
		return businessValue.toString();
	}

	/**
	 * 关系Key 取得方法
	 * @param kayaMetaModel
	 * @param subEntity
	 * @return
	 */
	public static String getOrientationKey(KayaMetaModel kayaMetaModel,HashMap<String,Object> subEntity){
		StringBuilder businessValue = new StringBuilder("");
		if (Constant.G_ROLE.equals(AccessKayaModel.getParentKayaModel(kayaMetaModel.getGmeId()).getMetaModelType())) {
			for (String businessStr:kayaMetaModel.getBusinessKeys()) {
				businessValue.append(subEntity.get(businessStr) + "^");
			}
			businessValue.insert(0, getOrientationKey(AccessKayaModel.getParentKayaModel(kayaMetaModel.getGmeId()),subEntity));
		} else {
			businessValue.append("^");
			for (String businessStr:kayaMetaModel.getBusinessKeys()) {
				businessValue.append(subEntity.get(businessStr) + "^");
			}
		}

		return businessValue.toString();
	}
	
	
	/**
	 * 关系Key 取得方法
	 * @param kayaMetaModel
	 * @param subEntity
	 * @return
	 */
	public static String editOrientationKey(KayaMetaModel kayaMetaModel,String orientationKey,HashMap<String,Object> subEntity){
		StringBuilder businessValue = new StringBuilder("");
		// 主表
		if (!Constant.G_ROLE.equals(AccessKayaModel.getParentKayaModel(kayaMetaModel.getGmeId()).getMetaModelType())) {
			businessValue.append("^");
			for (String businessStr:kayaMetaModel.getBusinessKeys()) {
				businessValue.append(subEntity.get(businessStr) + "^");
			}
		// 子表
		} else {
			businessValue.insert(0, orientationKey);
			for (String businessStr:kayaMetaModel.getBusinessKeys()) {
				businessValue.append(subEntity.get(businessStr) + "^");
			}
		}

		return businessValue.toString();
	}
	
	
	/**
	 * 表存在Check
	 * @param paramater
	 * @return
	 */
	public static boolean checkTableId(Paramater paramater){
		String kayaModelId = paramater.getId();
		if (AccessKayaModel.getKayaModelId(kayaModelId)==null) {
			paramater.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("10001");
			message.setMesage(Constant.MSG_TABLECHECK);
			paramater.setMessages(message);
		}
		return AccessKayaModel.getKayaModelId(kayaModelId)==null?false:true;
	}
	/**
	 * 表存在Check
	 * @param paramaters
	 * @return
	 */
	public static boolean checkTableId(Paramaters paramaters){
		String kayaModelId = paramaters.getId();
		if (AccessKayaModel.checkTable(kayaModelId)) {
			paramaters.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("10001");
			message.setMesage(Constant.MSG_TABLECHECK);
			paramaters.setMessages(message);
		}
		return AccessKayaModel.checkTable(kayaModelId);
	}
	
	// Insert 字符串编辑方法
	public static StringBuilder getInsertSql(String tableName){
		StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " ("
				+ "businessid,businesssubid,orientationkey,relid,gmeid,kind,name,kindvalue,kindtype,securitycode,"
				+ "flowcode,flowsubcode,startdate,enddate,withdrawaldate,parentid,createdate,createuser,updatemachine"
				+ ") VALUES ");
		return insertSQL;
	}
	
	// Insert 字符串编辑方法
	public static StringBuilder getWorkFlowInsertSql(String tableName){
		StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " ("
				+ "orientationkey,relid,gmeid,kind,name,kindvalue,kindtype,securitycode,"
				+ "flowcode,flowsubcode,startdate,enddate,withdrawaldate,parentid,createdate,createuser,updatemachine"
				+ ") VALUES ");
		return insertSQL;
	}
	
	// Insert 字符串编辑方法
	public static StringBuilder getWorkFlowHistorySql(String tableName){
		StringBuilder insertSQL = new StringBuilder("SELECT * from" + tableName + " ");
		return insertSQL;
	}
}
