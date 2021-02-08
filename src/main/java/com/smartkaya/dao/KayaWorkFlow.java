package com.smartkaya.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.smartkaya.api.utils.StringUtil;
import com.smartkaya.bean.Mapping;
import com.smartkaya.bean.Message;
import com.smartkaya.bean.Message.Lever;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.core.DbConnection;
import com.smartkaya.core.ParseKayaModel_XPATH;
import com.smartkaya.entity.KayaEntity;
import com.smartkaya.model.KayaMetaModel;
import com.smartkaya.model.KayaModelPermissionsItem;
import com.smartkaya.script.ScriptEXE;
import com.smartkaya.user.User;
import com.smartkaya.utils.UtilTools;

/**
 * KaYaWorkFlow 操作类
 * 
 * @author LiangChen 2019/6/9
 * @version 1.0.0
 */
public final class KayaWorkFlow {

	private DbConnection dBConnection = AccessKayaModel.getDbConnection();
	Properties properties = ParseKayaModel_XPATH.getProperties("kayaconfig.properties");
	// 读取必要的表
	String roleInfoKey = properties.getProperty(Constant.P_ROLES);

	public KayaWorkFlow() {
		// TODO:不需要？
		//new AccessKayaModel();
	}

	/**
	 * 单条流程+插入处理 Single-table single-row insert processing
	 * 
	 * @param paramater
	 * @return
	 */
	public int excuteKayaWorkFlow(Paramater paramater) {
		// WorkFlow处理类型判断
		if (!Constant.WORKFLOW.equals(paramater.getCrud())) {
			paramater.setError(true);
			paramater.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("10001");
			message.setMesage("Please make sure the parameters of 'CRUD' are correct.");
			paramater.setMessages(message);
			return 0;
			// 开始WorkFlow处理（参数WorkFlowID的场合）
		}
		// TODO: 验证目前的WorkFlow状态（确认该Action属于本流程的下一个状态）

		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)) {
			return 0;
		}
		// 判断Action是否符合自身流程要求

		String actionId = paramater.getActionid();

		String kayaModelId = paramater.getId();
		HashMap<String, String> businessKeyMap = new HashMap<String, String>();

		// 取得Role信息
		String tableName = AccessKayaModel.getKayaModelId(actionId).getTableId();
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();
		// Action insert SQLString
		StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);

		// Role Upadate SQLString
		StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET ");
		User userInfo = paramater.getUsrinfo();
		getInsertSqlString(paramater.getMapping(), kayaModelId, actionId, insertSQL, updateSQL, businessKeyMap,
				userInfo);
		insertSQL.append(";");

		// 更新状态成功的话，插入Action记录
		if (dBConnection.executeUpdate(updateSQL.toString()) > 0) {
			sqlStringList.add(insertSQL.toString());
			paramater.setError(false);
			Message message = new Message();
			message.setLever(Lever.INFO);
			message.setCode("10001");
			message.setMesage("Execute successfully");
			paramater.setMessages(message);
		} else {
			paramater.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("10001");
			message.setMesage("Please confirm the status of the process.");
			paramater.setMessages(message);
		}
		dBConnection.executeBatch(sqlStringList);
		System.out.println(insertSQL.toString());
		System.out.println(updateSQL.toString());

		return 0;

	}

	/**
	 * 单条流程+插入处理 Single-table single-row insert processing
	 * 
	 * @param userInfo
	 * @param paramater
	 * @return
	 */
	public int excuteKayaWorkFlow(Paramaters paramaters) {
		// WorkFlow处理类型判断
		if (!Constant.WORKFLOW.equals(paramaters.getCrud())) {
			paramaters.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("10001");
			message.setMesage("Please confirm the status of the process.");
			paramaters.setMessages(message);
			return 0;
			// 开始WorkFlow处理（参数WorkFlowID的场合）
		}
		// TODO： 权限控制处理

		// TODO:验证目前的WorkFlow状态（确认该Action属于本流程的下一个状态）

		String actionId = paramaters.getActionid();

		String kayaModelId = paramaters.getId();

		HashMap<String, String> businessKeyMap = paramaters.getBusinessKeyMap();
		businessKeyMap.put(Constant.ORIENTATIONKEY, paramaters.getOrientationKey());
		// 取得Role信息
		String tableName = AccessKayaModel.getKayaModelId(actionId).getTableId();

		// SqlList
		List<String> sqlStringList = new ArrayList<String>();
		// Action insert SQLString
		StringBuilder insertSQL = new StringBuilder("");

		// Role Upadate SQLString
		StringBuilder updateSQL = new StringBuilder("");
		User userInfo = paramaters.getUsrinfo();

		for (Mapping subEntity : paramaters.getMappings()) {
			// Role Upadate SQLString
			insertSQL = KayaModelUtils.getInsertSql(tableName);
			updateSQL = new StringBuilder("UPDATE " + tableName + " SET ");
			getInsertSqlString(subEntity, kayaModelId, actionId, insertSQL, updateSQL, businessKeyMap, userInfo);
			// 多条编辑","处理
			insertSQL.append(";");

			if (dBConnection.executeUpdate(updateSQL.toString()) > 0) {
				sqlStringList.add(insertSQL.toString());
				paramaters.setError(false);
				Message message = new Message();
				message.setLever(Lever.INFO);
				message.setCode("success");
				message.setMesage("Execute successfully");
				paramaters.setMessages(message);
			} else {
				paramaters.setError(true);
				Message message = new Message();
				message.setLever(Lever.ERROR);
				message.setCode("10001");
				message.setMesage("Please confirm the status of the process.");
				paramaters.setMessages(message);
			}
			System.out.println(insertSQL.toString());
			System.out.println(updateSQL.toString());
		}
		dBConnection.executeBatch(sqlStringList);
		return 0;

	}

	public List<Map<String, String>> getKayaModelInfo(Paramater paramater) {
		List<Map<String, String>> kayaEntityList = new ArrayList<Map<String, String>>();
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)) {
			return kayaEntityList;
		}
		String kayaModelId = paramater.getId();
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
		String businessId = KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),
				paramater.getMapping().getKeys());
		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
		if (AccessKayaModel.getKayaModelId(kayaModelId).getTableId().equals(kayaModelId)) {
			selectSQL.append(" WHERE businessid = '" + businessId + "'");

		} else {
			selectSQL.append(" WHERE parentid = '" + kayaModelId + "' AND businessid = '" + KayaModelUtils
					.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId), paramater.getMapping().getKeys())
					+ "' AND businesssubid = '" + businessId + "'");
		}
		selectSQL.append(" AND parentid = '" + kayaModelId + "' ORDER BY orientationkey DESC;");
		paramater.setOrientationKeySet(new HashSet<String>());
		kayaEntityList = dBConnection.executeQuery(selectSQL.toString(), paramater.getOrientationKeySet());
		return kayaEntityList;
	}


	public List<Map<String, String>> getWorkFlowDetailedHistory(Paramater paramater) {
		StringBuilder workFlowHistorySQL = KayaModelUtils.getWorkFlowHistorySql(paramater.getId());
		workFlowHistorySQL.append("ORDER BY relid DESC;");
		paramater.setOrientationKeySet(new HashSet<String>());
		List<Map<String, String>> kayaEntityList = new ArrayList<Map<String, String>>();
		kayaEntityList = dBConnection.executeQuery(workFlowHistorySQL.toString(), paramater.getOrientationKeySet());

		return kayaEntityList;
	}

	/**
	 * ' 取得插入已经更新对象SQL文
	 * 
	 * @param maping
	 * @param kayaModelId
	 * @param actionId
	 * @param insertSQL
	 * @param updateSQL
	 * @param userInfo
	 */
	private void getInsertSqlString(Mapping maping, String kayaModelId, String actionId, StringBuilder insertSQL,
			StringBuilder updateSQL, HashMap<String, String> businessKeyMap, User usrinfo) {
		// 取得Role子元素信息
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaWorkFlowAction(actionId);
		// 取得Role信息
		String orderNo = UtilTools.getOrderNo();
		// ActionName
		String actionName = Constant.EMPTY;
		// 取得生成workflow数据需要的组织信息
		KayaMetaModel actionUsrtask = AccessKayaModel.getParentKayaModel(actionId);
		
		List<String> organizationList = new ArrayList<String>();
		organizationList = actionUsrtask.getOrganizationItems();

		// NextStape
		String nextWorkFlowId = "";
		nextWorkFlowId = AccessKayaModel.getWorkFlowConnectionDes(actionId);

		String businessid = businessKeyMap.get(Constant.BUSINESSID);
		String businesssubid = businessKeyMap.get("businesssubid");
		if (StringUtil.isEmpty(businesssubid)) {
			businesssubid = "";
		}
		String orientationkey = businessKeyMap.get(Constant.ORIENTATIONKEY);
		boolean flg = true;
		for (KayaMetaModel kayaModel : kayaModelList) {
			// gmeid
			if (flg) {
				insertSQL.append("(");
				flg = false;
			} else {
				insertSQL.append(",(");
			}

			// businessid
			// businesssubid
			insertSQL.append("'" + businessid + "',");
			insertSQL.append("'" + businesssubid + "',");
			// orientationkey
			insertSQL.append("'" + orientationkey + "',");

			// relid
			insertSQL.append("'" + orderNo + "',");

			// kind
			// name
			// kindValue
			// 参照的情况下，取参照元的KindKey，设置的时候利用参照本身的KindKey
			if (Constant.PROPERTYREF.equals(kayaModel.getMetaModelType())) {
				insertSQL.append("'" + kayaModel.get(Constant.REFERRED) + "',");
			} else {
				insertSQL.append("'" + kayaModel.getGmeId() + "',");
			}

			insertSQL.append("'" + kayaModel.get(Constant.KINDKEY) + "',");
			insertSQL.append("'" + kayaModel.getName() + "',");
			if (kayaModel.getMetaModelType().contains(Constant.ACTION)) {
				actionName = AccessKayaModel.getKayaModelId(actionId).getName();
				insertSQL.append("'" + AccessKayaModel.getParentKayaModel(actionId).getName() + "',");
			} else {
				if (maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) != null) {
					insertSQL.append("'" + maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "',");
				} else {
					insertSQL.append("'',");
				}
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
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			insertSQL.append("'" + usrinfo.getUserId() + "',");
			// updatedate
			// updateuser
			// updatemachine
			insertSQL.append("'" + "')");
		}
		// 是否保留Action
		if (AccessKayaModel.getParentId(actionId).equals(nextWorkFlowId)) {
			insertSQL.append(",(");
			// businessid
			// businesssubid
			insertSQL.append("'" + businessid + "',");
			insertSQL.append("'" + businesssubid + "',");
			// orientationkey
			insertSQL.append("'" + orientationkey + "',");

			// relid
			insertSQL.append("'" + orderNo + "',");
			// gmeid
			// kind
			// name
			// kindValue
			insertSQL.append("'backtouser',");
			insertSQL.append("'backtouser',");
			insertSQL.append("'',");
			insertSQL.append("'" + usrinfo.getUserId() + "',");

			// kindtype
			insertSQL.append("'Organization',");
			// securitycode
			// flowcode
			// flowsubcode
			insertSQL.append("'',");
			insertSQL.append("'" + actionUsrtask.getGmeId() + "',");
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
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			insertSQL.append("'" + usrinfo.getUserId() + "',");
			// updatedate
			// updateuser
			// updatemachine
			insertSQL.append("'" + "')");
		} else {
			// 是否回退Action
			String actionType = AccessKayaModel.getKayaModelId(actionId).getMetaModelType();
			if (Constant.BACKACTION.equals(actionType) || Constant.CANCELACTION.equals(actionType)) {
				// 取得回退对象数据的updateuser信息
				String tableName = AccessKayaModel.getKayaModelId(actionId).getTableId();
				List<KayaEntity> backModelInfoList = getBackToModelInfo(nextWorkFlowId, orientationkey, tableName);
				if (backModelInfoList.size() > 0) {
					insertSQL.append(",(");
					// businessid
					// businesssubid
					insertSQL.append("'" + businessid + "',");
					insertSQL.append("'" + businesssubid + "',");
					// orientationkey
					insertSQL.append("'" + orientationkey + "',");

					// relid
					insertSQL.append("'" + orderNo + "',");
					// gmeid
					// kind
					// name
					// kindValue
					String backtouser = backModelInfoList.get(0).getCreateuser();
					insertSQL.append("'backtouser',");
					insertSQL.append("'backtouser',");
					insertSQL.append("'',");
					insertSQL.append("'" + backtouser + "',");

					// kindtype
					insertSQL.append("'Organization',");
					// securitycode
					// flowcode
					// flowsubcode
					insertSQL.append("'',");
					insertSQL.append("'" + actionUsrtask.getGmeId() + "',");
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
					insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
					insertSQL.append("'" + usrinfo.getUserId() + "',");
					// updatedate
					// updateuser
					// updatemachine
					insertSQL.append("'" + "')");
				}

			} else {
				// 组织信息插入
				for (int i = 0; i < organizationList.size(); i++) {

					String orgInfo = organizationList.get(i);
					// gmeid
					insertSQL.append(",(");

					// businessid
					// businesssubid
					insertSQL.append("'" + businessid + "',");
					insertSQL.append("'" + businesssubid + "',");
					// orientationkey
					insertSQL.append("'" + orientationkey + "',");

					// relid
					insertSQL.append("'" + orderNo + "',");
					// kind
					// name
					// kindValue

					String orgKindkey = "";
					insertSQL.append("'" + orgKindkey + "',");

					insertSQL.append("'" + orgKindkey + "',");
					insertSQL.append("'',");

					if (usrinfo.getUserMap().get(orgKindkey) != null) {
						insertSQL.append("'" + usrinfo.getUserMap().get(orgInfo) + "',");
					} else {
						insertSQL.append("'',");
					}

					// kindtype
					insertSQL.append("'Organization',");
					// securitycode
					// flowcode
					// flowsubcode
					insertSQL.append("'',");
					insertSQL.append("'" + actionUsrtask.getGmeId() + "',");
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
					insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
					insertSQL.append("'" + usrinfo.getUserId() + "',");
					// updatedate
					// insertSQL.append("'',");
					// updateuser
					// updatemachine
					insertSQL.append("'" + "')");

				}
			}
		}

		// ExclusiveGateway（分歧条件）
		if (Constant.E_GateWay.equals(AccessKayaModel.getKayaModelId(nextWorkFlowId).getMetaModelType())) {
			// ScriptEXE scriptExE = new ScriptEXE();
			// 如果返回GetWay本身ID,则申请条件异常（设置的分歧条件没有覆盖所有场合）
			if (nextWorkFlowId.equals(ScriptEXE.Exe(nextWorkFlowId, maping.getPropertys()))) {
				// TODO： 通知前台系统管理员修改分歧条件，覆盖所有业务场景
			} else {
				nextWorkFlowId = AccessKayaModel
						.getWorkFlowConnectionDes(ScriptEXE.Exe(nextWorkFlowId, maping.getPropertys()));
			}
			// ParallelGateway （同时满足多条件）
		} else if (Constant.P_GateWay.equals(AccessKayaModel.getKayaModelId(nextWorkFlowId).getMetaModelType())) {
			// 需要同时查询其他分支条件的流程状态。

			// 如果其他条件都满足了，那么更新本次流程状态之后，自动走向下一个流程

			// 如果其他条件没满足，那么仅更新本次流程状态。

		}

		// 主状态更新
		updateSQL.append("relid = '" + orderNo + "',");
		updateSQL.append("flowcode = '" + nextWorkFlowId + "',");
		updateSQL.append("flowsubcode = '" + actionId + "',");
		updateSQL.append("kindvalue = '" + actionName + "',");
		updateSQL.append("updatedate = " + "{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
		updateSQL.append("updateuser = '" + usrinfo.getUserId() + "' ");
		updateSQL.append("WHERE gmeid = '" + kayaModelId + "' ");
		updateSQL.append(" and businessid = '" + businessid + "'");
		updateSQL.append("  and businesssubid = '" + businesssubid + "'");
		// 必须符合流程的前后约束
		updateSQL.append("  and flowcode = '" + AccessKayaModel.getWorkFlowConnectionSrc(actionId) + "'");
		// 只更新主记录
		updateSQL.append("  and kindtype = '" + Constant.G_ROLE + "';");

	}

	/**
	 * WorkFlow一览检索
	 * 
	 * @param paramater
	 * @return
	 */
	public List<Map<String, String>> selectForWorkflow(Paramater paramater, boolean mgrFlg, User userInfo) {
		List<Map<String, String>> kayaEntityList = new ArrayList<Map<String, String>>();
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)) {
			return kayaEntityList;
		}
		String kayaModelId = paramater.getId();
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
		// 更新全对象取得（包含子）
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNoAction(kayaModelId);

		Map<String, Object> usrMap = userInfo.getUserMap();

		List<String> flowcodeList = new ArrayList<String>();
		if (mgrFlg) {		
			for (KayaMetaModel kayamodel : kayaModelList) {
				if (kayamodel.getPermissionsItems() != null && kayamodel.getPermissionsItems().size() > 0) {
					List<KayaModelPermissionsItem> permissions = kayamodel.getPermissionsItems();
					boolean perFlag = false;
					for (KayaModelPermissionsItem permissionsItem : permissions) {
						String perId = permissionsItem.getId();
						List<String> perText = permissionsItem.getTextList();

						@SuppressWarnings("unchecked")
						List<Map<String, Object>> roleDbList = (List<Map<String, Object>>) usrMap.get(roleInfoKey);
						for (Map<String, Object> tempRole : roleDbList) {
							if (perText.contains(tempRole.get(perId))) {
								perFlag = true;
								// flowcodeList.add(kayamodel.getGmeId());
								break;
							}
						}
					}
					if (perFlag) {
						flowcodeList.add(kayamodel.getGmeId());
					}
				}
			}
		}

		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
		selectSQL.append(" WHERE ");
		StringBuilder selectEmptSQL = new StringBuilder("");
		if (mgrFlg) {
			selectSQL.append(commonRelidInSQL(flowcodeList, userInfo, tableName));
			if (paramater.getMapping().getPropertys().get("kindvalue") != null) {
				if ("createdate".equals(paramater.getMapping().getPropertys().get("kind"))) {
					selectEmptSQL.append(
							" AND createdate LIKE '" + paramater.getMapping().getPropertys().get("kindvalue") + "%'");
				} else if ("createuser".equals(paramater.getMapping().getPropertys().get("kind"))) {
					selectEmptSQL.append(
							" AND createuser LIKE '" + paramater.getMapping().getPropertys().get("kindvalue") + "%'");
				} else if ("orientationkey".equals(paramater.getMapping().getPropertys().get("kind"))) {
					selectEmptSQL.append(" AND orientationkey LIKE '"
							+ paramater.getMapping().getPropertys().get("kindvalue") + "%'");
				} else {

				}

			}

		} else {
			selectEmptSQL.append(" relid IN (SELECT relid FROM " + tableName + " WHERE ");
			selectEmptSQL.append(" kindtype = 'Role' AND createuser = ");
			KayaSQLExecute kexe = new KayaSQLExecute();
			selectEmptSQL.append(kexe.getLoginUserIdForSql());
			selectEmptSQL.append(") ");
		}

		selectSQL.append(selectEmptSQL.toString() + " ORDER BY orientationkey;");
		System.out.println(selectSQL.toString());
		paramater.setOrientationKeySet(new HashSet<String>());
		kayaEntityList = dBConnection.executeQueryForWorkflow(selectSQL.toString(), paramater.getOrientationKeySet());
		return kayaEntityList;

	}

	/**
	 * WorkFlowDetail检索
	 * 
	 * @param paramater
	 * @return
	 */
	public HashMap<String, Object> selectForWorkflowDetail(Paramater paramater) {
		List<Map<String, String>> kayaEntityList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> kayaEntityListWF = new ArrayList<Map<String, String>>();
		String kayaModelId = paramater.getId();
		String orientationKey = paramater.getOrientationKey();
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
		// 更新全对象取得（包含子）
		paramater.setOrientationKeySet(new HashSet<String>());
		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
		selectSQL.append(" WHERE orientationkey = '" + orientationKey + "'");

		System.out.println(selectSQL.toString());
		kayaEntityList = dBConnection.executeQuery(selectSQL.toString(), paramater.getOrientationKeySet());

		if (paramater.getTargetTableList().size() > 0) {
			String workflowTableId = paramater.getTargetTableList().get(0);
			if ("mg".equals(workflowTableId.substring(0, 2))) {
				workflowTableId = workflowTableId.substring(3);
			}
			String tableNameWF = AccessKayaModel.getKayaModelId(workflowTableId).getTableId();
			StringBuilder selectSQLWF = new StringBuilder(KayaModelUtils.selectString + tableNameWF);
			selectSQLWF.append(" WHERE orientationkey = '" + orientationKey + "'");
			selectSQLWF.append(" ORDER BY relid DESC;");
			System.out.println(selectSQLWF.toString());
			kayaEntityListWF = dBConnection.executeQueryWorkflowDetail(selectSQLWF.toString(),
					paramater.getOrientationKeySet());

		}

		HashMap<String, Object> resultSql = new HashMap<String, Object>();
		resultSql.put("kayaEntityList", kayaEntityList);
		resultSql.put("workflowList", kayaEntityListWF);
		return resultSql;

	}

	/**
	 * 检索的共通SQl文做成
	 * 
	 * @param flowcodeList
	 * @param userInfo
	 * @return
	 */
	private StringBuilder commonRelidInSQL(List<String> flowcodeList, User userInfo, String tableName) {
		StringBuilder selectSQL = new StringBuilder("");
		selectSQL.append("relid IN (");
		String unionString = "";
		for (String flowcode : flowcodeList) {
			List<KayaMetaModel> kayamodelIdSrc = AccessKayaModel.getSrcUserTask(flowcode);
			for (KayaMetaModel kayamodelSrc : kayamodelIdSrc) {
				selectSQL.append(unionString + "SELECT relid FROM ").append(tableName);
				// selectSQL.append(" WHERE ");
				// 检索条件个数
				// int selectCount = 0;
				int selectCount = 1;
				// 复数检索条件（OR）

				StringBuilder selectEmptSQL = new StringBuilder("");
				selectEmptSQL.append("(kindtype = 'Role' AND flowcode = '" + flowcode + "') ");
				if (kayamodelSrc.getOrganizationItems() != null) {
					// 取得生成workflow数据需要的组织信息
					List<String> organizationList = new ArrayList<String>();
					organizationList = kayamodelSrc.getOrganizationItems();
					for (int i = 0; i < organizationList.size(); i++) {
					//	String orgInfo = organizationList.get(i);
						String orgKindkey = "";
						// 检索条件个数
						selectCount = selectCount + 1;
						selectEmptSQL.append(" OR ");
						selectEmptSQL.append("(kind = '" + orgKindkey)
								.append("' AND kindvalue = '" + userInfo.getUserMap().get(orgKindkey) + "')");
					}
				}
				selectEmptSQL.append(" OR ");
				selectEmptSQL.append("(kindtype = 'Action' AND flowcode = '" + kayamodelSrc.getGmeId() + "')");
				selectCount = selectCount + 1;
				if (selectCount > 0) {
					selectSQL.append(" WHERE ")
							.append(selectEmptSQL.toString() + " group by relid having count(1)=" + selectCount);
				} else {
					selectSQL.append(selectEmptSQL.toString() + " group by relid");
				}
				unionString = " UNION ";
				// 回退action的数据
				selectSQL.append(unionString + "SELECT relid FROM ").append(tableName);
				StringBuilder selectBackSQL = new StringBuilder("");
				selectBackSQL.append("(kindtype = 'Role' AND flowcode = '" + flowcode + "') ");
				selectBackSQL.append(" OR ");
				selectBackSQL.append("(kind = 'backtouser' AND kindvalue = '" + userInfo.getUserId() + "')");
				selectSQL.append(" WHERE ").append(selectBackSQL.toString() + " group by relid having count(1)= 2");
			}
		}
		selectSQL.append(") ");
		return selectSQL;
	}

	/**
	 * 回退Action必要信息取得
	 * 
	 * @param flowcode
	 * @param orientationkey
	 * @param tableName
	 * @return List<KayaEntity>
	 */
	public List<KayaEntity> getBackToModelInfo(String flowcode, String orientationkey, String tableName) {
		List<KayaEntity> kayaEntityList = new ArrayList<KayaEntity>();

		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
		selectSQL.append(" WHERE ");
		selectSQL.append("(kindtype = 'Action' AND flowcode = '" + flowcode + "' AND orientationkey = '"
				+ orientationkey + "') ");
		selectSQL.append(" ORDER BY relid DESC;");
		System.out.println(selectSQL.toString());
		kayaEntityList = dBConnection.executeQueryForWorkflowRows(selectSQL.toString());
		return kayaEntityList;
	}

}
