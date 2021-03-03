/*
 * Created on 2017/12
 * KaYa DB connection类
 * 1.二进制模型文件解析：主要用于Window系统和开发模式下
 * 2.XML格式模型文件解析：主要用于Unix，Linux ,MacOS 等系统下。
 * 以此来满足跨平台多系统的开发需求
 * @author LiangChen  
 * https://github.com/chljapan
 */
package com.smartkaya.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.smartkaya.api.utils.StringUtil;
import com.smartkaya.constant.Constant;
import com.smartkaya.entity.KayaEntity;
import com.smartkaya.exception.KayaException;
import com.smartkaya.log.KayaLogManager;
import com.smartkaya.model.KayaModelMasterItem;

/**
 * KaYa
 * 
 * @author LiangChen 2018/4/30
 * @version 1.0.0
 */
public class DbConnection implements Pool {
	private static KayaLogManager kayaLoger = KayaLogManager.getInstance();

	private static Hashtable<String, ConnectionPool> pools = new Hashtable<String, ConnectionPool>();
	private static Hashtable<String,DbConnection> dbConnectionInstances =  new Hashtable<String, DbConnection>();

	/**
	 * 此处可以设置成 读取 配置文件的方式
	 */
	private ConnectionPool connPool = null;

	public static DbConnection getInstance(String poolName) {
		String dbType = ParseKayaModel_XPATH.DBConnectionInfoMap.get(Constant.DBTYPE);
		String driver = ParseKayaModel_XPATH.DBConnectionInfoMap.get(Constant.DRIVER);

		String dbpropertys = ParseKayaModel_XPATH.DBConnectionInfoMap.get(Constant.DBPROPERTYS);
		String user = ParseKayaModel_XPATH.DBConnectionInfoMap.get(Constant.USER);
		String password = ParseKayaModel_XPATH.DBConnectionInfoMap.get(Constant.PWD);
		String url = "";

		switch (dbType) {
		case Constant.MYSQL:
			url = "jdbc:mysql://" + poolName + "?" + dbpropertys;
			break;
		case Constant.POSTGRESQL:
			url = "jdbc:postgresql://"  + poolName + "?" + dbpropertys;
			break;
		case Constant.H2:
			url = "jdbc:h2:tcp://" + poolName;
			break;
		case Constant.DB2:
			// TODO: DB2有四种驱动方式，这里默认（目前IBM一直都没有提供 TYPE 1的JDBC驱动程序.）

			//			dbType = "COM.ibm.db2.jdbc.app.DB2Driver";
			//			String url = "jdbc:db2:sample";
			//			Connection con = DriverManager.getConnection(url, user, password);

			//			dbType = "COM.ibm.db2.jdbc.net.DB2Driver";
			//			String url = "jdbc:db2://host:6789:SAMPLE";
			//			Connection con = DriverManager.getConnection(url, user, password);

			//dbType = "com.ibm.db2.jcc.DB2Driver";
			//			String url = "jdbc:db2://host:50000/SAMPLE";
			//			Connection con = DriverManager.getConnection(url, user, password); 
			//break;
		case Constant.ORACLE:
			//dbType = "oracle.jdbc.OracleDriver";
			//			String URL = "jdbc:oracle:thin:@192.168.0.X:1521:xe";
			//			 connection = DriverManager.getConnection(URL, USERNAMR, PASSWORD);
			//break;
		default :
			kayaLoger.error("This type of database is not supported!\n");
			break;
		}

		if (dbConnectionInstances.get(poolName) == null) {
			try {
				if (pools.get(poolName) == null) {
					ConnectionPool _connPool = new ConnectionPool(driver, url, user, password);
					_connPool.createPool();
					pools.put(poolName, _connPool);
				}

			} catch (Exception e) {
				kayaLoger.error(e);
			}

			dbConnectionInstances.put(poolName, new DbConnection(poolName));
		}
		return dbConnectionInstances.get(poolName);
	}

