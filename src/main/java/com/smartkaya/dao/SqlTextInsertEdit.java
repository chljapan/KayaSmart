package com.smartkaya.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.smartkaya.bean.Mapping;
import com.smartkaya.bean.Message;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.bean.Message.Lever;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.log.KayaLogManager;
import com.smartkaya.model.KayaMetaModel;
import com.smartkaya.script.ScriptEXE;
import com.smartkaya.utils.UtilTools;

public class SqlTextInsertEdit {
	// KayaLogManager
	private KayaLogManager kayaLoger = KayaLogManager.getInstance();
	
	protected SqlTextInsertEdit() {
	}

	
	/**
	 * InsertSQL文编辑处理
	 * @param paramater
	 * @return List<String> SQL文List
	 */
	protected List<String> editInsertSqlText(Paramater paramater) {

		String kayaModelId = paramater.getId();
		// 取得Role信息
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();
		StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);
		boolean flg = true;
		getInsertSqlString(paramater.getMapping(), kayaModelId, insertSQL, paramater.getOrientationKey(),flg);
		insertSQL.append(";");
		sqlStringList.add(insertSQL.toString());

		// 监听业务流程处理
		if (!Constant.EMPTY.equals(AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId())) {
			// TODO:验证流程ID
			sqlStringList.add(getWorkflowRoleSqlString(paramater.getId(),AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId(),paramater.getActionid(),paramater.getOrientationKey(),paramater.getMapping()).toString());
		}
		kayaLoger.info(insertSQL);
		return sqlStringList;
	}

	/**
	 * InsertSQL文编辑处理
	 * @param paramaters
	 * @return List<String> SQL文List
	 */
	protected List<String> editInsertSqlText(Paramaters paramaters){

		List<String> sqlStringList = new ArrayList<String>();

		String kayaModelId = paramaters.getId();
		// 取得Role子元素信息
		//	List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
		// 取得Role信息
		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
		String tableName = kayaMetaModel.getTableId().replace('-','_');
		StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);

		boolean flg = true;
		for (Mapping subEntity:paramaters.getMappings()) {
			getInsertSqlString(subEntity,kayaModelId,insertSQL,paramaters.getOrientationKey(),flg);

			// 多条编辑","处理
			flg = false;
			String workFlowId = AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId();
			if (!Constant.EMPTY.equals(workFlowId) && workFlowId != null) {
				// TODO:验证流程ID
				String workflowSql = getWorkflowRoleSqlString(paramaters.getId(),workFlowId, paramaters.getActionid(),paramaters.getOrientationKey(),subEntity);

				if (Constant.EMPTY.equals(workflowSql)) {
					paramaters.setError(true);
					Message message = new Message();
					message.setLever(Lever.ERROR);
					message.setCode("10001");
					message.setMesage("请确认ActionID，流程状态异常！");
					paramaters.setMessages(message);
					kayaLoger.warn(message);

				} else {
					sqlStringList.add(workflowSql);
				}
			}
		}
		insertSQL.append(";");

		kayaLoger.info(insertSQL);
		sqlStringList.add(insertSQL.toString());
		return sqlStringList;
	}

	/**
	 * Insert SQL文编辑处理
	 * @param maping
	 * @param kayaModelId
	 * @param insertSQL
	 * @param orientationKey
	 * @param flg
	 */
	private void getInsertSqlString(Mapping maping,String kayaModelId, StringBuilder insertSQL,String orientationKey,boolean flg){
		// 取得Role子元素信息
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);

		// 取得Role信息
		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);

		// 主键编辑处理
		for (KayaMetaModel kayaModel: kayaModelList) {
			// 自增项处理
			if (Constant.AUTO.equals(kayaModel.get(Constant.DATATYPE))) {
				maping.setProperty(kayaModel.get(Constant.KINDKEY), UtilTools.getOrderNo());
			}
		}

		for (KayaMetaModel kayaModel: kayaModelList) {
			// gmeid
			if(flg) {
				insertSQL.append("(");
				flg = false;
			}else {
				insertSQL.append(",(");
			}

			// businessid
			// businesssubid
			// 父类型是Product的时候判定为主表
			if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())){
				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,maping.getPropertys()) + "',");
				insertSQL.append("'',"); 
				// orientationkey
				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,maping.getPropertys()) + "',");
				// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
			} else {
				insertSQL.append("'" + orientationKey + "',");
				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,maping.getPropertys()) + "',");
				// orientationkey
				// insertSQL.append("'" + KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),KayaModelUtils.getBusinessKey(kayaParentMetaModel,maping.getPropertys()),maping.getPropertys())+ "',");
				insertSQL.append("'" + KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),orientationKey,maping.getPropertys())+ "',");
			}

			// relid
			insertSQL.append("'" + UtilTools.getOrderNo() + "',");
			// kind
			// name
			// kindValue
			// 参照的情况下，取参照元的KindKey，设置的时候利用参照本身的KindKey
			//			if (Constant.PROPERTYREF.equals(kayaModel.getMetaModelType())){
			//				insertSQL.append("'" + kayaModel.get(Constant.REFERRED) + "',");
			//			} else {
			insertSQL.append("'" + kayaModel.getGmeId() + "',");
			//			}
			insertSQL.append("'" + kayaModel.get(Constant.KINDKEY) + "',");
			insertSQL.append("'" + kayaModel.getName() + "',");

			if(maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) == null) {
				insertSQL.append("'',");
			} else {
				insertSQL.append("'" + maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "',");
			}

			// kindtype
			insertSQL.append("'" + kayaModel.getMetaModelType() + "',");
			// securitycode
			// flowcode
			// flowsubcode
			insertSQL.append("'',");
			insertSQL.append("'',");
			insertSQL.append("'',");
			// startdate
			// enddate
			// withdrawaldate
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			// parentid
			insertSQL.append("'" + kayaModelId + "',");
			// createdate
			// createuser
			//insertSQL.append("'" + (new Timestamp(System.currentTimeMillis())) + "',");
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			insertSQL.append("'chenliang',");
			// updatedate
			//				insertSQL.append("'',");
			// updateuser
			// updatemachine
			//				insertSQL.append("null" + ",");
			insertSQL.append("'" + "')");
		}

	}

	/**
	 * 业务流SQL文编辑处理(关联相关表启动该流程)
	 * @param kayaModelId
	 * @param workflowId
	 * @param actionId
	 * @param orientationKey
	 * @param mapping
	 * @return
	 */
	private String getWorkflowRoleSqlString(String kayaModelId, String workflowId, String actionId,String orientationKey,Mapping mapping) {
		StringBuilder insertSQL = new StringBuilder("");
		String orderNo = UtilTools.getOrderNo();
		// 判断Action是否符合自身流程要求
		KayaMetaModel kayaMetaWorkFlowModel = AccessKayaModel.getKayaModelId(workflowId);
		// 取得业务流开始元素
		String startUserTaskId = AccessKayaModel.getWorkFlowConnectionDes(kayaMetaWorkFlowModel.get(Constant.START));
		if (!AccessKayaModel.getParentId(actionId).equals(startUserTaskId)) {
			return "";
		}

		// 取得Role信息
		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
		// NextStape 
		String nextWorkFlowId = "";
		nextWorkFlowId = AccessKayaModel.getWorkFlowConnectionDes(actionId);
		if (Constant.E_GateWay.equals(AccessKayaModel.getKayaModelId(nextWorkFlowId).getMetaModelType()) ) {
			//			ScriptEXE scriptExE = new ScriptEXE();
			// 如果返回GetWay本身ID,则申请条件异常（设置的分歧条件没有覆盖所有场合）
			if (nextWorkFlowId.equals(ScriptEXE.Exe(nextWorkFlowId, mapping.getPropertys()))) {
				// TODO： 通知前台系统管理员修改分歧条件，覆盖所有业务场景
			} else {
				nextWorkFlowId = AccessKayaModel.getWorkFlowConnectionDes(ScriptEXE.Exe(nextWorkFlowId, mapping.getPropertys()));
			}
		}

		// 取得WorkFlow信息
		String tableName = kayaMetaWorkFlowModel.getTableId().replace('-','_');
		insertSQL = KayaModelUtils.getInsertSql(tableName);
		insertSQL.append("(");
		// businessid
		// businesssubid
		// 表ID等于自身ID的时候判定为主表
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())){
			insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,mapping.getPropertys()) + "',");
			insertSQL.append("'',");
			// orientationkey
			insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,mapping.getPropertys()) + "',");
			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
		} else {
			//				insertSQL.append("'" + KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),mapping.getPropertys()) + "',");
			insertSQL.append("'" + orientationKey + "',");
			insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,mapping.getPropertys()) + "',");
			// orientationkey
			//				insertSQL.append("'" + KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),mapping.getPropertys()),mapping.getPropertys()) + "',");
			insertSQL.append("'" + KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),orientationKey,mapping.getPropertys()) + "',");
		}

		// relid
		insertSQL.append("'" + orderNo + "',");

		// gmeid
		// kind
		// name
		// kindValue

		insertSQL.append("'" + kayaMetaModel.getGmeId() + "',");
		insertSQL.append("'" + kayaMetaModel.get(Constant.KINDKEY) + "',");
		insertSQL.append("'" + kayaMetaModel.getName() + "',");


		// UserTaskID(人为启动流程？还是自动触发流程？)
		//		insertSQL.append("'" + AccessKayaModel.getKayaModelId(workFlowListener.getActionId()).getParentId() + "',");
		insertSQL.append("'" + AccessKayaModel.getKayaModelId(actionId).getName() + "',");

		// kindtype
		insertSQL.append("'" + kayaMetaModel.getMetaModelType() + "',");
		// securitycode
		// flowcode

		// flowsubcode
		insertSQL.append("'',");
		insertSQL.append("'" + nextWorkFlowId + "',");
		insertSQL.append("'" + actionId + "',");
		// startdate
		// enddate
		// withdrawaldate
		insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
		insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
		insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");

		// parentid   申请ID
		insertSQL.append("'" + kayaModelId + "',");
		// createdate
		// createuser
		//		insertSQL.append("'" + (new Timestamp(System.currentTimeMillis())) + "',");
		insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
		insertSQL.append("'chenliang',");
		// updatedate
		//			insertSQL.append("'',");
		// updateuser
		// updatemachine
		//			insertSQL.append("'" + "',");
		//		insertSQL.append("'" + "')");
		//		// Action row insert
		//		insertSQL.append(",(");
		//		

		insertSQL.append("'" + "')");


		// 取得Role子元素信息
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaWorkFlowAction(actionId);
		for (KayaMetaModel kayaModel: kayaModelList) {
			// gmeid
			insertSQL.append(",(");

			// businessid
			// businesssubid
			// 表ID等于自身ID的时候判定为主表
			//if (kayaMetaModel.getTableId().equals(kayaModelId)){
			if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())){

				// Role主键处理
				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,mapping.getPropertys()) + "',");
				insertSQL.append("'',"); 
				// orientationkey
				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,mapping.getPropertys()) + "',");
				// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
			} else {
				insertSQL.append("'" + orientationKey + "',");
				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,mapping.getPropertys()) + "',");
				// orientationkey
				insertSQL.append("'" + KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),orientationKey,mapping.getPropertys())+ "',");
			}

			// relid
			insertSQL.append("'" + orderNo + "',");

			// kind
			// name
			// kindValue
			// 参照的情况下，取参照元的KindKey，设置的时候利用参照本身的KindKey
			if (Constant.PROPERTYREF.equals(kayaModel.getMetaModelType())){
				insertSQL.append("'" + kayaModel.get(Constant.REFERRED) + "',");
			} else {
				insertSQL.append("'" + kayaModel.getGmeId() + "',");
			}

			insertSQL.append("'" + kayaModel.get(Constant.KINDKEY) + "',");
			insertSQL.append("'" + kayaModel.getName() + "',");
			if (Constant.ACTION.equals(kayaModel.getMetaModelType())) {
				insertSQL.append("'" + AccessKayaModel.getParentKayaModel(actionId).getName() + "',");
			} else {
				insertSQL.append("'" + mapping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "',");
			}

			// kindtype
			insertSQL.append("'" + kayaModel.getMetaModelType() + "',");
			// securitycode
			// flowcode
			// flowsubcode
			insertSQL.append("'',");
			insertSQL.append("'" + kayaModel.getParentId() + "',");
			insertSQL.append("'" + actionId + "',");
			// startdate
			// enddate
			// withdrawaldate
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			// parentid
			insertSQL.append("'" + kayaModelId + "',");
			// createdate
			// createuser
			//insertSQL.append("'" + (new Timestamp(System.currentTimeMillis())) + "',");
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			insertSQL.append("'chenliang',");
			// updatedate
			//				insertSQL.append("'',");
			// updateuser
			// updatemachine
			insertSQL.append("'" + "')");
		}
		insertSQL.append(";");
		kayaLoger.info(insertSQL.toString());
		return insertSQL.toString();
	}
}
