package com.smartkaya.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.smartkaya.api.utils.StringUtil;
import com.smartkaya.bean.Message;
import com.smartkaya.bean.Message.Lever;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.constant.ConditionEnum;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.core.DbConnection;
import com.smartkaya.log.KayaLogManager;
import com.smartkaya.model.KayaMetaModel;
import com.smartkaya.model.KayaModelOrganizationItem;
import com.smartkaya.script.ScriptEXE;
import com.smartkaya.user.User;
import com.smartkaya.utils.UtilTools;

/**
 * KaYaMode 通用表CRUD操作类
 * 
 * @author LiangChen 2018/4/30
 * @version 1.0.0
 */
public final class KayaSQLExecute {
	private DbConnection dBConnection = AccessKayaModel.getDbConnection();
	private KayaLogManager kayaLoger = KayaLogManager.getInstance();

	public KayaSQLExecute() {
	}

	/**
	 * 插入更新删除检索处理（单表单行）
	 * @param paramater
	 * @return List
	 */
	public void execute(Paramater paramater) {
		switch (paramater.getCrud()) {
		case Constant.INSERT:
			insert(paramater);
			break;
		case Constant.UPDATE:
			update(paramater);
			break;
		case Constant.DELETE:
			delete(paramater);
			break;
		default :
			kayaLoger.error("This type of crud is not supported!");
			paramater.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("sql_10001");
			message.setMesage("请确认CRUD参数是否正确！");
			paramater.setMessages(message);
			kayaLoger.warn(message);
			break;
		}
	}

	/**
	 * 插入更新删除处理（单表多行）
	 * @param paramaters
	 * @return
	 */
	public void execute(Paramaters paramaters) {
		switch (paramaters.getCrud()) {
		case Constant.INSERT:
			insert(paramaters);
			break;
		case Constant.UPDATE:
			update(paramaters);
			break;
		case Constant.DELETE:
			delete(paramaters);
			break;
		default :
			kayaLoger.error("This type of crud is not supported!");
			paramaters.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("sql_10001");
			message.setMesage("请确认CRUD参数是否正确！");
			paramaters.setMessages(message);
			kayaLoger.warn(message);
			break;
		}
	}