	private DbConnection(String _poolname) {
		connPool = pools.get(_poolname);
	}

	/**
	 * 
	 * @param connection
	 * @param commitFlg
	 */
	public void execute(String sqlString) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = connPool.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.execute(sqlString);
			connection.commit();

		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
				kayaLoger.error(e);
		} finally {
			try {
				statement.close();
				connPool.returnConnection(connection);
			} catch (SQLException e) {
				kayaLoger.error(e);
			}
		}
	}

	/**
	 * 
	 * @param connection
	 * @param commitFlg
	 */
	public int executeUpdate(String sqlString) {
		Connection connection = null;
		Statement statement = null;
		int count = 0;
		try {
			connection = connPool.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			count = statement.executeUpdate(sqlString);
			connection.commit();
			return count;
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			kayaLoger.error(e);
			return count;
		} finally {
			try {
				statement.close();
				connPool.returnConnection(connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param connection
	 * @param commitFlg
	 */
	public List<HashMap<String, Object>> executeQuery(String sqlString, Set<String> orientationKeySet) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<KayaEntity> kayaEntityList = new ArrayList<KayaEntity>();
		List<HashMap<String, Object>> kayaEntityMapList = new ArrayList<HashMap<String, Object>>();
		try {
			connection = connPool.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);
			kayaEntityMapList = editKayaEntity(resultSet, kayaEntityList, orientationKeySet);
			return kayaEntityMapList;
		} catch (Exception e) {
			kayaLoger.error(e);
			return kayaEntityMapList;
		} finally {
			try {
				statement.close();
				connPool.returnConnection(connection);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param connection
	 * @param commitFlg
	 */
	public List<HashMap<String, Object>> executeOrientationsQuery(String sqlString, Set<String> orientationKeySet) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<KayaEntity> kayaEntityList = new ArrayList<KayaEntity>();
		List<HashMap<String, Object>> kayaEntityMapList = new ArrayList<HashMap<String, Object>>();
		try {
			connection = connPool.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);
			kayaEntityMapList = editKayaOrientationsEntity(resultSet, kayaEntityList, orientationKeySet);
			return kayaEntityMapList;
		} catch (Exception e) {
			kayaLoger.error(e);
			return kayaEntityMapList;
		} finally {
			try {
				statement.close();
				connPool.returnConnection(connection);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param connection
	 * @param commitFlg
	 */
	public Map<String, Object> executeQueryIncludeSubinfo(String sqlString, Set<String> orientationKeySet) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<KayaEntity> kayaEntityList = new ArrayList<KayaEntity>();
		Map<String, Object> kayaEntityMapList = new HashMap<String, Object>();
		try {
			connection = connPool.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);
			kayaEntityMapList = editKayaEntityIncludeSubinfo(resultSet, kayaEntityList, orientationKeySet);
			return kayaEntityMapList;
		} catch (Exception e) {
			kayaLoger.error(e);
			return kayaEntityMapList;
		}finally {
			try {
				statement.close();
				connPool.returnConnection(connection);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param sqlStringList
	 * @return 改变的项目数量（并非行数）
	 */
	public int executeBatch(List<String> sqlStringList) {
		Connection connection = null;
		Statement statement = null;
		int insertRows = 0;
		try {
			connection = connPool.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			for (String strSql : sqlStringList) {
				statement.addBatch(strSql);
			}
			insertRows = statement.executeBatch().length;
			return insertRows;
		} catch (Exception e) {
			kayaLoger.error(e);
			return insertRows;
		} finally {
			try {
				statement.clearBatch();
				connection.commit();
				statement.close();
				connPool.returnConnection(connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}

	public void setAutoComit(Connection connection, boolean commitFlg) {
		try {
			connection.setAutoCommit(commitFlg);
		} catch (SQLException e) {
			kayaLoger.error(e);
		}
	}

	public void commit(Connection connection) {
		try {
			connection.commit();
			connPool.returnConnection(connection);
		} catch (SQLException e) {
			kayaLoger.error(e);
		}
	}

	public void closs(Connection connection) {
		if (connection != null) {
			try {
				connPool.returnConnection(connection);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void rollback(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
				connPool.returnConnection(connection);
			} catch (Exception e) {
				kayaLoger.error(e);
			}
		}
	}

	@Override
	public int getInitialConnections() {

		return connPool.getInitialConnections();
	}

	@Override
	public void setInitialConnections(int initialConnections) {

		connPool.setInitialConnections(initialConnections);
	}

	@Override
	public int getIncrementalConnections() {

		return connPool.getIncrementalConnections();
	}

	@Override
	public void setIncrementalConnections(int incrementalConnections) {

		connPool.setIncrementalConnections(incrementalConnections);
	}

	@Override
	public int getMaxConnections() {

		return connPool.getMaxConnections();
	}

	@Override
	public void setMaxConnections(int maxConnections) {

		connPool.setMaxConnections(maxConnections);
	}

	@Override
	public void initPool() {
		try {
			connPool.createPool();
		} catch (Exception e) {
			kayaLoger.error(e);
		}
	}

	@Override
	public Connection getConnection() {

		Connection conn = null;
		try {
			conn = connPool.getConnection();
		} catch (SQLException e) {
			kayaLoger.error(e);
		}
		return conn;
	}

	@Override
	public void returnConnection(Connection conn) {
		connPool.returnConnection(conn);
	}

	@Override
	public void refreshConnections() {

		try {
			connPool.refreshConnections();
		} catch (SQLException e) {
			kayaLoger.error(e);
		}
	}

	@Override
	public void closeConnectionPool() {
		try {
			connPool.closeConnectionPool();
		} catch (SQLException e) {
			kayaLoger.error(e);
		}
	}

	private Map<String, Object> editKayaEntityIncludeSubinfo(ResultSet resultSet, List<KayaEntity> kayaEntityList,
			Set<String> orientationKeySet) {
		Map<String, Object> returnIdMap = new HashMap<String, Object>();
		try {

			Map<String, Object> tempSubMap = new HashMap<String, Object>();
			List<Map<String, Object>> tempSubList = new ArrayList<>();
			Set<String> parentIdSet = new HashSet<String>();
			Set<String> orientationSet = new HashSet<String>();
			while (resultSet.next()) {

				// 如果parentId不存在
				if (parentIdSet.add(resultSet.getString(Constant.PARENTID))) {
					tempSubList = new ArrayList<>();
					// 保存OrientationKey
					orientationSet
							.add(resultSet.getString(Constant.PARENTID) + resultSet.getString(Constant.ORIENTATIONKEY));
					tempSubMap = new HashMap<String, Object>();
					tempSubMap.put(Constant.BUSINESSID, resultSet.getString(Constant.BUSINESSID));
					tempSubMap.put(Constant.ORIENTATIONKEY, resultSet.getString(Constant.ORIENTATIONKEY));
					tempSubList.add(tempSubMap);
					returnIdMap.put(resultSet.getString(Constant.PARENTID), tempSubList);
				}

				// 如果OrientationKey不存在
				if (orientationSet
						.add(resultSet.getString(Constant.PARENTID) + resultSet.getString(Constant.ORIENTATIONKEY))) {
					tempSubMap = new HashMap<String, Object>();
					tempSubMap.put(Constant.BUSINESSID, resultSet.getString(Constant.BUSINESSID));
					tempSubMap.put(Constant.ORIENTATIONKEY, resultSet.getString(Constant.ORIENTATIONKEY));
					tempSubList.add(tempSubMap);
				}

				// 值设定
				String value = resultSet.getString(Constant.KINDVALUE);
				tempSubMap.put(resultSet.getString(Constant.KIND), value);

				// Master时关联的名字也取出来
				if (Constant.MASTER_REFERNCE.equals(resultSet.getString(Constant.KINDTYPE))) {
					List<KayaModelMasterItem> masterItemList =  AccessKayaModel
							.getKayaModelId(AccessKayaModel.getKayaModelId(resultSet.getString(Constant.GMEID))
									.get(Constant.REFERRED)).getMasterItems();

					if (StringUtil.isNotBlank(value)) {
						// MasterRef处理
						for (KayaModelMasterItem master : masterItemList) {
							if (value.equals(master.getId())) {
									tempSubMap.put(resultSet.getString(Constant.KIND) + Constant.NM, master.getText());
									tempSubMap.put(resultSet.getString(Constant.KIND) + Constant.CD_NM,
											value + ":" + master.getText());
									break;
							}
						}
					}
				}
			}

		} catch (Exception e) {
			kayaLoger.error(e);
		}
		return returnIdMap;
	}

	/*
	 * 通用查询结果集处理
	 * @param resultSet
	 * @param kayaEntityList
	 * @param orientationKeySet
	 * @return
	 */
	private List<HashMap<String, Object>> editKayaEntity(ResultSet resultSet, List<KayaEntity> kayaEntityList,
			Set<String> orientationKeySet) {
		List<HashMap<String, Object>> tempMapList = new ArrayList<HashMap<String, Object>>();
		try {

			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			while (resultSet.next()) {
				// 如果OrientationKey不存在
				if (orientationKeySet.add(resultSet.getString(Constant.ORIENTATIONKEY))) {
					tempMap = new HashMap<String, Object>();

					tempMap.put(Constant.ORIENTATIONKEY, resultSet.getString(Constant.ORIENTATIONKEY));

					tempMapList.add(tempMap);
				}

				// 值设定
				String value = resultSet.getString(Constant.KINDVALUE);
				tempMap.put(resultSet.getString(Constant.KIND), value);

				// Master时关联的名字也取出来
				if (Constant.MASTER_REFERNCE.equals(resultSet.getString(Constant.KINDTYPE))) {

					List<KayaModelMasterItem> masterItemList =  AccessKayaModel
							.getKayaModelId(AccessKayaModel.getKayaModelId(resultSet.getString(Constant.GMEID))
									.get(Constant.REFERRED)).getMasterItems();

					if (StringUtil.isNotBlank(value)) {
						// MasterRef处理
						for (KayaModelMasterItem master : masterItemList) {
							if (value.equals(master.getId())) {
									tempMap.put(resultSet.getString(Constant.KIND) + Constant.NM, master.getText());
									tempMap.put(resultSet.getString(Constant.KIND) + Constant.CD_NM,
											value + ":" + master.getText());
									break;
							}
						}
					}
					// 流程状态处理（当前状态和接下来的Pending状态）
				} else if (Constant.G_ROLE.equals(resultSet.getString(Constant.KINDTYPE)) && StringUtils.isNotEmpty(resultSet.getString(Constant.FLOWSUBCODE))) {
					tempMap.put(AccessKayaModel.getKayaModelId(AccessKayaModel.getParentId(resultSet.getString(Constant.FLOWSUBCODE))).get(Constant.KINDKEY), resultSet.getString(Constant.KINDVALUE));
					tempMap.put(AccessKayaModel.getKayaModelId(resultSet.getString(Constant.FLOWCODE)).get(Constant.KINDKEY), "Pending");
				}

				// KayaEntity kayaEntity = new KayaEntity();
				// kayaEntity.setKayaModelId(resultSet.getString("gmeid"));
				// kayaEntity.setName(resultSet.getString("name"));
				// kayaEntity.setKind(resultSet.getString("kind"));
				// kayaEntity.setKindType(resultSet.getString("kindtype"));
				// kayaEntity.setKindValue(resultSet.getString("kindvalue"));
				// kayaEntity.setParentId(resultSet.getString("parentid"));
				// kayaEntity.setBusinessId(resultSet.getString("businessid"));
				// kayaEntity.setBusinessSubId(resultSet.getString("businesssubid"));
				// kayaEntity.setOrientationKey(resultSet.getString("orientationkey"));
				// kayaEntity.setDataType(AccessKayaModel.getKayaModelId(resultSet.getString("gmeid")).get(Constant.DATATYPE));
				// //kayaEntity.setDataLength(Integer.valueOf(AccessKayaModel.getKayaModelId(resultSet.getString("gmeid")).get(Constant.DATALENGTH)));
				// kayaEntity.setStartDate(resultSet.getDate("startdate"));
				// kayaEntity.setEndDate(resultSet.getDate("enddate"));
				// kayaEntity.setWithdrawalDate(resultSet.getDate("withdrawaldate"));
				// kayaEntity.setCreatedate(resultSet.getDate("createdate"));
				// kayaEntity.setCreateuser(resultSet.getString("createuser"));
				// kayaEntity.setUpdatedate(resultSet.getDate("updatedate"));
				// kayaEntity.setUpdateuser(resultSet.getString("updateuser"));
				// kayaEntity.setLockflg(resultSet.getBoolean("lockflg"));
				// kayaEntity.setLockuser(resultSet.getString("lockuser"));
				//
				// kayaEntityList.add(kayaEntity);
				// System.out.println(kayaEntity.toString());
			}

		} catch (Exception e) {
			kayaLoger.error(e);
		}
		return tempMapList;
	}
	
	
	/*
	 * 通用查询结果集处理（支持父子关系表查询结果）
	 * @param resultSet
	 * @param kayaEntityList
	 * @param orientationKeySet
	 * @return
	 */
	private List<HashMap<String, Object>> editKayaOrientationsEntity(ResultSet resultSet, List<KayaEntity> kayaEntityList,
			Set<String> orientationKeySet) {
		List<HashMap<String, Object>> resultMapList = new ArrayList<HashMap<String, Object>>();

		try {
			HashMap<String, Object> tempParentMap = new HashMap<String, Object>();
			HashMap<String, Object> tempChildMap = new HashMap<String, Object>();
			Set<String> parentIdKeySet = new HashSet<String>();
			Set<String> orientationSubKeySet = new HashSet<String>();
			
			
			List<HashMap<String, Object>> childMapList = new ArrayList<HashMap<String, Object>>();
			while (resultSet.next()) {
				// 如果OrientationKey不存在
				// OrientationKey 有两种形式，一个是父类主键
				// 一个是子类主键
				// 
				if (resultSet.getString(Constant.PARENTID).equals(AccessKayaModel.getKayaModelId(resultSet.getString(Constant.PARENTID)).getTableId().replace("_", "-"))
						&& orientationKeySet.add(resultSet.getString(Constant.ORIENTATIONKEY))) {
					// 如果是子 需要王Map里面添加List<Map<Key Value>>
					tempParentMap = new HashMap<String, Object>();
					tempParentMap.put(Constant.ORIENTATIONKEY, resultSet.getString(Constant.ORIENTATIONKEY));
					resultMapList.add(tempParentMap);
					
					setEntity(resultSet,tempParentMap);
					
					// 新子表Row的时候
				} else if (!resultSet.getString(Constant.PARENTID).equals(AccessKayaModel.getKayaModelId(resultSet.getString(Constant.PARENTID)).getTableId().replace("_", "-"))) {
					// 子表发生变化的时候（利用ParentID去判断）
					if (parentIdKeySet.add(resultSet.getString(Constant.PARENTID))) {
						childMapList = new ArrayList<HashMap<String, Object>>();
						tempParentMap.put(AccessKayaModel.getKayaModelId(resultSet.getString(Constant.PARENTID)).get(Constant.KINDKEY),childMapList);
					}
					
					// 子表Orientation Key发生变化的场合
					if (orientationSubKeySet.add(resultSet.getString(Constant.ORIENTATIONKEY))) {
						tempChildMap = new HashMap<String, Object>();
						childMapList.add(tempChildMap);
					}
					setEntity(resultSet,tempChildMap);
				} else {
					setEntity(resultSet,tempParentMap);
				}
			}

		} catch (Exception e) {
			kayaLoger.error(e);
		}
		return resultMapList;
	}
	
	private void setEntity(ResultSet resultSet,HashMap<String, Object> tempMap) {
		// 值设定

		try {
			String value = resultSet.getString(Constant.KINDVALUE);
			tempMap.put(resultSet.getString(Constant.KIND), value);
			// Master时关联的名字也取出来
			if (Constant.MASTER_REFERNCE.equals(resultSet.getString(Constant.KINDTYPE))) {

				List<KayaModelMasterItem> masterItemList =  AccessKayaModel
						.getKayaModelId(AccessKayaModel.getKayaModelId(resultSet.getString(Constant.GMEID))
								.get(Constant.REFERRED)).getMasterItems();

				if (StringUtil.isNotBlank(value)) {
					// MasterRef处理
					for (KayaModelMasterItem master : masterItemList) {
						if (value.equals(master.getId())) {
							tempMap.put(resultSet.getString(Constant.KIND) + Constant.NM, master.getText());
							tempMap.put(resultSet.getString(Constant.KIND) + Constant.CD_NM,
									value + ":" + master.getText());
							break;
						}
					}
				}
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 执行业务流
	 * @param connection
	 * @param commitFlg
	 */
	@SuppressWarnings("finally")
	public List<Map<String, String>> executeQueryForWorkflow(String sqlString, Set<String> orientationKeySet) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<KayaEntity> kayaEntityList = new ArrayList<KayaEntity>();
		List<Map<String, String>> kayaEntityMapList = new ArrayList<Map<String, String>>();
		try {
			kayaEntityList = new ArrayList<KayaEntity>();
			connection = connPool.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);
			kayaEntityMapList = editKayaEntityForWorkflow(resultSet, kayaEntityList, orientationKeySet);
			statement.close();
			connPool.returnConnection(connection);
			return kayaEntityMapList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				statement.close();
				connPool.returnConnection(connection);
			} catch (Exception e1) {
				kayaLoger.error(e1);
			} finally {
				kayaLoger.error(e);
				throw new KayaException(e.toString());
			}
		}
	}
	
	/*
	 * 业务流查询结果编辑（树状数据->关系型数据）
	 * @param resultSet
	 * @param kayaEntityList
	 * @param orientationKeySet
	 * @return
	 */
	private List<Map<String, String>> editKayaEntityForWorkflow(ResultSet resultSet, List<KayaEntity> kayaEntityList,
			Set<String> orientationKeySet) {
		List<Map<String, String>> tempMapList = new ArrayList<Map<String, String>>();
		try {

			Map<String, String> tempMap = new HashMap<String, String>();
			while (resultSet.next()) {
				resultSet.getString("name");
				resultSet.getString("kindtype");
				resultSet.getString("kind");
				resultSet.getString("kindvalue");
				
				// 如果OrientationKey不存在
				if (orientationKeySet.add(resultSet.getString(Constant.ORIENTATIONKEY))) {
					tempMap = new HashMap<String, String>();
					tempMap.put(Constant.ORIENTATIONKEY, resultSet.getString(Constant.ORIENTATIONKEY));					

					tempMapList.add(tempMap);
				}
				if (Constant.G_ROLE.equals(resultSet.getString(Constant.KINDTYPE))) {
					tempMap.put("createuser", resultSet.getString("createuser"));
					tempMap.put("createdate", resultSet.getString("createdate"));
					tempMap.put("flowcode", resultSet.getString("flowcode"));
					tempMap.put(Constant.NAME, resultSet.getString(Constant.NAME));
					tempMap.put(Constant.GMEID, resultSet.getString(Constant.GMEID));
					tempMap.put(Constant.KINDKEY, resultSet.getString(Constant.KIND));
				}
				//目的是该条数据的当前状态对应的action按钮变disabled
				if (Constant.ACTION.equals(resultSet.getString(Constant.KINDTYPE))) {					
					tempMap.put("buttonId", resultSet.getString(Constant.GMEID));
				}

				// 值设定
				String value = resultSet.getString(Constant.KINDVALUE);
				tempMap.put(resultSet.getString(Constant.KIND), value);	
				
				//tempMap.put("name", resultSet.getString("name"));

				// kayaEntity.setName(resultSet.getString("name"));
				// kayaEntity.setKind(resultSet.getString("kind"));
				// kayaEntity.setKindType(resultSet.getString("kindtype"));
				// kayaEntity.setKindValue(resultSet.getString("kindvalue"));
				// kayaEntity.setParentId(resultSet.getString("parentid"));
				// kayaEntity.setBusinessId(resultSet.getString("businessid"));
				// kayaEntity.setBusinessSubId(resultSet.getString("businesssubid"));
				// kayaEntity.setOrientationKey(resultSet.getString("orientationkey"));
				// kayaEntity.setDataType(AccessKayaModel.getKayaModelId(resultSet.getString("gmeid")).get(Constant.DATATYPE));
				// //kayaEntity.setDataLength(Integer.valueOf(AccessKayaModel.getKayaModelId(resultSet.getString("gmeid")).get(Constant.DATALENGTH)));
				// kayaEntity.setStartDate(resultSet.getDate("startdate"));
				// kayaEntity.setEndDate(resultSet.getDate("enddate"));
				// kayaEntity.setWithdrawalDate(resultSet.getDate("withdrawaldate"));
				// kayaEntity.setCreatedate(resultSet.getDate("createdate"));
				// kayaEntity.setCreateuser(resultSet.getString("createuser"));
				// kayaEntity.setUpdatedate(resultSet.getDate("updatedate"));
				// kayaEntity.setUpdateuser(resultSet.getString("updateuser"));
				// kayaEntity.setLockflg(resultSet.getBoolean("lockflg"));
				// kayaEntity.setLockuser(resultSet.getString("lockuser"));
				//
				// kayaEntityList.add(kayaEntity);
				// System.out.println(kayaEntity.toString());
			}

		} catch (Exception e) {
			kayaLoger.error(e);
		}
		return tempMapList;
	}
	
	/*
	 * 业务流详细查询方法
	 * @param connection
	 * @param commitFlg
	 */
	@SuppressWarnings({ "finally"})
	public List<Map<String, String>> executeQueryWorkflowDetail(String sqlString, Set<String> orientationKeySet) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;		
		
		List<Map<String, String>> kayaEntityMapList = new ArrayList<Map<String, String>>();
		try {
			connection = connPool.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);
			kayaEntityMapList = editKayaEntityWorkflowDetail(resultSet, orientationKeySet);
			statement.close();
			connPool.returnConnection(connection);
			return kayaEntityMapList;
		} catch (Exception e) {
			try {
				statement.close();
				connPool.returnConnection(connection);
			} catch (Exception e1) {
				kayaLoger.error(e1);
			} finally {
				kayaLoger.error(e);
				throw new KayaException(e.toString());
			}
		}
	}
	
	/**
	 * workflow数据横向取得
	 * @param connection
	 * @param commitFlg
	 */
	@SuppressWarnings("finally")
	public List<KayaEntity> executeQueryForWorkflowRows(String sqlString) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<KayaEntity> kayaEntityList = new ArrayList<KayaEntity>();
		try {
			kayaEntityList = new ArrayList<KayaEntity>();
			connection = connPool.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);
			kayaEntityList = editKayaEntityForWFRows(resultSet);
			statement.close();
			connPool.returnConnection(connection);
			return kayaEntityList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				statement.close();
				connPool.returnConnection(connection);
			} catch (Exception e1) {
				kayaLoger.error(e1);
			} finally {
				kayaLoger.error(e);
				throw new KayaException(e.toString());
			}
		}
	}
	
	/**
	 * workflow数据横向取得结果
	 * @param connection
	 * @param commitFlg
	 */
	private List<KayaEntity> editKayaEntityForWFRows(ResultSet resultSet) {
		List<KayaEntity> kayaEntityList = new ArrayList<KayaEntity>();
		try {
			
			while (resultSet.next()) {
				KayaEntity kayaEntity = new KayaEntity();
				kayaEntity.setBusinessId(resultSet.getString("businessid"));
				kayaEntity.setName(resultSet.getString("name"));
				kayaEntity.setKind(resultSet.getString("kind"));
				kayaEntity.setKindType(resultSet.getString("kindtype"));
				kayaEntity.setKindValue(resultSet.getString("kindvalue"));
				kayaEntity.setParentId(resultSet.getString("parentid"));
				kayaEntity.setBusinessId(resultSet.getString("businessid"));
				kayaEntity.setBusinessSubId(resultSet.getString("businesssubid"));
				kayaEntity.setOrientationKey(resultSet.getString("orientationkey"));
				kayaEntity.setDataType(
						AccessKayaModel.getKayaModelId(resultSet.getString("gmeid")).get(Constant.DATATYPE));
				kayaEntity.setStartDate(resultSet.getDate("startdate"));
				kayaEntity.setEndDate(resultSet.getDate("enddate"));
				kayaEntity.setWithdrawalDate(resultSet.getDate("withdrawaldate"));
				kayaEntity.setCreatedate(resultSet.getDate("createdate"));
				kayaEntity.setCreateuser(resultSet.getString("createuser"));
				kayaEntity.setUpdatedate(resultSet.getDate("updatedate"));
				kayaEntity.setUpdateuser(resultSet.getString("updateuser"));
				kayaEntity.setLockflg(resultSet.getBoolean("lockflg"));
				kayaEntity.setLockuser(resultSet.getString("lockuser"));
				kayaEntity.setRelid(resultSet.getString("relid"));
				
				kayaEntityList.add(kayaEntity);
			}

		} catch (Exception e) {
			kayaLoger.error(e);
		}
		return kayaEntityList;
	}
	/*
	 * 业务流结果集编辑处理
	 * @param resultSet
	 * @param orientationKeySet
	 * @return
	 */
	
	private List<Map<String, String>> editKayaEntityWorkflowDetail(ResultSet resultSet, Set<String> orientationKeySet) {
		List<Map<String, String>> tempMapList = new ArrayList<Map<String, String>>();
		Set<String> relidSet = new HashSet<String>();
		String pro = null;
		try {

			Map<String, String> tempMap = new HashMap<String, String>();
			while (resultSet.next()) {
				if (relidSet.add(resultSet.getString(Constant.RELID))) {
					tempMap = new HashMap<String, String>();
					pro = "";

					tempMap.put(Constant.RELID, resultSet.getString(Constant.RELID));
					
					tempMap.put(Constant.BUSINESSID, resultSet.getString(Constant.BUSINESSID));
					tempMap.put("businesssubid", resultSet.getString("businesssubid"));

					tempMapList.add(tempMap);
				}

				// 值设定
				tempMap.put(resultSet.getString(Constant.KIND), resultSet.getString(Constant.KINDVALUE));
				tempMap.put(resultSet.getString(Constant.KIND) + Constant.NM, resultSet.getString(Constant.NAME));
				
				if(Constant.ACTION.equals(resultSet.getString(Constant.KINDTYPE))) {
					tempMap.put(Constant.ACTION, resultSet.getString(Constant.NAME));
					tempMap.put("createdate", resultSet.getString("createdate"));
				} else if(Constant.G_PROPERTY.equals(resultSet.getString(Constant.KINDTYPE))) {
					if(StringUtil.isNotEmpty(pro)){
						pro += ",";
					}
					pro += resultSet.getString(Constant.NAME) + " : " + resultSet.getString(Constant.KINDVALUE);
				}
				
				tempMap.put(Constant.G_PROPERTY, pro);
			}

		} catch (Exception e) {
			kayaLoger.error(e);
		}

		return tempMapList;
	}
}
