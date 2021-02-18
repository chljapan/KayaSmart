//package com.smartkaya.dao;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//
//import com.smartkaya.bean.Propertys;
//import com.smartkaya.bean.Paramater;
//import com.smartkaya.bean.Paramaters;
//import com.smartkaya.constant.Constant;
//import com.smartkaya.core.AccessKayaModel;
//import com.smartkaya.core.DbConnection;
//import com.smartkaya.model.KayaMetaModel;
//import com.smartkaya.utils.UtilTools;
//
///**
// * KaYaMode 通用表CRUD操作类
// * @author LiangChen　2018/4/30
// * @version 1.0.0
// */
//public final class KayaTestSQLExecute {
//	private DbConnection dBConnection = AccessKayaModel.getDbConnection();
//
//	public KayaTestSQLExecute() {
//	}
//
//	/**
//	 * 单表单条插入处理
//	 * Single-table single-row insert processing
//	 * @param paramater
//	 * @return
//	 */
//	public int insert(Paramater paramater) {
//		// Table存在确认
//		if (!KayaModelUtils.checkTableId(paramater)){
//			return 0;
//		}
//		String kayaModelId = paramater.getId();
//		// SqlList
//		List<String> sqlStringList = new ArrayList<String>();
//		//StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);
//		
//		StringBuilder insertSQL = getInsertCloumsSqlString(kayaModelId);
//				//+ ") VALUES ");
//		boolean flg = true;
//		getInsertSqlString(paramater.getMapping(), kayaModelId, insertSQL, paramater.getOrientationKey(),flg);
//		insertSQL.append(";");
//		sqlStringList.add(insertSQL.toString());
//
//		// 监听业务流程处理
//		if (!Constant.EMPTY.equals(AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId())) {
//			// TODO:验证流程ID
//			sqlStringList.add(getWorkflowStartSqlString(paramater.getId(),paramater.getActionid(),paramater.getMapping()).toString());
//		}
//		dBConnection.executeBatch(sqlStringList);
//		System.out.println(insertSQL.toString());
//		return 0;
//
//	}
//
//	/**
//	 * 单表多条插入处理
//	 * Single-table multi-row insert processing
//	 * @param paramaters 统一参数
//	 * @return
//	 */
//	public int insert(Paramaters paramaters) {
//		// Table存在确认 子表的时候需要处理
//		//		if (!KayaModelUtils.checkTableId(paramaters)){
//		//			return 0;
//		//		}
//		List<String> sqlStringList = new ArrayList<String>();
//
//		String kayaModelId = paramaters.getId();
//		// 取得Role子元素信息
//		//	List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
//		// 取得Role信息
//		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
//		String tableName = kayaMetaModel.getTableId().replace('-','_');
//		StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);
//
//		boolean flg = true;
//		for (Propertys subEntity:paramaters.getMappings()) {
//			getInsertSqlString(subEntity,kayaModelId,insertSQL,paramaters.getOrientationKey(),flg);
//
//			// 多条编辑","处理
//			flg = false;
//			String workFlowId = AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId();
//			if (!Constant.EMPTY.equals(workFlowId) && workFlowId != null) {
//				// TODO:验证流程ID
//				String workflowSql = getWorkflowStartSqlString(paramaters.getId(),workFlowId,subEntity).toString();
//				System.out.println(workflowSql);
//				sqlStringList.add(workflowSql);
//			}
//		}
//		insertSQL.append(";");
//		sqlStringList.add(insertSQL.toString());
//		dBConnection.executeBatch(sqlStringList);
//
//		System.out.println(insertSQL.toString());
//		return 0;
//
//	}
//
//	/**
//	 * 多表多条插入处理
//	 * @param paramaters 统一参数
//	 * @return
//	 */
//	public int insert(List<Paramaters> paramatersList) {
//		// SqlList
//		List<String> sqlStringList = new ArrayList<String>();
//
//		for (Paramaters paramaters: paramatersList) {
//			// Table存在确认
//			if (!KayaModelUtils.checkTableId(paramaters)){
//				return 0;
//			}
//			String kayaModelId = paramaters.getId();
//			// 取得Role信息
//			KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
//			String tableName = kayaMetaModel.getTableId().replace('-','_');
//			StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);
//			// 只执行一次处理Flg
//			boolean flg = true;
//			if (!Constant.EMPTY.equals(AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId())) {
//				// TODO:验证流程ID
//				System.out.println(AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId());
//			}
//			for (Propertys subEntity:paramaters.getMappings()) {
//
//				getInsertSqlString(subEntity,kayaModelId,insertSQL,paramaters.getOrientationKey(),flg);
//				flg = false;
//			}
//
//			insertSQL.append(";");
//			System.out.println(insertSQL.toString());
//			sqlStringList.add(insertSQL.toString());
//		}
//
//		dBConnection.executeBatch(sqlStringList);
//		return 0;
//
//	}
//
//
//	/**
//	 * 单表单行更新处理
//	 * Single-table single-row update processing
//	 * @param paramater
//	 * @return
//	 */
//	public int update(Paramater paramater) {
//		// Table存在确认
//		if (!KayaModelUtils.checkTableId(paramater)){
//			return 0;
//		}
//		String kayaModelId = paramater.getId();
//
//		List<String> sqlStringList = new ArrayList<String>();
//		getUpdateSqlString(paramater.getMapping(),kayaModelId,sqlStringList);
//
//		dBConnection.executeBatch(sqlStringList);
//
//		return 0;
//	}
//
//	/**
//	 * 单表多行更新处理
//	 * Single-table multi-row update processing
//	 * @param paramaters
//	 * @return
//	 */
//	public int update(Paramaters paramaters) {
//		// Table存在确认
//		//		if (!KayaModelUtils.checkTableId(paramaters)){
//		//			return 0;
//		//		}
//		String kayaModelId = paramaters.getId();
//		// SqlList
//		List<String> sqlStringList = new ArrayList<String>();
//		for (Propertys subEntity: paramaters.getMappings()) {
//			getUpdateSqlString(subEntity,kayaModelId,sqlStringList);
//		}
//		dBConnection.executeBatch(sqlStringList);
//		return 0;
//	}
//
//	/**
//	 * 追加字段更新处理
//	 * Single-table multi-row update processing
//	 * @param paramaters
//	 * @return
//	 */
//	public int update(Paramaters paramaters,List<String> insertFieldList) {
//		// Table存在确认
//		//		if (!KayaModelUtils.checkTableId(paramaters)){
//		//			return 0;
//		//		}
//		String kayaModelId = paramaters.getId();
//		// SqlList
//		List<String> sqlStringList = new ArrayList<String>();
//		for (Propertys subEntity: paramaters.getMappings()) {
//			getUpdateSqlString(subEntity,kayaModelId,sqlStringList,insertFieldList);
//		}
//		
//		
//		// insert处理
//		// 取得Role信息
//		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
//		String tableName = kayaMetaModel.getTableId().replace('-','_');
//		StringBuilder insertSQL = KayaModelUtils.getInsertSql(tableName);
//		// 只执行一次处理Flg
//		boolean flg = true;
//
//		for (Propertys subEntity:paramaters.getMappings()) {
//
//			getInsertSqlString(subEntity,kayaModelId,insertSQL,paramaters.getOrientationKey(),insertFieldList,flg);
//			flg = false;
//		}
//
//		insertSQL.append(";");
//		System.out.println(insertSQL.toString());
//		sqlStringList.add(insertSQL.toString());
//
//
//		dBConnection.executeBatch(sqlStringList);
//		return 0;
//	}
//
//	/**
//	 * 多表多行更新处理
//	 * Single-table multi-row update processing
//	 * @param paramaters
//	 * @return
//	 */
//	public int update(List<Paramaters> paramatersList) {
//		// SqlList
//		List<String> sqlStringList = new ArrayList<String>();
//		for (Paramaters paramaters:paramatersList) {
//			// Table存在确认
//			if (!KayaModelUtils.checkTableId(paramaters)){
//				return 0;
//			}
//			String kayaModelId = paramaters.getId();
//			for (Propertys subEntity: paramaters.getMappings()) {
//				getUpdateSqlString(subEntity,kayaModelId,sqlStringList);
//			}
//		}
//		dBConnection.executeBatch(sqlStringList);
//		return 0;
//	}
//
//	//		// TODO:根据需求来决定是否开放此方法
//	//		/**
//	//		 * 
//	//		 * @param multipleParamater
//	//		 * @param addFlg 新追加的列是否允许更新（需要插入新行）
//	//		 * 
//	//		 * @return
//	//		 * @throws KayaModelException 
//	//		 */
//	////		public int updateKayaModelFlowS(MultipleParamater multipleParamater, boolean addFlg) {
//	////			return 0;
//	////		}
//
//	/**
//	 * 任意键检索
//	 * @param paramater
//	 * @return
//	 */
//	public List<Map<String,String>> selectByFreeKind(Paramater paramater) {
//		List<Map<String,String>> kayaEntityList = new ArrayList<Map<String,String>>();
//		// Table存在确认
//		if (!KayaModelUtils.checkTableId(paramater)){
//			return kayaEntityList;
//		}
//		String kayaModelId = paramater.getId();
//		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
//		// 更新全对象取得（包含子）
//		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
//
//		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
//		selectSQL.append(" WHERE orientationkey IN (");
//		boolean flg = true;
//		StringBuilder selectEmptSQL = new StringBuilder("");
//
//		// 主次表处理
//		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())){
//			for (KayaMetaModel kayaModel: kayaModelList) {
//				if (paramater.getMapping().getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))){
//					selectEmptSQL.append("SELECT orientationkey FROM " + tableName 
//							+ " WHERE (kind = '" + kayaModel.get(Constant.KINDKEY) + "' AND kindvalue LIKE '"
//							+ paramater.getMapping().getPropertys().get(kayaModel.get(Constant.KINDKEY))
//							+ "%')");
//				}
//			}
//			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
//		} else {
//			// orientationkey
//			String businessId = KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),paramater.getBusinessKeyMap());
//			for (KayaMetaModel kayaModel: kayaModelList) {
//				if (paramater.getMapping().getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))){
//					if(flg) {
//
//						selectEmptSQL.append("SELECT orientationkey FROM " + tableName 
//								+ " WHERE (kind = '" + kayaModel.get(Constant.KINDKEY) + "' AND kindvalue LIKE '"
//								+ paramater.getMapping().getPropertys().get(kayaModel.get(Constant.KINDKEY))
//								+ "%' AND businessid = '" +  businessId  + "')");
//
//						flg = false;
//					}else {
//						selectEmptSQL.insert(0, " SELECT orientationkey FROM " + tableName + " WHERE (orientationkey IN (");
//						selectEmptSQL.append(")) AND (kind='" + kayaModel.get(Constant.KINDKEY) 
//								+ "' AND kindvalue LIKE '" + paramater.getMapping().getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "%' AND businessid = '" +  businessId  + "')");
//					}
//				}
//			}
//
//
//		}
//
//
//		selectSQL.append(selectEmptSQL.toString() + ") ORDER BY orientationkey DESC;");
//
//		System.out.println(selectSQL.toString());
//		paramater.setOrientationKeySet(new HashSet<String>());
//		kayaEntityList = dBConnection.executeQuery(selectSQL.toString(),paramater.getOrientationKeySet());
//		return kayaEntityList;
//	}
//
//	/**
//	 * 主键检索
//	 * @param paramater
//	 * @return
//	 */
//	public List<Map<String,String>> selectByBusinessKeys(Paramater paramater) {
//		List<Map<String,String>> kayaEntityList =  new ArrayList<Map<String,String>>();
//		// Table存在确认
//		if (!KayaModelUtils.checkTableId(paramater)){
//			return kayaEntityList;
//		}
//		String kayaModelId= paramater.getId();
//		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
//		String businessId = KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),paramater.getMapping().getPropertys());
//		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
//		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
//			selectSQL.append(" WHERE businessid = '" + businessId + "'");
//
//		} else {
//			selectSQL.append(" WHERE parentid = '" + kayaModelId
//					+ "' AND businessid = '" + KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),paramater.getMapping().getPropertys()) 
//					+ "' AND businesssubid = '" +  businessId + "'");
//		}
//		selectSQL.append(" AND parentid = '" + kayaModelId + "' ORDER BY orientationkey DESC;");
//		paramater.setOrientationKeySet(new HashSet<String>());
//		kayaEntityList = dBConnection.executeQuery(selectSQL.toString(),paramater.getOrientationKeySet());
//		return kayaEntityList;
//	}
//
//	/**
//	 * 全文检索
//	 * @param paramater
//	 * @return
//	 */
//	public List<Map<String,String>> selectByFullText(Paramater paramater) {
//		List<Map<String,String>> kayaEntityMapList = new ArrayList<Map<String,String>>();
//		// Table存在确认
//		if (!KayaModelUtils.checkTableId(paramater)){
//			return kayaEntityMapList;
//		}
//		String kayaModelId= paramater.getId();
//		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
//		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName + " WHERE orientationkey IN (SELECT orientationkey FROM " + tableName 
//				+ " WHERE kindvalue like '%" + paramater.getText() + "%') ORDER BY orientationkey DESC;");
//		System.out.println(selectSQL.toString());
//		paramater.setOrientationKeySet(new HashSet<String>());
//		kayaEntityMapList = dBConnection.executeQuery(selectSQL.toString(),paramater.getOrientationKeySet());
//		return kayaEntityMapList;
//	}
//
//	/**
//	 * 单表单行删除（ByBusinessKey）
//	 * @param paramater
//	 * @return
//	 */
//	public int delete(Paramater paramater) {
//		// Table存在确认
//		if (!KayaModelUtils.checkTableId(paramater)){
//			return 0;
//		}
//		String kayaModelId= paramater.getId();
//		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
//		StringBuilder deleteSQL = new StringBuilder("DELETE "
//				+ "FROM " + tableName);
//		deleteSQL.append(" WHERE orientationkey like '" + KayaModelUtils.getOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),paramater.getMapping().getPropertys()) + "%';");
//		System.out.println(deleteSQL);
//		dBConnection.execute(deleteSQL.toString());
//
//		return 0;
//	}
//	/**
//	 * 单表多行删除（ByBusinessKeys）
//	 * @param paramater
//	 * @return
//	 */
//	public int delete(Paramaters paramaters) {
//		// Table存在确认
//		// SqlList
//		List<String> sqlStringList = new ArrayList<String>();
//		String kayaModelId= paramaters.getId();
//		
//		for(Propertys propertys:paramaters.getMappings()) {
//			String orientationKey = "";
//			if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())){
//				// orientationkey
//				orientationKey =  KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),propertys.getPropertys());
//				// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
//			} else {
//				// orientationkey
//				orientationKey =   KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),propertys.getPropertys()),propertys.getPropertys());
//			}
//			String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
//			StringBuilder deleteSQL = new StringBuilder("DELETE "
//					+ "FROM " + tableName);
//			deleteSQL.append(" WHERE orientationkey like '" + orientationKey + "%';");
//			System.out.println(deleteSQL);
//			sqlStringList.add(deleteSQL.toString());
//		}
//
//		dBConnection.executeBatch(sqlStringList);
//
//		return 0;
//	}
//
//	/**
//	 * 多表多行删除
//	 * @param paramater
//	 * @return
//	 */
//	public int delete(List<Paramaters> paramatersList) {
//		// SqlList
//		List<String> sqlStringList = new ArrayList<String>();
//
//		for (Paramaters paramaters:paramatersList) {
//			// Table存在确认
//			if (!KayaModelUtils.checkTableId(paramaters)){
//				return 0;
//			}
//			String kayaModelId= paramaters.getId();
//			for(Propertys propertys:paramaters.getMappings()) {
//
//				String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
//				StringBuilder deleteSQL = new StringBuilder("DELETE "
//						+ "FROM " + tableName);
//				deleteSQL.append(" WHERE orientationkey like '" + KayaModelUtils.getOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),propertys.getPropertys()) + "%';");
//				System.out.println(deleteSQL);
//				sqlStringList.add(deleteSQL.toString());
//			}
//		}
//
//		//dBConnection.executeBatch(sqlStringList);
//
//		return 0;
//	}
//
//	// Business文字列取得
//	private String getNewBusinessKey(KayaMetaModel kayaMetaModel,Propertys propertys ,NewBusinessKey newBusinessKey){
//		StringBuilder businessValue = new StringBuilder("^");
//		Map<String,Object> subEntity = propertys.getPropertys();
//		for (String businessStr:kayaMetaModel.getBusinessKeys()) {
//			if (subEntity.containsKey(businessStr)) {
//				businessValue.append(subEntity.get(businessStr) + "^");
//
//				newBusinessKey.setFlg(true);;
//			} else {
//				businessValue.append(propertys.getPropertys().get(businessStr) + "^");
//			}
//		}
//		return businessValue.toString();
//	}
//
//	class NewBusinessKey{
//		private boolean flg;
//		private String businessKey;
//		public boolean isFlg() {
//			return flg;
//		}
//		public void setFlg(boolean flg) {
//			this.flg = flg;
//		}
//		public String getBusinessKey() {
//			return businessKey;
//		}
//		public void setBusinessKey(String businessKey) {
//			this.businessKey = businessKey;
//		}
//	}
//
//	// Insert 字符串编辑方法
//	//	private StringBuilder getInsertSql(String tableName){
//	//		StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " ("
//	//				+ "businessid,businesssubid,orientationkey,relid,gmeid,kind,name,kindvalue,kindtype,securitycode,"
//	//				+ "flowcode,flowsubcode,startdate,enddate,withdrawaldate,parentid,createdate,createuser,updatemachine"
//	//				+ ") VALUES ");
//	//		return insertSQL;
//	//	}
//
//	private String getWorkflowStartSqlString(String kayaModelId, String workflowId, Propertys propertys) {
//		StringBuilder insertSQL = new StringBuilder("");
//
//		// 判断Action是否符合自身流程要求
//		KayaMetaModel kayaMetaWorkFlowModel = AccessKayaModel.getKayaModelId(workflowId);
//		// 取得业务流开始元素
//		String startUserTaskId = AccessKayaModel.getWorkFlowConnectionDes(kayaMetaWorkFlowModel.get(Constant.START));
//		// 取得Role信息
//		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
//		// 取得WorkFlow信息
//		String tableName = kayaMetaWorkFlowModel.getTableId().replace('-','_');
//		insertSQL = KayaModelUtils.getInsertSql(tableName);
//		insertSQL.append("(");
//		// businessid
//		// businesssubid
//		// 表ID等于自身ID的时候判定为主表
//		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())){
//			insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,propertys.getPropertys()) + "',");
//			insertSQL.append("'',");
//			// orientationkey
//			insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,propertys.getPropertys()) + "',");
//			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
//		} else {
//			insertSQL.append("'" + KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),propertys.getPropertys()) + "',");
//			insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,propertys.getPropertys()) + "',");
//			// orientationkey
//			insertSQL.append("'" + KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),propertys.getPropertys()),propertys.getPropertys()) + "',");
//			//KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),mapping.getPropertys()),mapping.getPropertys())
//		}
//
//		// relid
//		insertSQL.append("'" + UtilTools.getOrderNo() + "',");
//
//		// gmeid
//		// kind
//		// name
//		// kindValue
//
//		insertSQL.append("'" + kayaMetaModel.getGmeId() + "',");
//		insertSQL.append("'" + kayaMetaModel.get(Constant.KINDKEY) + "',");
//		insertSQL.append("'" + kayaMetaModel.getName() + "',");
//
//
//		// UserTaskID(人为启动流程？还是自动触发流程？)
//		//		insertSQL.append("'" + AccessKayaModel.getKayaModelId(workFlowListener.getActionId()).getParentId() + "',");
//		insertSQL.append("'" + AccessKayaModel.getKayaModelId(kayaMetaWorkFlowModel.get(Constant.START)).getName() + "',");
//
//		// kindtype
//		insertSQL.append("'" + kayaMetaModel.getMetaModelType() + "',");
//		// securitycode
//		// flowcode
//
//		// flowsubcode
//		insertSQL.append("'',");
//		insertSQL.append("'" + startUserTaskId + "',");
//		insertSQL.append("'" + kayaMetaWorkFlowModel.get(Constant.START) + "',");
//		// startdate
//		// enddate
//		// withdrawaldate
//		insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
//		insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
//		insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
//
//		// parentid
//		insertSQL.append("'" + kayaMetaWorkFlowModel.getParentId() + "',");
//		// createdate
//		// createuser
//		//		insertSQL.append("'" + (new Timestamp(System.currentTimeMillis())) + "',");
//		insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
//		insertSQL.append("'chenliang',");
//		// updatedate
//		//			insertSQL.append("'',");
//		// updateuser
//		// updatemachine
//		//			insertSQL.append("'" + "',");
//		//		insertSQL.append("'" + "')");
//		//		// Action row insert
//		//		insertSQL.append(",(");
//		//		
//
//		insertSQL.append("'" + "');");	
//
//
//		return insertSQL.toString();
//	}
//	
//	// 主表子表主键处理
//	private StringBuilder getInsertCloumsSqlString(String kayaModelId){
//		// 取得Role子元素信息
//		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
//
//		// 取得Role信息
//		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
//		String tableName = kayaMetaModel.getTableId().replace('-','_');
//		StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " (");
//		
//		boolean flg = true;
//		for (KayaMetaModel kayaModel: kayaModelList) {
//			// gmeid
//			if(flg) {
//				insertSQL.append(kayaModel.get(Constant.KINDKEY));
//				flg = false;
//			}else {
//				insertSQL.append("," + kayaModel.get(Constant.KINDKEY));
//			}
//		}
//		insertSQL.append(")  VALUES (");
//		
//		return insertSQL;
//	}
//	
//	
//	// 主表子表主键处理
//	private void getInsertSqlString(Propertys maping,String kayaModelId, StringBuilder insertSQL,String orientationKey,boolean flg){
//		// 取得Role子元素信息
//		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
//		for (KayaMetaModel kayaModel: kayaModelList) {
//			if(flg) {
//				flg = false;
//			}else {
//				insertSQL.append(",");
//			}
//			if(maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) == null) {
//				insertSQL.append("''");
//			} else {
//				insertSQL.append("'" + maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "',");
//			}
//		}
//
//	}
//
//	// 主表子表主键处理
//	private void getInsertSqlString(Propertys maping,String kayaModelId, StringBuilder insertSQL,String orientationKey,List<String> sqlStringList,boolean flg){
//		// 取得Role子元素信息
//		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
//		// 取得ParentRole信息
//		KayaMetaModel kayaParentMetaModel = AccessKayaModel.getParentKayaModel(kayaModelId);
//
//		// 取得Role信息
//		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
//		for (KayaMetaModel kayaModel: kayaModelList) {
//			if (sqlStringList.contains(kayaModel.get(Constant.KINDKEY))) {
//				// gmeid
//				if(flg) {
//					insertSQL.append("(");
//					flg = false;
//				}else {
//					insertSQL.append(",(");
//				}
//
//				// businessid
//				// businesssubid
//				// 表ID等于自身ID的时候判定为主表
//				if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())){
//					insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,maping.getPropertys()) + "',");
//					insertSQL.append("'',"); 
//					// orientationkey
//					insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,maping.getPropertys()) + "',");
//					// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
//				} else {
//					insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaParentMetaModel,maping.getPropertys()) + "',");
//					insertSQL.append("'" + KayaModelUtils.getBusinessKey(kayaMetaModel,maping.getPropertys()) + "',");
//					// orientationkey
//					insertSQL.append("'" + KayaModelUtils.editOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),KayaModelUtils.getBusinessKey(kayaParentMetaModel,maping.getPropertys()),maping.getPropertys())+ "',");
//				}
//
//				// relid
//				insertSQL.append("'" + UtilTools.getOrderNo() + "',");
//				// kind
//				// name
//				// kindValue
//				// 参照的情况下，取参照元的KindKey，设置的时候利用参照本身的KindKey
//				//			if (Constant.PROPERTYREF.equals(kayaModel.getMetaModelType())){
//				//				insertSQL.append("'" + kayaModel.get(Constant.REFERRED) + "',");
//				//			} else {
//				insertSQL.append("'" + kayaModel.getGmeId() + "',");
//				//			}
//				insertSQL.append("'" + kayaModel.get(Constant.KINDKEY) + "',");
//				insertSQL.append("'" + kayaModel.getName() + "',");
//
//				if(maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) == null) {
//					insertSQL.append("'',");
//				} else {
//					insertSQL.append("'" + maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "',");
//				}
//
//				// kindtype
//				insertSQL.append("'" + kayaModel.getMetaModelType() + "',");
//				// securitycode
//				// flowcode
//				// flowsubcode
//				insertSQL.append("'',");
//				insertSQL.append("'',");
//				insertSQL.append("'',");
//				// startdate
//				// enddate
//				// withdrawaldate
//				insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
//				insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
//				insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
//				// parentid
//				insertSQL.append("'" + kayaModelId + "',");
//				// createdate
//				// createuser
//				//insertSQL.append("'" + (new Timestamp(System.currentTimeMillis())) + "',");
//				insertSQL.append("{ts '" + (new Timestamp(System.currentTimeMillis())) + "'},");
//				insertSQL.append("'chenliang',");
//				// updatedate
//				//				insertSQL.append("'',");
//				// updateuser
//				// updatemachine
//				//				insertSQL.append("null" + ",");
//				insertSQL.append("'" + "')");
//			}
//		}
//
//	}
//
//	private void getUpdateSqlString(Propertys maping,String kayaModelId,List<String> sqlStringList){
//
//		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
//		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
//		StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET kindvalue = (CASE kind");
//		StringBuilder updateBusinessIdSQL = new StringBuilder("");
//		StringBuilder updateOrientationKeySQL = new StringBuilder("");
//		StringBuilder updateUserInfoSQL = new StringBuilder("");
//
//		// 如果是Role主节点
//		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
//			updateBusinessIdSQL.append(",businessid = (CASE kind");
//		} else {
//			updateBusinessIdSQL.append(",businesssubid = (CASE kind");
//		}
//
//		updateOrientationKeySQL.append(",orientationkey = (CASE kind");
//		updateUserInfoSQL.append(",updatedate = {ts '" + (new Timestamp(System.currentTimeMillis())) + "'}");
//		updateUserInfoSQL.append(",updateuser = '" + "kaya" + "'");
//
//		StringBuilder updateWhere = new StringBuilder(" WHERE kind IN (");
//		StringBuilder updateKeyOnlyWhere = new StringBuilder(" WHERE kind IN (");
//		boolean flg = true;
//		boolean keyOnlyflg = true;
//		NewBusinessKey newBusinessKey = new NewBusinessKey();
//		newBusinessKey.setFlg(false);
//		for (KayaMetaModel kayaModel: kayaModelList) {
//			// 参照的情况下，取参照元的KindKey，设置的时候利用参照本身的KindKey
//			if (Constant.PROPERTYREF.equals(kayaModel.getMetaModelType())){
//				// 只处理更新字段
//				if (maping.getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))) {
//					updateSQL.append(" WHEN '" 
//							+ kayaModel.get(Constant.KINDKEY)
//							+ "' THEN '" + maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "'");
//					if(keyOnlyflg) {
//						updateKeyOnlyWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
//						keyOnlyflg = false;
//					}else {
//						updateKeyOnlyWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
//					}
//
//				}
//				updateBusinessIdSQL.append(" WHEN '"   
//						+ kayaModel.get(Constant.KINDKEY)  
//						+ "' THEN '"+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey) + "'");
//				updateOrientationKeySQL.append(" WHEN '"   
//						+ kayaModel.get(Constant.KINDKEY)  
//						+ "' THEN replace(orientationkey,"+ "'" + KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "'," 
//						+ "'" + getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey)  + "')");
//				if(flg) {
//					updateWhere.append("'"  
//							+ kayaModel.get(Constant.KINDKEY) 
//							+ "'");
//					flg = false;
//				}else {
//					updateWhere.append(",'"  
//							+ kayaModel.get(Constant.KINDKEY) 
//							+ "'");
//				}
//			} else {
//				// 只处理更新字段
//				if (maping.getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))) {
//					updateSQL.append(" WHEN '" 
//							+ kayaModel.get(Constant.KINDKEY) 
//							+ "' THEN '" 
//							+ maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "'");
//					if(keyOnlyflg) {
//						updateKeyOnlyWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
//						keyOnlyflg = false;
//					}else {
//						updateKeyOnlyWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
//					}
//				}
//				updateBusinessIdSQL.append(" WHEN '" 
//						+ kayaModel.get(Constant.KINDKEY) 
//						+ "' THEN '" 
//						+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey) + "'");
//				updateOrientationKeySQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY) 
//						+ "' THEN replace(orientationkey,"+ "'" + KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "'," 
//						+ "'" + getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey)  + "')");
//
//				if(flg) {
//					updateWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
//					flg = false;
//				}else {
//					updateWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
//				}
//			}
//		}
//		updateSQL.append(" ELSE kindvalue END)");
//		// 如果是Role主节点
//		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {			
//			// 主键被更新
//			if (newBusinessKey.isFlg()){
//				updateSQL.append(updateBusinessIdSQL.toString() + " END)");
//				updateSQL.append(updateOrientationKeySQL.toString() + " END)");
//				// 更新日时，更新者
//				updateSQL.append(updateUserInfoSQL.toString());
//				updateSQL.append(updateWhere.append(") AND businessid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
//			} else {
//				// 更新日时，更新者
//				updateSQL.append(updateUserInfoSQL.toString());
//				updateSQL.append(updateKeyOnlyWhere.append(") AND businessid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
//			}
//
//		} else {			
//			if (newBusinessKey.isFlg()){
//				updateSQL.append(updateBusinessIdSQL.toString() + " END)");
//				updateSQL.append(updateOrientationKeySQL.toString() + " END)");
//				// 更新日时，更新者
//				updateSQL.append(updateUserInfoSQL.toString());
//				updateSQL.append(updateWhere.append(") AND businessid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),maping.getPropertys()) 
//						+ "' AND businesssubid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
//			} else {
//				// 更新日时，更新者
//				updateSQL.append(updateUserInfoSQL.toString());
//				updateSQL.append(updateKeyOnlyWhere.append(") AND businessid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),maping.getPropertys()) 
//						+ "' AND businesssubid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
//			}
//		}
//		updateSQL.append(" AND parentid = '" + kayaModelId + "';");
//		// 以多条SQL方式执行操作
//		sqlStringList.add(updateSQL.toString());
//
//		// OrientationKey更新（主键被更新时）
//		if (newBusinessKey.isFlg()) {
//			String _updateOrientationKeySQL = "UPDATE " + tableName + " SET orientationkey = REPLACE(orientationkey,'" 
//					+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "','"
//					+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey)
//					+ "'),businessid = (CASE businessid WHEN '" + KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) 
//					+ "' THEN '" + getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey) + "' ELSE businessid END)"
//					+ updateUserInfoSQL.toString()
//					+ " WHERE orientationkey LIKE '" + KayaModelUtils.getOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "%'";
//			System.out.println(_updateOrientationKeySQL);
//			sqlStringList.add(_updateOrientationKeySQL);
//		}
//	}
//
//
//	private void getUpdateSqlString(Propertys maping,String kayaModelId,List<String> sqlStringList,List<String> insertFieldList){
//
//		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);
//		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
//		StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET kindvalue = (CASE kind");
//		StringBuilder updateBusinessIdSQL = new StringBuilder("");
//		StringBuilder updateOrientationKeySQL = new StringBuilder("");
//		StringBuilder updateUserInfoSQL = new StringBuilder("");
//
//		// 如果是Role主节点
//		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
//			updateBusinessIdSQL.append(",businessid = (CASE kind");
//		} else {
//			updateBusinessIdSQL.append(",businesssubid = (CASE kind");
//		}
//
//		updateOrientationKeySQL.append(",orientationkey = (CASE kind");
//		updateUserInfoSQL.append(",updatedate = {ts '" + (new Timestamp(System.currentTimeMillis())) + "'}");
//		updateUserInfoSQL.append(",updateuser = '" + "kaya" + "'");
//
//		StringBuilder updateWhere = new StringBuilder(" WHERE kind IN (");
//		StringBuilder updateKeyOnlyWhere = new StringBuilder(" WHERE kind IN (");
//		boolean flg = true;
//		boolean keyOnlyflg = true;
//		NewBusinessKey newBusinessKey = new NewBusinessKey();
//		newBusinessKey.setFlg(false);
//		for (KayaMetaModel kayaModel: kayaModelList) {
//			if (!insertFieldList.contains(kayaModel.get(Constant.KINDKEY) + "'")) {
//
//
//
//				// 参照的情况下，取参照元的KindKey，设置的时候利用参照本身的KindKey
//				if (Constant.PROPERTYREF.equals(kayaModel.getMetaModelType())){
//
//					// 只处理更新字段
//					if (maping.getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))) {
//						updateSQL.append(" WHEN '" 
//								+ kayaModel.get(Constant.KINDKEY)
//								+ "' THEN '" + maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "'");
//						if(keyOnlyflg) {
//							updateKeyOnlyWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
//							keyOnlyflg = false;
//						}else {
//							updateKeyOnlyWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
//						}
//
//					}
//					updateBusinessIdSQL.append(" WHEN '"   
//							+ kayaModel.get(Constant.KINDKEY)  
//							+ "' THEN '"+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey) + "'");
//					updateOrientationKeySQL.append(" WHEN '"   
//							+ kayaModel.get(Constant.KINDKEY)  
//							+ "' THEN replace(orientationkey,"+ "'" + KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "'," 
//							+ "'" + getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey)  + "')");
//					if(flg) {
//						updateWhere.append("'"  
//								+ kayaModel.get(Constant.KINDKEY) 
//								+ "'");
//						flg = false;
//					}else {
//						updateWhere.append(",'"  
//								+ kayaModel.get(Constant.KINDKEY) 
//								+ "'");
//					}
//				} else {
//					// 只处理更新字段
//					if (maping.getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))) {
//						updateSQL.append(" WHEN '" 
//								+ kayaModel.get(Constant.KINDKEY) 
//								+ "' THEN '" 
//								+ maping.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "'");
//						if(keyOnlyflg) {
//							updateKeyOnlyWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
//							keyOnlyflg = false;
//						}else {
//							updateKeyOnlyWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
//						}
//					}
//					updateBusinessIdSQL.append(" WHEN '" 
//							+ kayaModel.get(Constant.KINDKEY) 
//							+ "' THEN '" 
//							+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey) + "'");
//					updateOrientationKeySQL.append(" WHEN '" + kayaModel.get(Constant.KINDKEY) 
//							+ "' THEN replace(orientationkey,"+ "'" + KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "'," 
//							+ "'" + getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey)  + "')");
//
//					if(flg) {
//						updateWhere.append("'" + kayaModel.get(Constant.KINDKEY) + "'");
//						flg = false;
//					}else {
//						updateWhere.append(",'" + kayaModel.get(Constant.KINDKEY) + "'");
//					}
//				}
//			}
//		}
//		updateSQL.append(" ELSE kindvalue END)");
//		// 如果是Role主节点
//		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {			
//			// 主键被更新
//			if (newBusinessKey.isFlg()){
//				updateSQL.append(updateBusinessIdSQL.toString() + " END)");
//				updateSQL.append(updateOrientationKeySQL.toString() + " END)");
//				// 更新日时，更新者
//				updateSQL.append(updateUserInfoSQL.toString());
//				updateSQL.append(updateWhere.append(") AND businessid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
//			} else {
//				// 更新日时，更新者
//				updateSQL.append(updateUserInfoSQL.toString());
//				updateSQL.append(updateKeyOnlyWhere.append(") AND businessid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
//			}
//
//		} else {			
//			if (newBusinessKey.isFlg()){
//				updateSQL.append(updateBusinessIdSQL.toString() + " END)");
//				updateSQL.append(updateOrientationKeySQL.toString() + " END)");
//				// 更新日时，更新者
//				updateSQL.append(updateUserInfoSQL.toString());
//				updateSQL.append(updateWhere.append(") AND businessid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),maping.getPropertys()) 
//						+ "' AND businesssubid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
//			} else {
//				// 更新日时，更新者
//				updateSQL.append(updateUserInfoSQL.toString());
//				updateSQL.append(updateKeyOnlyWhere.append(") AND businessid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getParentKayaModel(kayaModelId),maping.getPropertys()) 
//						+ "' AND businesssubid = '" 
//						+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys())) + "'");
//			}
//		}
//		updateSQL.append(" AND parentid = '" + kayaModelId + "';");
//		// 以多条SQL方式执行操作
//		sqlStringList.add(updateSQL.toString());
//
//		// OrientationKey更新（主键被更新时）
//		if (newBusinessKey.isFlg()) {
//			String _updateOrientationKeySQL = "UPDATE " + tableName + " SET orientationkey = REPLACE(orientationkey,'" 
//					+ KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "','"
//					+ getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey)
//					+ "'),businessid = (CASE businessid WHEN '" + KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) 
//					+ "' THEN '" + getNewBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),maping,newBusinessKey) + "' ELSE businessid END)"
//					+ updateUserInfoSQL.toString()
//					+ " WHERE orientationkey LIKE '" + KayaModelUtils.getOrientationKey(AccessKayaModel.getKayaModelId(kayaModelId),maping.getPropertys()) + "%'";
//			System.out.println(_updateOrientationKeySQL);
//			sqlStringList.add(_updateOrientationKeySQL);
//		}
//	}
//}