	/**
	 * 插入更新删除处理（多表多行）
	 * @param paramatersList
	 * @return
	 */
	public void execute(List<Paramaters> paramatersList) {
		List<String> sqlStringList = new ArrayList<String>();
		for (Paramaters paramaters : paramatersList) {
			String kayaModelId = paramaters.getId();

			String crud = paramaters.getCrud();
			User usrinfo = paramaters.getUsrinfo();
			switch (crud) {
			case Constant.INSERT:
				KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
				String tableName = kayaMetaModel.getTableId();
				StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);

				boolean flg = true;
				for (HashMap<String,Object> subEntity : paramaters.getListPropertys()) {
					getInsertSqlString(subEntity,paramaters.getOrientationKey(), kayaModelId, insertSQL, usrinfo, flg);

					// 多条编辑","处理
					flg = false;
					String workFlowId = AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId();
					if (!Constant.EMPTY.equals(workFlowId) && workFlowId != null) {
						sqlStringList.add(getWorkflowRoleSqlString(paramaters.getId(), workFlowId,
								paramaters.getActionid(), paramaters.getOrientationKey(),subEntity,paramaters.getBusinessKeyMap(),usrinfo));

					}
				}
				insertSQL.append(";");
				kayaLoger.info(insertSQL);
				sqlStringList.add(insertSQL.toString());
				break;
			case Constant.UPDATE:
				for (HashMap<String,Object> subEntity : paramaters.getListPropertys()) {
					getUpdateSqlString(subEntity, kayaModelId, sqlStringList, paramaters.getOrientationKey(),paramaters.getUsrinfo());
				}
				break;
			case Constant.DELETE:
				for (HashMap<String,Object> propertys: paramaters.getListPropertys()) {
					String orientationKey = "";
					StringBuilder deleteSQL = new StringBuilder("DELETE FROM ").append(AccessKayaModel.getKayaModelId(kayaModelId).getTableId()).append(" WHERE ");
					if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
						// orientationkey
						orientationKey = KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),
								propertys);
						// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
					} else {
						// orientationkey
						orientationKey = KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),
								paramaters.getOrientationKey(), propertys);
						if (paramaters.getTargetTableList().size() > 0) {
							deleteSQL.append(" parentid in (");
							String empSQL = "";
							for (String tableId : paramaters.getTargetTableList()) {
								empSQL = empSQL.concat(",");
								empSQL = empSQL + "'" + tableId + "'";
							}
							deleteSQL.append(empSQL.substring(1)).append(") AND");
						}
					}

					deleteSQL.append("  orientationkey like '" + orientationKey + "%'");

					deleteSQL.append(";");
					// System.out.println(deleteSQL);
					kayaLoger.info(deleteSQL);
					sqlStringList.add(deleteSQL.toString());
				}
				break;
			default:// 参数错误处理
				kayaLoger.error("This type of crud is not supported!");
				paramaters.setError(true);
				Message message = new Message();
				message.setLever(Lever.ERROR);
				message.setCode("sql_10001");
				message.setMesage("请确认CRUD参数是否正确！");
				paramaters.setMessages(message);
				kayaLoger.warn(message);
				break;
			}
		}

		dBConnection.executeBatch(sqlStringList);
	}

	/**
	 * 单表单条插入处理 Single-table single-row insert processing
	 * 
	 * @param paramater
	 * @return
	 */
	private int insert(Paramater paramater) {
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)) {
			return 0;
		}
		String kayaModelId = paramater.getId();
		User usrinfo = paramater.getUsrinfo();
		// 取得Role信息
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();
		StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);
		boolean flg = true;
		getInsertSqlString(paramater.getPropertys(),paramater.getOrientationKey(), kayaModelId, insertSQL,usrinfo, flg);
		insertSQL.append(";");
		sqlStringList.add(insertSQL.toString());

		// 监听业务流程处理
		if (!Constant.EMPTY.equals(AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId())) {
			// TODO:验证流程ID
			sqlStringList.add(getWorkflowRoleSqlString(paramater.getId(),
					AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId(), paramater.getActionid(),
					paramater.getOrientationKey(),paramater.getPropertys(),paramater.getBusinessKeyMap(), usrinfo).toString());
		}
		dBConnection.executeBatch(sqlStringList);
		// System.out.println(insertSQL.toString());
		kayaLoger.info(insertSQL);
		return 0;

	}

	/**
	 * 单表多条插入处理 Single-table multi-row insert processing
	 * 
	 * @param paramaters
	 *            统一参数
	 * @return
	 */
	private int insert(Paramaters paramaters) {
		List<String> sqlStringList = new ArrayList<String>();

		String kayaModelId = paramaters.getId();

		// 取得Role信息
		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
		String tableName = kayaMetaModel.getTableId();
		User userInfo = paramaters.getUsrinfo();
		// WF处理（更新WF状态）
		if (Constant.UPDATE.equals(paramaters.getCrud())) {
			KayaWorkFlow kayaWorkFlow = new KayaWorkFlow();
			kayaWorkFlow.excuteKayaWorkFlow(paramaters);


			// 新输入数据的场合（一般数据插入处理和WF Start处理）
		} else if (Constant.INSERT.equals(paramaters.getCrud())){
			StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);
			boolean flg = true;
			for (HashMap<String,Object> subEntity : paramaters.getListPropertys()) {
				getInsertSqlString(subEntity,paramaters.getOrientationKey(), kayaModelId, insertSQL,userInfo, flg);

				// 多条编辑","处理
				flg = false;
				String workFlowId = AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId();
				if (!Constant.EMPTY.equals(workFlowId) && workFlowId != null) {
					sqlStringList.add(getWorkflowRoleSqlString(paramaters.getId(), workFlowId, paramaters.getActionid(),
							paramaters.getOrientationKey(),subEntity,paramaters.getBusinessKeyMap(),userInfo));
				}
			}
			insertSQL.append(";");
			sqlStringList.add(insertSQL.toString());
			kayaLoger.info(insertSQL);
		}

		dBConnection.executeBatch(sqlStringList);

		return 0;

	}

	/**
	 * 多表多条插入处理
	 * 
	 * @param paramaters
	 *            统一参数
	 * @return
	 */
	public int insert(List<Paramaters> paramatersList) {
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();

		for (Paramaters paramaters : paramatersList) {
			// Table存在确认
			//			if (!KayaModelUtils.checkTableId(paramaters)) {
			//				return 0;
			//			}
			User userInfo = paramaters.getUsrinfo();
			String kayaModelId = paramaters.getId();
			// 取得Role信息
			KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
			String tableName = kayaMetaModel.getTableId();
			StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);
			// 只执行一次处理Flg
			boolean flg = true;
			if (!Constant.EMPTY.equals(AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId())) {
				// TODO:验证流程ID
				System.out.println(AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId());
				kayaLoger.info("WorkFlowId:" + insertSQL);
			}
			for (HashMap<String,Object> subEntity : paramaters.getListPropertys()) {

				getInsertSqlString(subEntity, paramaters.getOrientationKey(),kayaModelId, insertSQL,userInfo, flg);
				flg = false;
			}

			insertSQL.append(";");
			// System.out.println(insertSQL.toString());
			kayaLoger.info(insertSQL);
			sqlStringList.add(insertSQL.toString());
		}

		dBConnection.executeBatch(sqlStringList);
		return 0;

	}

	/**
	 * 单表单行更新处理 Single-table single-row update processing
	 * 
	 * @param paramater
	 * @return
	 */
	public int update(Paramater paramater) {
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)) {
			return 0;
		}
		String kayaModelId = paramater.getId();

		List<String> sqlStringList = new ArrayList<String>();
		getUpdateSqlString(paramater.getPropertys(), kayaModelId, sqlStringList, paramater.getOrientationKey(),paramater.getUsrinfo());

		dBConnection.executeBatch(sqlStringList);

		return 0;
	}

	/**
	 * 单表多行更新处理 Single-table multi-row update processing
	 * 
	 * @param paramaters
	 * @return
	 */
	public int update(Paramaters paramaters) {
		// Table存在确认
		// if (!KayaModelUtils.checkTableId(paramaters)){
		// return 0;
		// }
		String kayaModelId = paramaters.getId();
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();
		for (HashMap<String,Object> subEntity : paramaters.getListPropertys()) {
			getUpdateSqlString(subEntity, kayaModelId, sqlStringList, paramaters.getOrientationKey(),paramaters.getUsrinfo());
		}
		dBConnection.executeBatch(sqlStringList);
		return 0;
	}

	/**
	 * 追加字段更新处理 Single-table multi-row update processing
	 * 
	 * @param paramaters
	 * @return
	 */
	public int update(Paramaters paramaters, List<String> insertFieldList) {
		// Table存在确认
		// if (!KayaModelUtils.checkTableId(paramaters)){
		// return 0;
		// }
		User userInfo = paramaters.getUsrinfo();
		String kayaModelId = paramaters.getId();
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();
		for (HashMap<String,Object> subEntity : paramaters.getListPropertys()) {
			getUpdateSqlString(subEntity, kayaModelId, sqlStringList, insertFieldList, paramaters.getOrientationKey(),paramaters.getUsrinfo());
		}

		// insert处理
		// 取得Role信息
		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
		String tableName = kayaMetaModel.getTableId();
		StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);
		// 只执行一次处理Flg
		boolean flg = true;

		for (HashMap<String,Object> subEntity : paramaters.getListPropertys()) {

			getInsertSqlString(subEntity, kayaModelId, insertSQL, paramaters.getOrientationKey(), insertFieldList,userInfo, flg);
			flg = false;
		}

		insertSQL.append(";");
		// System.out.println(insertSQL.toString());
		kayaLoger.info(insertSQL);
		sqlStringList.add(insertSQL.toString());

		dBConnection.executeBatch(sqlStringList);
		return 0;
	}

	/**
	 * 多表多行更新处理 Single-table multi-row update processing
	 * 
	 * @param paramaters
	 * @return
	 */
	public int update(List<Paramaters> paramatersList) {
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();
		for (Paramaters paramaters : paramatersList) {
			String kayaModelId = paramaters.getId();
			for (HashMap<String,Object> subEntity : paramaters.getListPropertys()) {
				getUpdateSqlString(subEntity, kayaModelId, sqlStringList, paramaters.getOrientationKey(),paramaters.getUsrinfo());
			}
		}
		dBConnection.executeBatch(sqlStringList);
		return 0;
	}

	/**
	 * 任意键检索
	 * 
	 * @param paramater
	 * @return
	 */
	public List<Map<String, String>> selectByFreeKind(Paramater paramater) {
		List<Map<String, String>> kayaEntityList = new ArrayList<Map<String, String>>();
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)) {
			return kayaEntityList;
		}
		String kayaModelId = paramater.getId();
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
		// 更新全对象取得（包含子）
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);

		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
		selectSQL.append(" WHERE orientationkey IN (");
		boolean flg = true;
		StringBuilder selectEmptSQL = new StringBuilder("");

		// 主次表处理
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			for (KayaMetaModel kayaModel : kayaModelList) {
				if (paramater.getPropertys()
						.containsKey(kayaModel.get(Constant.KINDKEY))) {
					selectEmptSQL.append("SELECT orientationkey FROM " + tableName + " WHERE (kind = '"
							+ kayaModel.get(Constant.KINDKEY) + "' AND kindvalue LIKE '" + paramater
							.getPropertys().get(kayaModel.get(Constant.KINDKEY))
							+ "%')");
				}
			}
			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
		} else {
			// orientationkey
			// String businessId =
			// KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),paramater.getBusinessKeyMap());
			for (KayaMetaModel kayaModel : kayaModelList) {
				if (paramater.getPropertys()
						.containsKey(kayaModel.get(Constant.KINDKEY))) {
					if (flg) {

						selectEmptSQL.append("SELECT orientationkey FROM " + tableName + " WHERE (kind = '"
								+ kayaModel.get(Constant.KINDKEY) + "' AND kindvalue LIKE '"
								+ paramater.getPropertys()
								.get(kayaModel.get(Constant.KINDKEY))
								+ "%' AND businessid = '" + paramater.getOrientationKey() + "')");

						flg = false;
					} else {
						selectEmptSQL.insert(0,
								" SELECT orientationkey FROM " + tableName + " WHERE (orientationkey IN (");
						selectEmptSQL.append(")) AND (kind='" + kayaModel.get(Constant.KINDKEY)
						+ "' AND kindvalue LIKE '"
						+ paramater.getPropertys()
						.get(kayaModel.get(Constant.KINDKEY))
						+ "%' AND businessid = '" + paramater.getOrientationKey() + "')");
					}
				}
			}
		}

		selectSQL.append(selectEmptSQL.toString() + ") ORDER BY orientationkey DESC;");

		// System.out.println(selectSQL.toString());
		kayaLoger.info(selectSQL);
		paramater.setOrientationKeySet(new HashSet<String>());
		kayaEntityList = dBConnection.executeQuery(selectSQL.toString(), paramater.getOrientationKeySet());
		return kayaEntityList;
	}

	/**
	 * 任意多键检索
	 * 
	 * @param paramater
	 * @return
	 */
	public List<Map<String, String>> selectMuiltKindByOrientationkey(Paramater paramater) {
		List<Map<String, String>> kayaEntityList = new ArrayList<Map<String, String>>();
		// Table存在确认
		// if (!KayaModelUtils.checkTableId(paramater)){
		// return kayaEntityList;
		// }
		// TODO commonSQL要应用
		StringBuilder selectSQL = new StringBuilder();
		String workFlowId = AccessKayaModel.getKayaModelId(paramater.getId()).getWorkFlowId();
		if (!Constant.EMPTY.equals(workFlowId) && workFlowId != null) {
			selectSQL = commonSelectSQL(paramater, "orientationkey",true);
			selectSQL.append(WFCommonSelectSQL(paramater));
		} else {
			selectSQL = commonSelectSQL(paramater, "orientationkey",false);
		}

		kayaLoger.info(selectSQL);
		paramater.setOrientationKeySet(new HashSet<String>());
		kayaEntityList = dBConnection.executeQuery(selectSQL.toString(), paramater.getOrientationKeySet());
		return kayaEntityList;
	}

	/**
	 * 任意多键检索（与businessid关联的所有情报都检索出来）
	 * 
	 * @param paramater
	 * @return
	 */
	public List<Map<String, String>> selectMuiltKindByBusiness(Paramater paramater) {

		List<Map<String, String>> kayaEntityList = new ArrayList<Map<String, String>>();

		StringBuilder selectSQL = commonSelectSQL(paramater, "businessid",false);
		kayaEntityList = dBConnection.executeQuery(selectSQL.toString(), paramater.getOrientationKeySet());
		return kayaEntityList;
	}

	/**
	 * 任意多键检索并包含其子表（与businessid关联的所有情报都检索出来）
	 * 
	 * @param paramater
	 * @return
	 */
	public Map<String, Object> selectMuiltKindAllInfo(Paramater paramater) {

		Map<String, Object> kayaEntity = new HashMap<String, Object>();

		StringBuilder selectSQL = commonSQLIncludeSubinfo(paramater, "orientationkey");
		kayaEntity = dBConnection.executeQueryIncludeSubinfo(selectSQL.toString(), paramater.getOrientationKeySet());
		return kayaEntity;
	}

	/**
	 * 检索的共通SQl文做成
	 * 
	 * @param paramater
	 * @param selectSQL
	 * @return
	 */
	private StringBuilder commonSelectSQL(Paramater paramater, String key,boolean wfflag) {
		String kayaModelId = paramater.getId();

		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();

		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
		selectSQL.append(" WHERE ");

		// 对象表指定
		String selectEmpSQL = "";
		if (paramater.getTargetTableList().size() > 0) {
			selectSQL.append(" parentid in (");
			for (String tableId : paramater.getTargetTableList()) {
				selectEmpSQL = selectEmpSQL.concat(",");
				selectEmpSQL = selectEmpSQL + "'" + tableId + "'";
			}
			selectSQL.append(selectEmpSQL.substring(1)).append(") and ");
		}

		//
		if (wfflag) {
			selectSQL.append(" kindtype<>'Role' AND " + key).append(" IN (SELECT ").append(key).append(" FROM ").append(tableName);
		} else {
			selectSQL.append(key).append(" IN (SELECT ").append(key).append(" FROM ").append(tableName);
		}


		// 更新全对象取得（包含子）
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
		StringBuilder selectEmptSQL = new StringBuilder("");

		// 检索条件个数
		int selectCount = 0;
		// 复数检索条件（OR）
		String orString = "";

		// 主次表处理
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			for (KayaMetaModel kayaModel : kayaModelList) {
				if (paramater.getPropertys()
						.containsKey(kayaModel.get(Constant.KINDKEY))) {
					// 检索条件个数
					selectCount = selectCount + 1;
					selectEmptSQL.append(orString);
					String values = paramater.getPropertys()
							.get(kayaModel.get(Constant.KINDKEY)).toString();
					switch (ConditionEnum.toEnum(StringUtil.getCondition(values))){
					case  IN:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue in (" + values + "))");
						break;
					case  DATE:
						String[] value = values.split("～");
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue >= '" + StringUtil.trim(value[0]) + "'")
						.append(" AND kindvalue <= '" + StringUtil.trim(value[1]) + "')");
						break;
					default:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue LIKE '" + values + "%')");
						break;
					}
					orString = " OR ";
				}
			}
			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
		} else {
			// orientationkey
			// String businessId =
			// KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),paramater.getBusinessKeyMap());
			for (KayaMetaModel kayaModel : kayaModelList) {
				if (paramater.getPropertys()
						.containsKey(kayaModel.get(Constant.KINDKEY))) {
					// 检索条件个数
					selectCount = selectCount + 1;
					selectEmptSQL.append(orString);
					String values = paramater.getPropertys()
							.get(kayaModel.get(Constant.KINDKEY)).toString();

					switch (ConditionEnum.toEnum(StringUtil.getCondition(values))) {
					case IN:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue in (" + values + ")");
						if (StringUtil.isNotEmpty(paramater.getOrientationKey())) {
							selectEmptSQL.append(" AND businessid = '").append(paramater.getOrientationKey())
							.append("'");
						}
						selectEmptSQL.append(")");
						break;
					case DATE:
						String[] value = values.split("～");
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue >= '" + StringUtil.trim(value[0]) + "'")
						.append(" AND kindvalue <= '" + StringUtil.trim(value[1]) + "'");
						if (StringUtil.isNotEmpty(paramater.getOrientationKey())) {
							selectEmptSQL.append(" AND businessid = '").append(paramater.getOrientationKey())
							.append("'");
						}
						selectEmptSQL.append(")");
						break;
					default:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue LIKE '").append(values)
						// + "%' AND businessid = '" + businessId +
						// "')");
						.append("%'");
						if (StringUtil.isNotEmpty(paramater.getOrientationKey())) {
							selectEmptSQL.append(" AND businessid = '").append(paramater.getOrientationKey())
							.append("'");
						}
						selectEmptSQL.append(")");
						break;
					}
					orString = " OR ";
				}
			}
		}


		if (wfflag) {
			if (selectCount > 0) {
				selectSQL.append(" WHERE ").append(selectEmptSQL.toString() + " group by " + key + " having count(1)="
						+ selectCount + ") ");
			} else {
				selectSQL.append(selectEmptSQL.toString() + " group by " + key + " ) ");
			}
		} else {
			if (selectCount > 0) {
				selectSQL.append(" WHERE ").append(selectEmptSQL.toString() + " group by " + key + " having count(1)="
						+ selectCount + ") ORDER BY orientationkey;");
			} else {
				selectSQL.append(selectEmptSQL.toString() + " group by " + key + " ) ORDER BY orientationkey;");
			}
		}

		//kayaLoger.info(selectSQL);
		paramater.setOrientationKeySet(new HashSet<String>());
		return selectSQL;
	}

	/**
	 * 检索的共通WFSQl文做成
	 * 
	 * @param paramater
	 * @param selectSQL
	 * @return
	 */
	private StringBuilder WFCommonSelectSQL(Paramater paramater) {
		String kayaModelId = paramater.getId();

		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
		String workFlowId = AccessKayaModel.getKayaModelId(AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId()).getTableId();
		StringBuilder selectSQL = new StringBuilder("union " + KayaModelUtils.selectWorkFlowString + workFlowId);
		selectSQL.append(" WHERE ");

		// 对象表指定
		String selectEmpSQL = "";
		if (paramater.getTargetTableList().size() > 0) {
			selectSQL.append(" parentid in (");
			for (String tableId : paramater.getTargetTableList()) {
				selectEmpSQL = selectEmpSQL.concat(",");
				selectEmpSQL = selectEmpSQL + "'" + tableId + "'";
			}
			selectSQL.append(selectEmpSQL.substring(1)).append(") and ");
		}


		selectSQL.append(" kindtype='Role' AND orientationkey IN (SELECT orientationkey FROM ").append(tableName);



		// 更新全对象取得（包含子）
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
		StringBuilder selectEmptSQL = new StringBuilder("");

		// 检索条件个数
		int selectCount = 0;
		// 复数检索条件（OR）
		String orString = "";

		// 主次表处理
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			for (KayaMetaModel kayaModel : kayaModelList) {
				if (paramater.getPropertys()
						.containsKey(kayaModel.get(Constant.KINDKEY))) {
					// 检索条件个数
					selectCount = selectCount + 1;
					selectEmptSQL.append(orString);
					String values = paramater.getPropertys()
							.get(kayaModel.get(Constant.KINDKEY)).toString();
					switch (ConditionEnum.toEnum(StringUtil.getCondition(values))){
					case  IN:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue in (" + values + "))");
						break;
					case  DATE:
						String[] value = values.split("～");
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue >= '" + StringUtil.trim(value[0]) + "'")
						.append(" AND kindvalue <= '" + StringUtil.trim(value[1]) + "')");
						break;
					default:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue LIKE '" + values + "%')");
						break;
					}
					orString = " OR ";
				}
			}
			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
		} else {
			// orientationkey
			// String businessId =
			// KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),paramater.getBusinessKeyMap());
			for (KayaMetaModel kayaModel : kayaModelList) {
				if (paramater.getPropertys()
						.containsKey(kayaModel.get(Constant.KINDKEY))) {
					// 检索条件个数
					selectCount = selectCount + 1;
					selectEmptSQL.append(orString);
					String values = paramater.getPropertys()
							.get(kayaModel.get(Constant.KINDKEY)).toString();

					switch (ConditionEnum.toEnum(StringUtil.getCondition(values))) {
					case IN:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue in (" + values + ")");
						if (StringUtil.isNotEmpty(paramater.getOrientationKey())) {
							selectEmptSQL.append(" AND businessid = '").append(paramater.getOrientationKey())
							.append("'");
						}
						selectEmptSQL.append(")");
						break;
					case DATE:
						String[] value = values.split("～");
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue >= '" + StringUtil.trim(value[0]) + "'")
						.append(" AND kindvalue <= '" + StringUtil.trim(value[1]) + "'");
						if (StringUtil.isNotEmpty(paramater.getOrientationKey())) {
							selectEmptSQL.append(" AND businessid = '").append(paramater.getOrientationKey())
							.append("'");
						}
						selectEmptSQL.append(")");
						break;
					default:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue LIKE '").append(values)
						// + "%' AND businessid = '" + businessId +
						// "')");
						.append("%'");
						if (StringUtil.isNotEmpty(paramater.getOrientationKey())) {
							selectEmptSQL.append(" AND businessid = '").append(paramater.getOrientationKey())
							.append("'");
						}
						selectEmptSQL.append(")");
						break;
					}
					orString = " OR ";
				}
			}
		}

		// OrderBy Orientationkey(14)
		selectSQL.append(" WHERE ").append(selectEmptSQL.toString() + " group by orientationkey having count(1)="
				+ selectCount + ") ORDER BY 14;");


		kayaLoger.info(selectSQL);
		paramater.setOrientationKeySet(new HashSet<String>());
		return selectSQL;
	}

	/**
	 * 检索的共通SQl文做成
	 * 
	 * @param paramater
	 * @param selectSQL
	 * @return
	 */
	private StringBuilder commonSQLIncludeSubinfo(Paramater paramater, String key) {
		String kayaModelId = paramater.getId();

		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();

		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectStringMain + tableName).append(" as m,");
		selectSQL.append(" (SELECT ").append(key).append(" FROM ").append(tableName);
		// 更新全对象取得（包含子）
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
		StringBuilder selectEmptSQL = new StringBuilder("");

		// 检索条件个数
		int selectCount = 0;
		// 复数检索条件（OR）
		String orString = "";

		// 主次表处理
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			for (KayaMetaModel kayaModel : kayaModelList) {
				if (paramater.getPropertys()
						.containsKey(kayaModel.get(Constant.KINDKEY))) {
					// 检索条件个数
					selectCount = selectCount + 1;
					selectEmptSQL.append(orString);
					String values = paramater.getPropertys()
							.get(kayaModel.get(Constant.KINDKEY)).toString();
					switch (ConditionEnum.toEnum(StringUtil.getCondition(values))){
					case  IN:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue in (" + values + "))");
						break;
					case  DATE:
						String[] value = values.split("～");
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue >= '" + StringUtil.trim(value[0]) + "'")
						.append(" AND kindvalue <= '" + StringUtil.trim(value[1]) + "')");
						break;
					default:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue LIKE '" + values + "%')");
						break;
					}
					orString = " OR ";
				}
			}
			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
		} else {
			for (KayaMetaModel kayaModel : kayaModelList) {
				if (paramater.getPropertys()
						.containsKey(kayaModel.get(Constant.KINDKEY))) {
					// 检索条件个数
					selectCount = selectCount + 1;
					selectEmptSQL.append(orString);
					String values = paramater.getPropertys()
							.get(kayaModel.get(Constant.KINDKEY)).toString();

					switch (ConditionEnum.toEnum(StringUtil.getCondition(values))) {
					case IN:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue in (" + values + ")");
						if (StringUtil.isNotEmpty(paramater.getOrientationKey())) {
							selectEmptSQL.append(" AND businessid = '").append(paramater.getOrientationKey())
							.append("'");
						}
						selectEmptSQL.append(")");
						break;
					case DATE:
						String[] value = values.split("～");
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue >= '" + StringUtil.trim(value[0]) + "'")
						.append(" AND kindvalue <= '" + StringUtil.trim(value[1]) + "'");
						if (StringUtil.isNotEmpty(paramater.getOrientationKey())) {
							selectEmptSQL.append(" AND businessid = '").append(paramater.getOrientationKey())
							.append("'");
						}
						selectEmptSQL.append(")");
						break;
					default:
						selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY))
						.append("' AND kindvalue LIKE '").append(values)
						// + "%' AND businessid = '" + businessId +
						// "')");
						.append("%'");
						if (StringUtil.isNotEmpty(paramater.getOrientationKey())) {
							selectEmptSQL.append(" AND businessid = '").append(paramater.getOrientationKey())
							.append("'");
						}
						selectEmptSQL.append(")");
						break;
					}
					orString = " OR ";
				}
			}
		}

		if (selectCount > 0) {
			selectSQL.append(" WHERE ").append(selectEmptSQL.toString() + " group by " + key + " having count(1)="
					+ selectCount + ")");
		} else {
			selectSQL.append(selectEmptSQL.toString() + " group by " + key + " )");
		}

		// TODO
		selectSQL.append(" as s WHERE m.").append(key).append(" like concat(s.").append(key).append(", '%') ");

		// 对象表指定
		String selectEmpSQL = "";
		if (paramater.getTargetTableList().size() > 0) {
			selectSQL.append(" and m.parentid in (");
			for (String tableId : paramater.getTargetTableList()) {
				selectEmpSQL = selectEmpSQL.concat(",");
				selectEmpSQL = selectEmpSQL + "'" + tableId + "'";
			}
			selectSQL.append(selectEmpSQL.substring(1));
		}
		selectSQL.append("  ORDER BY m.parentid,m.orientationkey;");

		kayaLoger.info(selectSQL);
		paramater.setOrientationKeySet(new HashSet<String>());
		return selectSQL;
	}

	/**
	 * 主键检索
	 * 
	 * @param paramater
	 * @return
	 */
	public List<Map<String, String>> selectByBusinessKeys(Paramater paramater) {
		List<Map<String, String>> kayaEntityList = new ArrayList<Map<String, String>>();
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)) {
			return kayaEntityList;
		}
		String kayaModelId = paramater.getId();
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
		String businessId = KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),
				paramater.getPropertys());
		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			selectSQL.append(" WHERE businessid = '" + businessId + "'");

		} else {
			selectSQL.append(" WHERE parentid = '" + kayaModelId + "' AND businessid = '"
					+ paramater.getOrientationKey() + "' AND businesssubid = '" + businessId + "'");
		}
		selectSQL.append(" AND parentid = '" + kayaModelId + "' ORDER BY orientationkey DESC;");
		paramater.setOrientationKeySet(new HashSet<String>());
		kayaEntityList = dBConnection.executeQuery(selectSQL.toString(), paramater.getOrientationKeySet());
		return kayaEntityList;
	}

	/**
	 * 全文检索
	 * 
	 * @param paramater
	 * @return
	 */
	public List<Map<String, String>> selectByFullText(Paramater paramater) {
		List<Map<String, String>> kayaEntityMapList = new ArrayList<Map<String, String>>();
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)) {
			return kayaEntityMapList;
		}
		String kayaModelId = paramater.getId();
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName
				+ " WHERE orientationkey IN (SELECT orientationkey FROM " + tableName + " WHERE kindvalue like '%"
				+ paramater.getText() + "%') ORDER BY orientationkey DESC;");
		// System.out.println(selectSQL.toString());
		kayaLoger.info(selectSQL);
		paramater.setOrientationKeySet(new HashSet<String>());
		kayaEntityMapList = dBConnection.executeQuery(selectSQL.toString(), paramater.getOrientationKeySet());
		return kayaEntityMapList;
	}

	/**
	 * 单表单行删除（ByBusinessKey）
	 * 
	 * @param paramater
	 * @return
	 */
	public int delete(Paramater paramater) {
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)) {
			return 0;
		}
		String kayaModelId = paramater.getId();
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
		StringBuilder deleteSQL = new StringBuilder("DELETE " + "FROM " + tableName);
		String orientationKey = "";
		// Orientation key
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			orientationKey = KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),
					paramater.getPropertys());
			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
		} else {
			orientationKey = KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),
					paramater.getOrientationKey(), paramater.getPropertys());

		}

		deleteSQL.append(" WHERE orientationkey like '" + orientationKey + "%';");
		kayaLoger.info(deleteSQL);
		dBConnection.execute(deleteSQL.toString());

		return 0;
	}

	/**
	 * 单表多行删除（ByBusinessKeys）
	 * 
	 * @param paramater
	 * @return
	 */
	public int delete(Paramaters paramaters) {
		// Table存在确认
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();
		String kayaModelId = paramaters.getId();

		for (HashMap<String,Object> propertys : paramaters.getListPropertys()) {
			String orientationKey = "";
			// Orientation key
			if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
				orientationKey = KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),
						propertys);
				// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
			} else {
				orientationKey = KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),
						paramaters.getOrientationKey(), propertys);

			}
			String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
			StringBuilder deleteSQL = new StringBuilder("DELETE " + "FROM " + tableName);
			deleteSQL.append(" WHERE orientationkey like '" + orientationKey + "%';");
			kayaLoger.info(deleteSQL);
			sqlStringList.add(deleteSQL.toString());
		}

		dBConnection.executeBatch(sqlStringList);

		return 0;
	}

	/**
	 * 多表多行删除
	 * 
	 * @param paramater
	 * @return
	 */
	public int delete(List<Paramaters> paramatersList) {
		// SqlList
		List<String> sqlStringList = new ArrayList<String>();

		for (Paramaters paramaters : paramatersList) {
			// Table存在确认
			if (!KayaModelUtils.checkTableId(paramaters)) {
				return 0;
			}
			String kayaModelId = paramaters.getId();
			for (HashMap<String,Object> propertys : paramaters.getListPropertys()) {

				String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
				StringBuilder deleteSQL = new StringBuilder("DELETE " + "FROM " + tableName);
				deleteSQL.append(" WHERE orientationkey like '" + KayaModelUtils
						.getOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys) + "%';");
				// System.out.println(deleteSQL);
				kayaLoger.info(deleteSQL);
				sqlStringList.add(deleteSQL.toString());
			}
		}

		dBConnection.executeBatch(sqlStringList);

		return 0;
	}

	// Business文字列取得
	private String getNewBusinessKey(KayaMetaModel kayaMetaModel, HashMap<String,Object> propertys, NewBusinessKey newBusinessKey) {
		StringBuilder businessValue = new StringBuilder("^");
		for (String businessStr : kayaMetaModel.getBusinessKeys()) {
			if (propertys.containsKey(businessStr)) {
				businessValue.append(propertys.get(businessStr) + "^");

				newBusinessKey.setFlg(true);
			} else {
				businessValue.append(propertys.get(businessStr) + "^");
			}
		}
		return businessValue.toString();
	}

	class NewBusinessKey {
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

	private String getWorkflowRoleSqlString(String kayaModelId, String workflowId, String actionId, String orientationKey, HashMap<String,Object> propertys,HashMap<String,Object> businesskey,User usrinfo) {
		StringBuilder insertSQL = new StringBuilder("");
		String orderNo = UtilTools.getOrderNo();
		// 判断Action是否符合自身流程要求
		KayaMetaModel kayaMetaWorkFlowModel = AccessKayaModel.getKayaModelId(workflowId);
		// 取得业务流开始元素
		String startUserTaskId = AccessKayaModel
				.getWorkFlowConnectionDes(kayaMetaWorkFlowModel.get(Constant.START));
		if (!actionId.equals(startUserTaskId)) {
			return "";
		}
		KayaMetaModel startModel = AccessKayaModel.getKayaModelId(startUserTaskId);
		if(startModel.getOrganizationItems() == null) {
			return "";
		}
		//取得生成workflow数据需要的组织信息

		List<KayaModelOrganizationItem> organizationItems = new ArrayList<KayaModelOrganizationItem>();
		organizationItems =  startModel.getOrganizationItems();

		// 取得Role信息
		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
		// NextStape
		String nextWorkFlowId = "";
		nextWorkFlowId = AccessKayaModel.getWorkFlowConnectionDes(actionId);
		if (Constant.E_GateWay.equals(AccessKayaModel.getKayaModelId(nextWorkFlowId).getMetaModelType())) {
			// ScriptEXE scriptExE = new ScriptEXE();
			// 如果返回GetWay本身ID,则申请条件异常（设置的分歧条件没有覆盖所有场合）
			if (nextWorkFlowId.equals(ScriptEXE.Exe(nextWorkFlowId, propertys))) {
				// TODO： 通知前台系统管理员修改分歧条件，覆盖所有业务场景
			} else {
				nextWorkFlowId = AccessKayaModel
						.getWorkFlowConnectionDes(ScriptEXE.Exe(nextWorkFlowId, propertys));
			}
		}

		// 取得WorkFlow信息
		String tableName = kayaMetaWorkFlowModel.getTableId();
		// 插入流程开始信息（User相关的组织信息）
		insertSQL = KayaModelUtils.getWorkFlowInsertSql(tableName);
		insertSQL.append("(");
		// orientationkey
		// 表ID等于自身ID的时候判定为主表
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel, businesskey) + "',");
			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
		} else {
			insertSQL.append("'" + KayaModelUtils.getOrientationKey(kayaMetaModel, new HashMap<String, Object>(){/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

			{
				putAll(propertys);
				putAll(businesskey);
			}}) + "',");
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

		// parentid 申请ID
		insertSQL.append("'" + kayaModelId + "',");
		// createdate
		// createuser
		// insertSQL.append("'" + (new Timestamp(System.currentTimeMillis())) +
		// "',");
		insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
		insertSQL.append("'"+ usrinfo.getUserId() + "',");
		// updatedate
		// insertSQL.append("'',");
		// updateuser
		// updatemachine
		// insertSQL.append("'" + "',");
		// insertSQL.append("'" + "')");
		// // Action row insert
		// insertSQL.append(",(");
		//

		insertSQL.append("'" + "')");

		// 取得Role子元素信息
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaWorkFlowAction(actionId);
		for (KayaMetaModel kayaModel : kayaModelList) {
			// gmeid
			insertSQL.append(",(");

			// businessid
			// businesssubid
			// 表ID等于自身ID的时候判定为主表
			// if (kayaMetaModel.getTableId().equals(kayaModelId)){
			if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {

				// Role主键处理
				// orientationkey
				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel, propertys) + "',");
				// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
			} else {
				// orientationkey
				insertSQL.append("'" + KayaModelUtils.getOrientationKey(kayaMetaModel, new HashMap<String, Object>(){/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

				{
					putAll(propertys);
					putAll(businesskey);
				}}) + "',");
			}

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
			if (Constant.ACTION.equals(kayaModel.getMetaModelType())) {
				insertSQL.append("'" + AccessKayaModel.getParentKayaModel(actionId).getName() + "',");
			} else {
				if(propertys.get(kayaModel.get(Constant.KINDKEY)) != null) {
					insertSQL.append(
							"'" + propertys.get(kayaModel.get(Constant.KINDKEY)) + "',");
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
			// insertSQL.append("'" + (new
			// Timestamp(System.currentTimeMillis())) + "',");
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			insertSQL.append("'"+ usrinfo.getUserId() + "',");
			// updatedate
			// insertSQL.append("'',");
			// updateuser
			// updatemachine
			insertSQL.append("'" + "')");
		}
		//组织信息插入
		for(int i = 0;i < organizationItems.size();i++) {

			//String orgInfo =  organizationList.get(i);
			// gmeid
			insertSQL.append(",(");

			// businessid
			// businesssubid
			// 表ID等于自身ID的时候判定为主表
			// if (kayaMetaModel.getTableId().equals(kayaModelId)){
			if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
				// orientationkey
				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel, propertys) + "',");
				// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
			} else {
				// orientationkey
				//				insertSQL.append("'" + KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),
				//						orientationKey, propertys) + "',");
				insertSQL.append("'" + KayaModelUtils.getOrientationKey(kayaMetaModel, new HashMap<String, Object>(){/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

				{
					putAll(propertys);
					putAll(businesskey);
				}}) + "',");
			}

			// relid
			insertSQL.append("'" + orderNo + "',");

			// kind
			// name
			// kindValue

			String orgKindkey = Constant.EMPTY;

			if (organizationItems.get(i).isRef()) {
				orgKindkey = organizationItems.get(i).getRefSrc();
			} else {
				orgKindkey = organizationItems.get(i).getText();
			}
			insertSQL.append("'" + orgKindkey + "',");

			insertSQL.append("'" + orgKindkey + "',");
			insertSQL.append("'',");

			if(usrinfo.getUserMap().get(orgKindkey) != null) {
				insertSQL.append(
						"'" + usrinfo.getUserMap().get(orgKindkey) + "',");
			} else {
				insertSQL.append("'',");
			}

			// kindtype
			insertSQL.append("'Organization',");
			// securitycode
			// flowcode
			// flowsubcode
			insertSQL.append("'',");
			insertSQL.append("'" + startUserTaskId + "',");
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
			// insertSQL.append("'" + (new
			// Timestamp(System.currentTimeMillis())) + "',");
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			insertSQL.append("'"+ usrinfo.getUserId() + "',");
			// updatedate
			// insertSQL.append("'',");
			// updateuser
			// updatemachine
			insertSQL.append("'" + "')");

		}


		insertSQL.append(";");

		return insertSQL.toString();
	}


	// 主表子表主键处理
	private void getInsertSqlString(HashMap<String,Object> propertys,String orientationKey, String kayaModelId, StringBuilder insertSQL,
			User user,boolean flg) {
		// 取得Role子元素信息
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);

		// 取得Role信息
		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);

		// Auto项编辑处理
		for (KayaMetaModel kayaModel : kayaModelList) {
			// 自增项处理
			if (Constant.AUTO.equals(kayaModel.get(Constant.DATATYPE))) {
				propertys.put(kayaModel.get(Constant.KINDKEY), UtilTools.getOrderNo());
			}
		}

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
			// 父类型是Product的时候判定为主表
			if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel, propertys) + "',");
				insertSQL.append("'',");
				// orientationkey
				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel, propertys) + "',");
				// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
			} else {
//				insertSQL.append("'" + KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId), businessKey) + "',");
//				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel, propertys) + "',");
//				insertSQL.append("'" + KayaModelUtils.getOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),new HashMap<String, Object>(){/**
//					 * 
//					 */
//					private static final long serialVersionUID = 1L;
//
//				{
//					putAll(propertys);
//					putAll(businessKey);
//				}}) + "',");
				insertSQL.append("'" + orientationKey + "',");
				insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel, propertys) + "',");
				// orientationkey
				// insertSQL.append("'" +
				// KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),KayaModelUtils.getBusinessKey(kayaParentMetaModel,maping.getPropertys()),maping.getPropertys())+
				// "',");
				insertSQL.append("'" + KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),
						orientationKey, propertys) + "',");
			}

			// relid
			insertSQL.append("'" + UtilTools.getOrderNo() + "',");
			// kind
			// name
			// kindValue
			// 参照的情况下，取参照元的KindKey，设置的时候利用参照本身的KindKey
			// if (Constant.PROPERTYREF.equals(kayaModel.getMetaModelType())){
			// insertSQL.append("'" +
			// kayaModel.get(Constant.REFERRED) + "',");
			// } else {
			insertSQL.append("'" + kayaModel.getGmeId() + "',");
			// }
			insertSQL.append("'" + kayaModel.get(Constant.KINDKEY) + "',");
			insertSQL.append("'" + kayaModel.getName() + "',");

			if (propertys.get(kayaModel.get(Constant.KINDKEY)) == null) {
				insertSQL.append("'',");
			} else {
				insertSQL.append(
						"'" + propertys.get(kayaModel.get(Constant.KINDKEY)) + "',");
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
			// insertSQL.append("'" + (new
			// Timestamp(System.currentTimeMillis())) + "',");
			insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
			insertSQL.append(user.getUserId()).append(",");
			//insertSQL.append("'chenliang',");
			// updatedate
			// insertSQL.append("'',");
			// updateuser
			// updatemachine
			// insertSQL.append("null" + ",");
			insertSQL.append("'" + "')");
		}

	}

	// 主表子表主键处理
	private void getInsertSqlString(HashMap<String,Object> propertys, String kayaModelId, StringBuilder insertSQL, String orientationKey,
			List<String> sqlStringList, User user,boolean flg) {
		// 取得Role子元素信息
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);

		// 取得Role信息
		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
		// 主键编辑处理
		for (KayaMetaModel kayaModel : kayaModelList) {
			// 自增项处理
			if (Constant.AUTO.equals(kayaModel.get(Constant.DATATYPE))) {
				propertys.put(kayaModel.get(Constant.KINDKEY), UtilTools.getOrderNo());
			}
		}
		for (KayaMetaModel kayaModel : kayaModelList) {
			if (sqlStringList.contains(kayaModel.get(Constant.KINDKEY))) {
				// gmeid
				if (flg) {
					insertSQL.append("(");
					flg = false;
				} else {
					insertSQL.append(",(");
				}

				// businessid
				// businesssubid
				// 表ID等于自身ID的时候判定为主表
				if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
					insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel, propertys) + "',");
					insertSQL.append("'',");
					// orientationkey
					insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel, propertys) + "',");
					// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
				} else {
					// insertSQL.append("'" +
					// KayaModelUtils.getBusinessKey(kayaParentMetaModel,maping.getPropertys())
					// + "',");
					insertSQL.append("'" + orientationKey + "',");
					insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel, propertys) + "',");
					// orientationkey
					// insertSQL.append("'" +
					// KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),KayaModelUtils.getBusinessKey(kayaParentMetaModel,maping.getPropertys()),maping.getPropertys())+
					// "',");
					insertSQL
					.append("'" + KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),
							orientationKey, propertys) + "',");
				}

				// relid
				insertSQL.append("'" + UtilTools.getOrderNo() + "',");
				// kind
				// name
				// kindValue
				// 参照的情况下，取参照元的KindKey，设置的时候利用参照本身的KindKey
				// if
				// (Constant.PROPERTYREF.equals(kayaModel.getMetaModelType())){
				// insertSQL.append("'" +
				// kayaModel.get(Constant.REFERRED) + "',");
				// } else {
				insertSQL.append("'" + kayaModel.getGmeId() + "',");
				// }
				insertSQL.append("'" + kayaModel.get(Constant.KINDKEY) + "',");
				insertSQL.append("'" + kayaModel.getName() + "',");

				if (propertys.get(kayaModel.get(Constant.KINDKEY)) == null) {
					insertSQL.append("'',");
				} else {
					insertSQL.append(
							"'" + propertys.get(kayaModel.get(Constant.KINDKEY)) + "',");
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
				// insertSQL.append("'" + (new
				// Timestamp(System.currentTimeMillis())) + "',");
				insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
				insertSQL.append(user.getUserId()).append(",");
				// updatedate
				// insertSQL.append("'',");
				// updateuser
				// updatemachine
				// insertSQL.append("null" + ",");
				insertSQL.append("'" + "')");
			}
		}

	}

	private void getUpdateSqlString(HashMap<String,Object> propertys, String kayaModelId, List<String> sqlStringList,
			String orientationKey,User user) {

		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
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
		updateUserInfoSQL.append(",updateuser = " + user);

		StringBuilder updateWhere = new StringBuilder(" WHERE kind IN (");
		StringBuilder updateKeyOnlyWhere = new StringBuilder(" WHERE kind IN (");
		boolean flg = true;
		boolean keyOnlyflg = true;
		NewBusinessKey newBusinessKey = new NewBusinessKey();
		newBusinessKey.setFlg(false);
		for (KayaMetaModel kayaModel : kayaModelList) {
			// 参照的情况下，取参照元的KindKey，设置的时候利用参照本身的KindKey
			if (Constant.PROPERTYREF.equals(kayaModel.getMetaModelType())) {
				// 只处理更新字段
				if (propertys.containsKey(kayaModel.get(Constant.KINDKEY))) {
					updateSQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY) + "' THEN '"
							+ propertys.get(kayaModel.get(Constant.KINDKEY)) + "'");
					if (keyOnlyflg) {
						updateKeyOnlyWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
						keyOnlyflg = false;
					} else {
						updateKeyOnlyWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
					}

				}
				updateBusinessIdSQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY) + "' THEN '"
						+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey) + "'");
				updateOrientationKeySQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY)
				+ "' THEN replace(orientationkey," + "'"
				+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),
						propertys)
				+ "'," + "'"
				+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey)
				+ "')");
				if (flg) {
					updateWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
					flg = false;
				} else {
					updateWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
				}
			} else {
				// 只处理更新字段
				if (propertys.containsKey(kayaModel.get(Constant.KINDKEY))) {
					updateSQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY) + "' THEN '"
							+ propertys.get(kayaModel.get(Constant.KINDKEY)) + "'");
					if (keyOnlyflg) {
						updateKeyOnlyWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
						keyOnlyflg = false;
					} else {
						updateKeyOnlyWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
					}
				}
				updateBusinessIdSQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY) + "' THEN '"
						+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey) + "'");
				updateOrientationKeySQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY)
				+ "' THEN replace(orientationkey," + "'"
				+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),
						propertys)
				+ "'," + "'"
				+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey)
				+ "')");

				if (flg) {
					updateWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
					flg = false;
				} else {
					updateWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
				}
			}
		}
		updateSQL.append(" ELSE kindvalue END)");
		// 如果是Role主节点
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			// 主键被更新
			if (newBusinessKey.isFlg()) {
				updateSQL.append(updateBusinessIdSQL.toString() + " END)");
				updateSQL.append(updateOrientationKeySQL.toString() + " END)");
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(updateWhere
						.append(") AND businessid = '" + KayaModelUtils
								.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys))
						+ "'");
			} else {
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(updateKeyOnlyWhere
						.append(") AND businessid = '" + KayaModelUtils
								.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys))
						+ "'");
			}

		} else {
			if (newBusinessKey.isFlg()) {
				updateSQL.append(updateBusinessIdSQL.toString() + " END)");
				updateSQL.append(updateOrientationKeySQL.toString() + " END)");
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(
						updateWhere
						.append(") AND businessid = '"
								// +
								// KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),maping.getPropertys())
								+ orientationKey
								+ "' AND businesssubid = '" + KayaModelUtils.getBusinessKey(
										AccessKayaModel.getKayaModelId(kayaModelId), propertys))
						+ "'");
			} else {
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(
						updateKeyOnlyWhere
						.append(") AND businessid = '"
								// +
								// KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),maping.getPropertys())
								+ orientationKey
								+ "' AND businesssubid = '" + KayaModelUtils.getBusinessKey(
										AccessKayaModel.getKayaModelId(kayaModelId), propertys))
						+ "'");
			}
		}
		updateSQL.append(" AND parentid = '" + kayaModelId + "';");
		// 以多条SQL方式执行操作
		sqlStringList.add(updateSQL.toString());

		// OrientationKey更新（主键被更新时）
		if (newBusinessKey.isFlg()) {
			String _updateOrientationKeySQL = "UPDATE " + tableName + " SET orientationkey = REPLACE(orientationkey,'"
					+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys)
					+ "','" + getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey)
					+ "'),businessid = (CASE businessid WHEN '"
					+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys)
					+ "' THEN '"
					+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey)
					+ "' ELSE businessid END)" + updateUserInfoSQL.toString() + " WHERE orientationkey LIKE '"
					+ KayaModelUtils.getOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),
							propertys)
					+ "%'";
			// System.out.println(_updateOrientationKeySQL);
			kayaLoger.info(_updateOrientationKeySQL);
			sqlStringList.add(_updateOrientationKeySQL);
		}
	}

	private void getUpdateSqlString(HashMap<String,Object> propertys, String kayaModelId, List<String> sqlStringList,
			List<String> insertFieldList, String orientationKey, User user) {

		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId();
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
		updateUserInfoSQL.append(",updateuser = " + user.getUserId());

		StringBuilder updateWhere = new StringBuilder(" WHERE kind IN (");
		StringBuilder updateKeyOnlyWhere = new StringBuilder(" WHERE kind IN (");
		boolean flg = true;
		boolean keyOnlyflg = true;
		NewBusinessKey newBusinessKey = new NewBusinessKey();
		newBusinessKey.setFlg(false);
		for (KayaMetaModel kayaModel : kayaModelList) {
			if (!insertFieldList.contains(kayaModel.get(Constant.KINDKEY) + "'")) {

				// 参照的情况下，取参照元的KindKey，设置的时候利用参照本身的KindKey
				if (Constant.PROPERTYREF.equals(kayaModel.getMetaModelType())) {

					// 只处理更新字段
					if (propertys.containsKey(kayaModel.get(Constant.KINDKEY))) {
						updateSQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY) + "' THEN '"
								+ propertys.get(kayaModel.get(Constant.KINDKEY)) + "'");
						if (keyOnlyflg) {
							updateKeyOnlyWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
							keyOnlyflg = false;
						} else {
							updateKeyOnlyWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
						}

					}
					updateBusinessIdSQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY)
					+ "' THEN '"
					+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey)
					+ "'");
					updateOrientationKeySQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY)
					+ "' THEN replace(orientationkey," + "'"
					+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),
							propertys)
					+ "'," + "'"
					+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey)
					+ "')");
					if (flg) {
						updateWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
						flg = false;
					} else {
						updateWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
					}
				} else {
					// 只处理更新字段
					if (propertys.containsKey(kayaModel.get(Constant.KINDKEY))) {
						updateSQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY) + "' THEN '"
								+ propertys.get(kayaModel.get(Constant.KINDKEY)) + "'");
						if (keyOnlyflg) {
							updateKeyOnlyWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
							keyOnlyflg = false;
						} else {
							updateKeyOnlyWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
						}
					}
					updateBusinessIdSQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY)
					+ "' THEN '"
					+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey)
					+ "'");
					updateOrientationKeySQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY)
					+ "' THEN replace(orientationkey," + "'"
					+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),
							propertys)
					+ "'," + "'"
					+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey)
					+ "')");

					if (flg) {
						updateWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
						flg = false;
					} else {
						updateWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
					}
				}
			}
		}
		updateSQL.append(" ELSE kindvalue END)");
		// 如果是Role主节点
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			// 主键被更新
			if (newBusinessKey.isFlg()) {
				updateSQL.append(updateBusinessIdSQL.toString() + " END)");
				updateSQL.append(updateOrientationKeySQL.toString() + " END)");
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(updateWhere
						.append(") AND businessid = '" + KayaModelUtils
								.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys))
						+ "'");
			} else {
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(updateKeyOnlyWhere
						.append(") AND businessid = '" + KayaModelUtils
								.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys))
						+ "'");
			}

		} else {
			if (newBusinessKey.isFlg()) {
				updateSQL.append(updateBusinessIdSQL.toString() + " END)");
				updateSQL.append(updateOrientationKeySQL.toString() + " END)");
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(
						updateWhere
						.append(") AND businessid = '"
								// +
								// KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),maping.getPropertys())
								+ orientationKey
								+ "' AND businesssubid = '" + KayaModelUtils.getBusinessKey(
										AccessKayaModel.getKayaModelId(kayaModelId), propertys))
						+ "'");
			} else {
				// 更新日时，更新者
				updateSQL.append(updateUserInfoSQL.toString());
				updateSQL.append(
						updateKeyOnlyWhere
						.append(") AND businessid = '"
								// +
								// KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),maping.getPropertys())
								+ orientationKey
								+ "' AND businesssubid = '" + KayaModelUtils.getBusinessKey(
										AccessKayaModel.getKayaModelId(kayaModelId), propertys))
						+ "'");
			}
		}
		updateSQL.append(" AND parentid = '" + kayaModelId + "';");
		// 以多条SQL方式执行操作
		sqlStringList.add(updateSQL.toString());

		// OrientationKey更新（主键被更新时）
		if (newBusinessKey.isFlg()) {
			String _updateOrientationKeySQL = "UPDATE " + tableName + " SET orientationkey = REPLACE(orientationkey,'"
					+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys)
					+ "','" + getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey)
					+ "'),businessid = (CASE businessid WHEN '"
					+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys)
					+ "' THEN '"
					+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId), propertys, newBusinessKey)
					+ "' ELSE businessid END)" + updateUserInfoSQL.toString() + " WHERE orientationkey LIKE '"
					+ KayaModelUtils.getOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),
							propertys)
					+ "%'";
			// System.out.println(_updateOrientationKeySQL);
			kayaLoger.info(_updateOrientationKeySQL);
			sqlStringList.add(_updateOrientationKeySQL);
		}
	}
}
