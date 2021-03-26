/*
 * 
 * Created on 2017/12
 * KaYa 模型解析处理，它包含两种方式
 * 1.二进制模型文件解析：主要用于Window系统和开发模式下
 * 2.XML格式模型文件解析：主要用于Unix，Linux ,MacOS 等系统下。
 * 以此来满足跨平台多系统的开发需求
 * @author LiangChen  
 * https://github.com/chljapan
 */
package com.smartkaya.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.isis.gme.mga.MgaAtom;
import org.isis.gme.mga.MgaAttributes;
import org.isis.gme.mga.MgaConnection;
import org.isis.gme.mga.MgaModel;
import org.isis.gme.mga.MgaObject;
import org.isis.gme.mga.MgaObjects;
import org.isis.gme.mga.MgaProject;
import org.isis.gme.mga.MgaReference;
import org.isis.gme.mga.MgaSimpleConnection;
import org.isis.gme.mga.MgaUtil;
import org.isis.jaut.Apartment;
import org.isis.jaut.Dispatch;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mysql.cj.Constants;
import com.smartkaya.api.utils.StringUtil;
import com.smartkaya.constant.Constant;
import com.smartkaya.log.KayaLogManager;
import com.smartkaya.model.KayaMetaModel;
import com.smartkaya.model.KayaModelMasterItem;
import com.smartkaya.model.KayaModelOrganizationItem;
import com.smartkaya.model.KayaModelPermissionsItem;
import com.smartkaya.utils.UtilTools;

/**
 * KaYa model analysis，Data structure initialization
 * @author LiangChen
 */
public final class ParseKayaModel_XPATH {
	// LogManager
	static KayaLogManager kayaLoger;
	private static String newVersion = ""; 

	// Map <kayaModelId, kayaModelOrientationKey>
	protected Map<String,String> KayaModelIdOrientationKeyMap;

	// Map <kayaModelId, kayaModelParentId>
	protected Map<String,String> KayaModelParentIdMap;

	// Map <srcId, dstId>
	protected Map<String,String> ConnectionMap;

	// Map <srcId, dstId>
	protected Map<String,WorkFlowConnection> WorkFlowConnectionMap;

	// Map <kayaModelId, kayaModelParentId>
	protected Map<String, KayaMetaModel> KayaModelMap;

	// Map <kayaModelId, kayaModelParentId>
	protected List<String> ErrKayaModelList;


	// RootModelId
	protected Map<String,String>  RootFolderAttributesMap;

	// DBConnectionMap
	protected static Map<String,String>  DBConnectionInfoMap;


	// RootFolderId
	protected String RootFolderId;

	// MetamodelDiagramId
	protected String KaYaDiagramId;

	// TableIdList
	protected List<String> KayaModelTables;

	// Roles
	private List<String> KayaRolesList;

	// WorkFlowTables
	protected List<String> WorkFlowTables;

	// WorkFlowItems
	protected Map<String,String> WorkFlowItems;



	// kayaModelTablesMap
	protected Map<String,String>  KayaModelRoleToProductConnectionMap;

	// kayaRoleConnectionsMap
	protected Map<String,String>  KayaRoleConnectionsMap;

	// kayaPropertyToGroupConnectionsMap
	protected Map<String,String> kayaPropertyToGroupConnectionsMap;

	// kayaGroupConnectionsMap
	protected Map<String,String> kayaGroupConnectionsMap;

	// kayaGroupConnectionsMap
	protected Map<String,String> kayaGroupToGroupConnectionsMap;

	// kayaRoleConnectionsMap
	protected Map<String,String>  KayaProductConnectionsMap;
	protected Map<String,String> ProductRefMap;
	private Map<String,String> WorkFlowProductRefMap;

	// LogMap
	protected static Map<String,String>  LogMap;

	//protected DbConnection dbConnection;
	protected static Map<String,DbConnection> dbConnectionInstanceMap = null;

	protected static DbConnection dbConnection = null;
	// Language
	protected String Language;

	private static String mode = "";

	protected String Schema = "";

	private static HashMap<String,ParseKayaModel_XPATH> kayaModelInstanceMap;

	private static String baseModelPath = "";
	private static String baseModelVersion = "";
	protected static String kayaBaseBusiness = "";

	public static final ParseKayaModel_XPATH getInstance(){
		ParseKayaModel_XPATH INSTANCE = null;
		LogMap = new HashMap<String,String>();
		dbConnectionInstanceMap = new  HashMap<String,DbConnection>();

		FileHandler logFileHandler;
		//String kayaModelBasePath = "";
		//String kayaModelVs = "";
		Properties properties = ParseKayaModel_XPATH.getProperties("kayaconfig.properties");
		LogMap.put(Constant.KAYAMETAMODELPARSELOGPATH, properties.getProperty(Constant.KAYAMETAMODELPARSELOGPATH));
		LogMap.put(Constant.KAYAMETAMODELSQLLOGPATH, properties.getProperty(Constant.KAYAMETAMODELSQLLOGPATH));
		LogMap.put(Constant.KAYAMETAMODELCONNECTIONLOGPATH, properties.getProperty(Constant.KAYAMETAMODELCONNECTIONLOGPATH));

		try {
			logFileHandler = new FileHandler(
					properties.getProperty(Constant.KAYAMETAMODELPARSELOGPATH));
			logFileHandler.setFormatter(new SimpleFormatter());
			/*
			 * 自定义方式输出日志
			  	logFileHandler.setFormatter(new Formatter(){
					public String format(LogRecord record) {
			           return record.getLevel() + ":" + record.getMessage() + "\n";
			        }
			    });
			 */

			KayaLogManager.setFileHandler(logFileHandler);

			kayaLoger = KayaLogManager.getInstance();
			baseModelPath = properties.getProperty(Constant.KAYAMODELBASEPATH);
			kayaBaseBusiness = properties.getProperty(Constant.KAYAMODELBASEPATH);

			mode = properties.getProperty(Constant.MODE);
			if (Constant.PRODUCTION.equals(mode)) {
				baseModelVersion = properties.getProperty(Constant.KAYAMODELXMEV);
			} else {
				baseModelVersion = properties.getProperty(Constant.KAYAMODELMGAV);
			}

			kayaModelInstanceMap = new HashMap<String,ParseKayaModel_XPATH>();
			for (String version:baseModelVersion.split(",")) {
				kayaModelInstanceMap.put(version.split(":")[0],new ParseKayaModel_XPATH(version.split(":")[0], baseModelPath + version.split(":")[1]));
				newVersion = version.split(":")[0];
			}

			logFileHandler.close();
			INSTANCE = kayaModelInstanceMap.get(newVersion);

		} catch (SecurityException | IOException e) {
			INSTANCE = null;
			e.printStackTrace();
		}

		return INSTANCE;

	}

	public static final DbConnection getDbConnection(){
		return dbConnectionInstanceMap.get(newVersion);
	}

	// The singleton
	public static final ParseKayaModel_XPATH getInstance(String version){
		return kayaModelInstanceMap.get(version);
	}

	// 更新所有模型信息
	public static final void resetXPATH() {

		LogMap = new HashMap<String,String>();

		FileHandler logFileHandler;
		//String kayaModelBasePath = "";
		//String kayaModelVs = "";
		Properties properties = ParseKayaModel_XPATH.getProperties("kayaconfig.properties");
		LogMap.put(Constant.KAYAMETAMODELPARSELOGPATH, properties.getProperty(Constant.KAYAMETAMODELPARSELOGPATH));
		LogMap.put(Constant.KAYAMETAMODELSQLLOGPATH, properties.getProperty(Constant.KAYAMETAMODELSQLLOGPATH));
		LogMap.put(Constant.KAYAMETAMODELCONNECTIONLOGPATH, properties.getProperty(Constant.KAYAMETAMODELCONNECTIONLOGPATH));

		try {
			logFileHandler = new FileHandler(
					properties.getProperty(Constant.KAYAMETAMODELPARSELOGPATH));
			logFileHandler.setFormatter(new SimpleFormatter());
			/*
			 * 自定义方式输出日志
				  	logFileHandler.setFormatter(new Formatter(){
						public String format(LogRecord record) {
				           return record.getLevel() + ":" + record.getMessage() + "\n";
				        }
				    });
			 */

			KayaLogManager.setFileHandler(logFileHandler);

			kayaLoger = KayaLogManager.getInstance();

			baseModelPath = properties.getProperty(Constant.KAYAMODELBASEPATH);

			kayaBaseBusiness = properties.getProperty(Constant.KAYAMODELBASEPATH);

			mode = properties.getProperty(Constant.MODE);
			if (Constant.PRODUCTION.equals(mode)) {
				baseModelVersion = properties.getProperty(Constant.KAYAMODELXMEV);
			} else {
				baseModelVersion = properties.getProperty(Constant.KAYAMODELMGAV);
			}

			kayaModelInstanceMap = new HashMap<String,ParseKayaModel_XPATH>();
			for (String version:baseModelVersion.split(",")) {
				kayaModelInstanceMap.put(version.split(":")[0],new ParseKayaModel_XPATH(version.split(":")[0], baseModelPath + version.split(":")[1]));
			}

			logFileHandler.close();

		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	// 更新指定版本模型
	public static final void resetXPATH(String version) {
		kayaModelInstanceMap.put(version, null);

		for (String _version:baseModelVersion.split(",")) {
			if (_version.split(":")[0].equals(version)) {
				kayaModelInstanceMap.put(version, new ParseKayaModel_XPATH(version, baseModelPath + _version.split(":")[1]));
			}

		}
	}

	private ParseKayaModel_XPATH(String version,String filePath) {
		KayaRolesList = new ArrayList<String>();
		KayaModelIdOrientationKeyMap = new HashMap<String, String>();
		KayaModelMap = new HashMap<String,KayaMetaModel>();
		RootFolderAttributesMap = new HashMap<String,String>();
		ConnectionMap = new HashMap<String,String>();
		KayaModelTables = new ArrayList<String>();
		WorkFlowTables = new ArrayList<String>();
		KayaModelRoleToProductConnectionMap = new HashMap<String,String>();
		KayaRoleConnectionsMap = new HashMap<String,String>();
		kayaPropertyToGroupConnectionsMap = new HashMap<String,String>();
		kayaGroupConnectionsMap = new HashMap<String,String>();

		kayaGroupToGroupConnectionsMap = new HashMap<String,String>();
		DBConnectionInfoMap = new HashMap<String,String>();
		KayaModelParentIdMap = new HashMap<String,String>(); 
		KayaProductConnectionsMap = new HashMap<String,String>(); 
		WorkFlowConnectionMap = new HashMap<String,WorkFlowConnection>();
		ProductRefMap = new HashMap<String,String>();
		WorkFlowProductRefMap = new HashMap<String,String>();
		WorkFlowItems = new HashMap<String,String>();


		DocumentBuilderFactory dbFactory 
		= DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document document;
		try {
			dBuilder = dbFactory.newDocumentBuilder();


			// LogConfig

			//
			KayaMetaModel projectKayaMetaModel = new KayaMetaModel();
			Map<String,Object> folderAttributesMap = new HashMap<String,Object>();
			projectKayaMetaModel.setAttributesMap(folderAttributesMap);

			kayaLoger.info("*********************Model Parse Start***********************\n");


			// Production mode
			if (Constant.PRODUCTION.equals(mode)) {
				dBuilder.setEntityResolver(new EntityResolver(){
					@Override
					public InputSource resolveEntity(String publicId, String systemId)
							throws SAXException, IOException {
						//return new InputSource(Constant.DTD);
						//						 if (System.getProperty("os.name").toLowerCase().startsWith("win"))
						//			            {
						//			                System.out.println("Win");
						//			            }
						//			            else
						//			            {
						//			            	System.out.println("Ohter");
						//			            }
						InputSource inputSource = null;
						//						String url = ParseKayaModel_XPATH.class.getProtectionDomain().getCodeSource().getLocation().getPath();
						//						if (url.lastIndexOf(".jar")!=-1) {
						//							InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources/mga.dtd");
						//				            inputSource = new InputSource(inputStream);
						//				            inputSource.setSystemId(systemId);
						//				            inputSource.setEncoding("UTF-8");
						//						} else {
						//						    inputSource = new InputSource(Constant.DTD);
						//						}
						inputSource = new InputSource(Constant.DTD);
						return inputSource;
					}
				});

				document = dBuilder.parse(new FileInputStream(filePath));
				Element kayaModelElement = document.getDocumentElement();
				// ParseKayaModel
				ParseKayaModel_ProductRef(kayaModelElement);
				ParseKayaModel_Connection(kayaModelElement);

				// Folder
				NodeList folderNodeList = kayaModelElement.getElementsByTagName(Constant.FOLDER);
				Element folderElement = (Element) folderNodeList.item(0);
				RootFolderId = folderElement.getAttribute(Constant.ID);
				projectKayaMetaModel.setGmeId(folderElement.getAttribute(Constant.ID));
				projectKayaMetaModel.setMetaModelType(folderElement.getAttribute(Constant.KIND));
				projectKayaMetaModel.setName(((Element)folderNodeList.item(0)).getElementsByTagName(Constant.NAME).item(0).getTextContent());
				projectKayaMetaModel.setParentId(Constant.EMPTY);
				projectKayaMetaModel.setOrientationKey(folderElement.getAttribute(Constant.ID) + ".");
				KayaModelIdOrientationKeyMap.put(folderElement.getAttribute(Constant.ID), folderElement.getAttribute(Constant.ID) + ".");
				RootFolderAttributesMap.put(Constant.VERSION, kayaModelElement.getAttribute(Constant.VERSION));
				RootFolderAttributesMap.put(Constant.AUTHOR, kayaModelElement.getElementsByTagName(Constant.AUTHOR).item(0).getTextContent());
				RootFolderAttributesMap.put(Constant.COMMENT, kayaModelElement.getElementsByTagName(Constant.COMMENT).item(0).getTextContent());

				KayaModelMap.put(RootFolderId, projectKayaMetaModel);
				ConnectionMap.put(folderElement.getAttribute(Constant.ID), Constant.EMPTY);
				kayaLoger.info(projectKayaMetaModel.toString() + "\n");
				// 模型信息解析
				Node mainNode = folderNodeList.item(0);

				if (mainNode.getNodeType() == Node.ELEMENT_NODE){
					Element mainElement = (Element) mainNode;
					for (int i=0;i<mainElement.getChildNodes().getLength();i++){
						Node mainNode1 = mainElement.getChildNodes().item(i);
						if (mainNode1.getNodeType() == Node.ELEMENT_NODE){
							Element eElement = (Element) mainNode1;
							if (Constant.MODEL.equals(eElement.getNodeName())) {	

								if(Constant.KAYADIAGRAM.equals(eElement.getAttribute(Constant.KIND))){
									KaYaDiagramId = eElement.getAttribute(Constant.ID);
									// Get root node
									for (int l=0;l<eElement.getChildNodes().getLength();l++){
										Node productNode = eElement.getChildNodes().item(l);
										if (productNode.getNodeType() == Node.ELEMENT_NODE){
											Element productElement = (Element) productNode;
											switch (productElement.getNodeName()) {
											case Constant.ATTRIBUTE:
												DBConnectionInfoMap.put(productElement.getAttribute(Constant.KIND),
														productElement.getTextContent());
												Language = DBConnectionInfoMap.get(Constant.LANGUAGE);
												break;
											default:
												break;
											}
										}
									}
								}
							}
						}
					}
					Schema = DBConnectionInfoMap.get(Constant.SCHEMA);


					KayaMetaModel kayaMetaModel = new KayaMetaModel();
					parseXme(mainElement,kayaMetaModel);
				}
				// Developer mode
			} else if(Constant.DEVELOPER.equals(mode)){
				Apartment.enter(true);
				MgaProject project = MgaProject.createInstance();
				project.open(Constant.MGA + filePath.replace("/", "\\"));

				project.beginTransaction(null);
				// kayaMetaModel

				MgaObject mgaObject = new MgaObject(project.getRootFolder());
				RootFolderId = mgaObject.getID();
				projectKayaMetaModel.setGmeId(mgaObject.getID());
				projectKayaMetaModel.setMetaModelType(mgaObject.getMetaBase().getName());
				projectKayaMetaModel.setName(mgaObject.getName());
				projectKayaMetaModel.setParentId(Constant.EMPTY);
				projectKayaMetaModel.setTableId(Constant.EMPTY);
				projectKayaMetaModel.setOrientationKey(mgaObject.getID() + ".");
				KayaModelIdOrientationKeyMap.put(mgaObject.getID(), mgaObject.getID() + ".");
				// KayaModel Info
				RootFolderAttributesMap.put(Constant.AUTHOR, project.get(Constant.AUTHOR).toString());
				RootFolderAttributesMap.put(Constant.VERSION, project.get(Constant.VERSION).toString());
				RootFolderAttributesMap.put(Constant.COMMENT, project.get(Constant.COMMENT).toString());
				// Attributes
				projectKayaMetaModel.setAttributesMap(folderAttributesMap);
				KayaModelMap.put(RootFolderId, projectKayaMetaModel);

				// 环境变量取得
				MgaObjects children = project.getRootFolder().getChildObjects();
				for (int i=0;i <children.getCount(); i++) {
					// get All Connection
					MgaModel mgaModel = new MgaModel((Dispatch)children.getItem(i));
					// KaYaDiagram解析
					if (Constant.KAYADIAGRAM.equals(mgaModel.getMetaBase().getName())) {
						KaYaDiagramId = mgaModel.getID();
						// DBConnection info
						// Attributes
						MgaAttributes modelMgaAttributes = new MgaAttributes(((Dispatch)mgaModel.get(Constant.ATTRIBUTES)));
						for (int j=0; j<modelMgaAttributes.getCount(); j++) {
							DBConnectionInfoMap.put(modelMgaAttributes.getItem(j).getMeta().getName(), modelMgaAttributes.getItem(j).getValue().toString());
							Language = DBConnectionInfoMap.get(Constant.LANGUAGE);
						}
					}
				}
				Schema = DBConnectionInfoMap.get(Constant.SCHEMA);

				ConnectionMap.put(mgaObject.getID(), Constant.EMPTY);
				parseMga(new MgaObject(project.getRootFolder()),projectKayaMetaModel);
				project.close();
				// 移除线程
				Apartment.leave();
			} else {
				kayaLoger.error("Confirm the mode in the Config file");
			}
			kayaLoger.info("*********************Model Parse End***********************\n");

			/// UnModifiable处理

			Collections.unmodifiableMap(KayaModelIdOrientationKeyMap);
			Collections.unmodifiableMap(KayaModelParentIdMap);
			Collections.unmodifiableMap(ConnectionMap);
			Collections.unmodifiableMap(WorkFlowConnectionMap);
			Collections.unmodifiableMap(KayaModelMap);
			Collections.unmodifiableMap(RootFolderAttributesMap);
			Collections.unmodifiableMap(DBConnectionInfoMap);
			Collections.unmodifiableList(KayaModelTables);
			Collections.unmodifiableList(WorkFlowTables);
			Collections.unmodifiableMap(KayaModelRoleToProductConnectionMap);
			Collections.unmodifiableMap(KayaRoleConnectionsMap);
			Collections.unmodifiableMap(kayaPropertyToGroupConnectionsMap);
			Collections.unmodifiableMap(kayaGroupConnectionsMap);
			Collections.unmodifiableMap(kayaGroupToGroupConnectionsMap);
			Collections.unmodifiableMap(KayaProductConnectionsMap);
			Collections.unmodifiableMap(ProductRefMap);
			Collections.unmodifiableMap(WorkFlowProductRefMap);
			Collections.unmodifiableMap(LogMap);

			kayaLoger.info("*********************Table Create Start***********************\n");
			DbConnection  dbConnection = null;
			// 生成基础表
			dbConnection = DbConnection.getInstance(DBConnectionInfoMap.get(Constant.HOST) + "/" + DBConnectionInfoMap.get(Constant.DBNAME));
			dbConnectionInstanceMap.put(version, dbConnection);
			kayaLoger.info(DBConnectionInfoMap.get(Constant.HOST) + "/" + DBConnectionInfoMap.get(Constant.DBNAME));

			//TODO 注：在模型中删除表后（可以视做表合并），元数据还会存在原来的表中（可以提供相应的Batch处理。。。暂时未对应，需要手动迁移数据到新的表中）
			for (String kayaModelId: KayaModelTables) {
				if (Constant.UPDATE.equals(this.KayaModelMap.get(kayaModelId).get(Constant.UPDATE))) {
					dbConnection.execute("DROP TABLE IF EXISTS " + kayaModelId.replace('-','_') + ";");
				}
				//dbConnection.execute("DROP TABLE IF EXISTS " + kayaModelId.replace('-','_') + ";");
				switch (DBConnectionInfoMap.get(Constant.DBTYPE)) {
				case Constant.MYSQL:
					if ("Tree".equals("Tree")) {
						dbConnection.execute(CreateMysqlTreeTables(kayaModelId.replace('-','_')));
					} else {
						getCreateTablesSqlText(kayaModelId.replace('-','_'));
					}
					break;
				case Constant.POSTGRESQL:
					dbConnection.execute(CreatePostegreSqlTreeTables(kayaModelId.replace('-','_')));
					break;
				case Constant.H2:
					dbConnection.execute(CreateH2TreeTables(kayaModelId.replace('-','_')));
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
					break;
				case Constant.ORACLE:
					//dbType = "oracle.jdbc.OracleDriver";
					//			String URL = "jdbc:oracle:thin:@192.168.0.X:1521:xe";
					//			 connection = DriverManager.getConnection(URL, USERNAMR, PASSWORD);
					break;
				default :
					kayaLoger.error("This type of database is not supported!\n");
					break;
				}

			}

			//TODO 注：在模型中删除表后（可以视做表合并），元数据还会存在原来的表中（可以提供相应的Batch处理。。。暂时未对应，需要手动迁移数据到新的表中）
			for (String kayaModelId: WorkFlowTables) {

				switch (DBConnectionInfoMap.get(Constant.DBTYPE)) {
				case Constant.MYSQL:
					//dBConnection.execute("DROP TABLE IF EXISTS " + kayaModelId.replace('-','_') + ";");
					dbConnection.execute(CreateMysqlWorkflowTables(kayaModelId.replace('-','_')));
					break;
				case Constant.POSTGRESQL:
					break;
				case Constant.H2:
					dbConnection.execute(CreateH2WorkflowTables(kayaModelId.replace('-','_')));
					break;
				case Constant.DB2: 
					break;
				case Constant.ORACLE:
					break;
				default :
					kayaLoger.error("This type of database is not supported!\n");
					break;
				}
			}
			kayaLoger.info("*********************Table Create End***********************\n");

			kayaLoger.info("*********************Bean Create Start***********************\n");
			if (KayaRolesList.size() > 0) {
//				class emlInfo {
//					private  HashMap<String,Object> property;
//					emlInfo(HashMap<String,Object> property) {
//						this.property = property;
//					}
//					
//					public String 姓名 = (String) this.property.get("name");
//				}
				KayaRolesList.forEach(item -> {
					// 定义类名称
					System.out.println("// " + KayaModelMap.get(item).getName() + "\n"
							+ "import java.util.HashMap;");
					System.out.println("class " + KayaModelMap.get(item).get(Constant.KINDKEY) + " {");
					System.out.println("	private  HashMap<String,Object> property;");
					System.out.println("	" + KayaModelMap.get(item).get(Constant.KINDKEY) + "(HashMap<String,Object> property) {"
							+ "\n		this.property = property;\n"
							+ "	}");
					for (Map.Entry<String, String> entity : KayaModelParentIdMap.entrySet()) {
						if(entity.getValue().equals(item)){
							switch (KayaModelMap.get(entity.getKey()).getMetaModelType()) { 
							case Constant.G_PROPERTY:
							case Constant.UNIQUEPROPERTY:
								System.out.println("	public " + KayaModelMap.get(entity.getKey()).get(Constant.DATATYPE) + " "
										+ KayaModelMap.get(entity.getKey()).getName() 
										+ " = (" + KayaModelMap.get(entity.getKey()).get(Constant.DATATYPE) + ")this.property.get(\"" 
										+ KayaModelMap.get(entity.getKey()).get(Constant.KINDKEY) + "\");");
								break;
							case Constant.MASTER_REFERNCE:
								System.out.println("	public String "
										+ KayaModelMap.get(entity.getKey()).getName() 
										+ " = (String)this.property.get(\"" 
										+ KayaModelMap.get(entity.getKey()).get(Constant.KINDKEY) + "\");");
								break;
							case Constant.PROPERTYREF:
								System.out.println("	public " + KayaModelMap.get(KayaModelMap.get(entity.getKey()).get(Constant.REFERRED)).get(Constant.DATATYPE) + " "
										+ KayaModelMap.get(entity.getKey()).getName() 
										+ " = (" + KayaModelMap.get(KayaModelMap.get(entity.getKey()).get(Constant.REFERRED)).get(Constant.DATATYPE) + ")this.property.get(\"" 
										+ KayaModelMap.get(entity.getKey()).get(Constant.KINDKEY) + "\");");
								break;
							default:
								break;

							}
						}
					}
					
					System.out.println("}");
				});




			}
			kayaLoger.info("*********************Bean Create End***********************\n");

		} catch (ParserConfigurationException | SAXException | IOException e1) {
			// TODO:
			kayaModelInstanceMap = new HashMap<String,ParseKayaModel_XPATH>();
			e1.printStackTrace();
		}
	}

	/**
	 * ParseMga Binary model resolution processing
	 * @param obj Model
	 * @param orientationKey The associated key
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void parseMga(MgaObject obj, KayaMetaModel kayaMetaModelMga) {

		// Object SelectType
		int type = obj.getObjType();

		switch (type) {
		// Folder
		case MgaObject.OBJTYPE_FOLDER:
			// kayaModel
			kayaLoger.info(kayaMetaModelMga.toString() + "\n");
			KayaModelMap.put(obj.getID(), kayaMetaModelMga);

			// Connection Info (Src,Dst)
			MgaObjects children = obj.getChildObjects();
			MgaSimpleConnection connection;

			for (int i=0;i < children.getCount(); i++) {
				// get All Connection
				MgaModel mgaModel = new MgaModel((Dispatch)children.getItem(i));
				// KaYaDiagram
				if (Constant.KAYADIAGRAM.equals(mgaModel.getMetaBase().getName())) {
					// ProductTOSystem (Empty)
					List<MgaConnection> productToSystemConnectionList = MgaUtil.getChildConnectionList(mgaModel, Constant.PSC);
					// RoleToProduct
					List<MgaConnection> roleToProductConnectionList = MgaUtil.getChildConnectionList(mgaModel, Constant.RPC);

					// MasterToProduct
					List<MgaConnection> masterToProductConnectionList = MgaUtil.getChildConnectionList(mgaModel, Constant.MPC);

					// ProductTOProduct
					List<MgaConnection> productToProductConnectionList = MgaUtil.getChildConnectionList(mgaModel, Constant.PPC);
					// RoleToROle
					List<MgaConnection> roleToRoleConnectionList = MgaUtil.getChildConnectionList(mgaModel, Constant.RRC);
					// catAllList
					productToSystemConnectionList.addAll(roleToProductConnectionList);
					productToSystemConnectionList.addAll(productToProductConnectionList);

					productToSystemConnectionList.addAll(roleToRoleConnectionList);
					productToSystemConnectionList.addAll(masterToProductConnectionList);
					// get src and dst
					for (MgaConnection mgaConnection:productToSystemConnectionList) {
						connection = new MgaSimpleConnection((Dispatch) mgaConnection);
						ConnectionMap.put(connection.getSrc().getID(), connection.getDst().getID());
					}

					for (MgaConnection mgaConnection:productToProductConnectionList) {
						connection = new MgaSimpleConnection((Dispatch) mgaConnection);
						KayaProductConnectionsMap.put(connection.getSrc().getID(), connection.getDst().getID());
					}

					// Master Connection
					for (MgaConnection mgaConnection:masterToProductConnectionList) {
						connection = new MgaSimpleConnection((Dispatch) mgaConnection);
						ConnectionMap.put(connection.getSrc().getID(), connection.getDst().getID());
					}

					// get TableId
					for (MgaConnection mgaConnection:roleToProductConnectionList) {
						connection = new MgaSimpleConnection((Dispatch) mgaConnection);
						KayaModelRoleToProductConnectionMap.put(connection.getSrc().getID(), connection.getDst().getID());
						KayaModelTables.add(connection.getSrc().getID());
					}
					// get TableId
					for (MgaConnection mgaConnection:roleToRoleConnectionList) {
						connection = new MgaSimpleConnection((Dispatch) mgaConnection);
						KayaRoleConnectionsMap.put(connection.getSrc().getID(),connection.getDst().getID());
					}
					// WorkFlowDiagram
				} else if (Constant.KAYAWORKFLOWDIAGRAM.equals(mgaModel.getMetaBase().getName())) {
					// WorkFlow table

					List<MgaReference> productRefList = MgaUtil.getChildReferenceList(mgaModel, Constant.PRODUCTREF);
					//ProductRefMap = new HashMap<String,String>();
					// Product Ref Map
					for (MgaReference mgaReference:productRefList) {
						try {
							ProductRefMap.put(mgaReference.getID(), mgaReference.getReferred().getID());
							ConnectionMap.put(mgaReference.getID(), mgaReference.getParent().getID());
						} catch(Exception e) {
							kayaLoger.error("The Product[" + mgaReference.getID() + "]reference object cannot be empty " + "\n");
						}
					}
					// WorkFlowToProductRef
					List<MgaConnection> workFlowToProductConnectionList = MgaUtil.getChildConnectionList(mgaModel, Constant.WPC);
					// get TableId
					for (MgaConnection mgaConnection:workFlowToProductConnectionList) {
						connection = new MgaSimpleConnection((Dispatch) mgaConnection);
						ConnectionMap.put(connection.getSrc().getID(), connection.getDst().getID());
					}

					// WorkFlow
					MgaObjects workFlowChildren = mgaModel.getChildObjects();
					for (int workFlowCount=0;workFlowCount < workFlowChildren.getCount(); workFlowCount++) {
						// workFlow
						if(MgaObject.OBJTYPE_MODEL==workFlowChildren.getItem(workFlowCount).getObjType()){
							MgaObjects workFlowMgaModel = workFlowChildren.getItem(workFlowCount).getChildObjects();
							for (int workFlowChildrenCount=0;workFlowChildrenCount<workFlowMgaModel.getCount();workFlowChildrenCount++) {
								// Connections
								if(MgaObject.OBJTYPE_CONNECTION==workFlowMgaModel.getItem(workFlowChildrenCount).getObjType()){
									connection = new MgaSimpleConnection((Dispatch) workFlowMgaModel.getItem(workFlowChildrenCount));
									if (Constant.EAC.equals(connection.getName()) || Constant.MAC.equals(connection.getName())) {
										if (WorkFlowConnectionMap.containsKey(connection.getDst().getID())) {
											WorkFlowConnectionMap.get(connection.getDst().getID()).setKayaModelId(connection.getSrc().getID());
										} else {
											ConnectionMap.put(connection.getDst().getID(),"WorkFlowConnectionMap");
											WorkFlowConnection workFlowConnection = new WorkFlowConnection();
											workFlowConnection.setReverse(true);
											workFlowConnection.setKayaModelId(connection.getSrc().getID());
											WorkFlowConnectionMap.put(connection.getDst().getID(), workFlowConnection);
										}
									} else {
										ConnectionMap.put(connection.getSrc().getID(), connection.getDst().getID());
										WorkFlowConnection workFlowConnection = new WorkFlowConnection();
										workFlowConnection.setReverse(false);
										workFlowConnection.setKayaModelId(connection.getDst().getID());
										WorkFlowConnectionMap.put(connection.getSrc().getID(), workFlowConnection);
									}
								}
							}
						}
					}
				}

				parseMga (children.getItem(i),kayaMetaModelMga);
			}

			break;
			// Model
		case MgaObject.OBJTYPE_MODEL:
			Map<String,Object> modelAttributesMap = new HashMap<String,Object>();
			List<String> businessKeys = new ArrayList<String>();


			KayaMetaModel kayaModel = new KayaMetaModel();
			kayaModel.setGmeId(obj.getID());
			kayaModel.setMetaModelType(obj.getMetaBase().getName());
			if (Constant.LANGUAGE_DEFAULT.equals(Language)) {
				kayaModel.setName(obj.getName().trim());
			}
			kayaModel.setBusinessKeys(businessKeys);

			MgaModel mgaModel = new MgaModel((Dispatch)obj);
			// KaYaDiagram
			if (obj.getMetaBase().getName().equals(Constant.G_ROLE))  {

				// PropertyToGroup
				List<MgaConnection> propertyToGroupConnectionList = MgaUtil.getChildConnectionList(mgaModel, Constant.PGC);
				propertyToGroupConnectionList.addAll(MgaUtil.getChildConnectionList(mgaModel, Constant.MGC));
				propertyToGroupConnectionList.addAll(MgaUtil.getChildConnectionList(mgaModel, Constant.PRGC));
				// Group level
				for (MgaConnection mgaConnection:propertyToGroupConnectionList) {
					connection = new MgaSimpleConnection((Dispatch) mgaConnection);
					kayaPropertyToGroupConnectionsMap.put(connection.getSrc().getID(), connection.getDst().getID());
				}

				// Group to Group Connection
				propertyToGroupConnectionList.addAll(MgaUtil.getChildConnectionList(mgaModel, Constant.GGC));
				for (MgaConnection mgaConnection:propertyToGroupConnectionList) {
					connection = new MgaSimpleConnection((Dispatch) mgaConnection);
					kayaGroupConnectionsMap.put(connection.getSrc().getID(), connection.getDst().getID());
				}

				for (MgaConnection mgaConnection:MgaUtil.getChildConnectionList(mgaModel, Constant.GGC)) {
					connection = new MgaSimpleConnection((Dispatch) mgaConnection);
					kayaGroupToGroupConnectionsMap.put(connection.getSrc().getID(), connection.getDst().getID());
				}

				kayaModel.setParentId(ConnectionMap.get(obj.getID()));
				KayaModelParentIdMap.put(obj.getID(), ConnectionMap.get(obj.getID()));
				if (getOrientationKey(ConnectionMap, obj.getID(), obj.getID()).equals(obj.getID() + ".")) {
					kayaLoger.error("(" + obj.getName() + ":" + obj.getID() + ")No superclass link object is specified");
				} else {
					kayaModel.setTableId(getTableId(ConnectionMap,obj.getID()));
				}
				kayaModel.setOrientationKey(getOrientationKey(ConnectionMap, obj.getID(), obj.getID()));
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getID(), obj.getID()));


				KayaRolesList.add(obj.getID());

			} else if (obj.getMetaBase().getName().equals(Constant.G_WORKFLOW)) {
				kayaModel.setParentId(ConnectionMap.get(obj.getID()));
				// WorkFlow table
				kayaModel.setTableId(obj.getID().replace("-", "_"));
				// WorkFlow root node
				WorkFlowTables.add(obj.getID());
				modelAttributesMap.put(Constant.GROUP_ITEMS, new ArrayList<String>());
				KayaModelParentIdMap.put(obj.getID(), obj.getParent().getID());
				kayaModel.setOrientationKey(getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				modelAttributesMap.put(Constant.INDEXNO, "999");
				modelAttributesMap.put(Constant.ROWSPAN, 2);
			} else if (obj.getMetaBase().getName().equals(Constant.USERTASK) || obj.getMetaBase().getName().equals(Constant.E_GateWay)) {
				kayaModel.setParentId(obj.getParent().getID());
				KayaModelParentIdMap.put(obj.getID(), obj.getParent().getID());
				kayaModel.setTableId(KayaModelMap.get(obj.getParent().getID()).getTableId());
				kayaModel.setOrientationKey(KayaModelIdOrientationKeyMap.get(obj.getParent().getID())+ obj.getID() + ".");
				KayaModelIdOrientationKeyMap.put(obj.getID(), KayaModelIdOrientationKeyMap.get(obj.getParent().getID())+ obj.getID() + ".");
				kayaModel.setTableId(obj.getParent().getID().replace("-", "_"));
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				((List<String>) KayaModelMap.get(obj.getParent().getID()).getAttributesMap().get(Constant.GROUP_ITEMS)).add(obj.getID());

			} else if (obj.getMetaBase().getName().equals(Constant.MASTER)) {
				kayaModel.setName(obj.getName().trim());
				kayaModel.setParentId(ConnectionMap.get(obj.getID()));
				KayaModelParentIdMap.put(obj.getID(), obj.getParent().getID());
				kayaModel.setOrientationKey(getOrientationKey(ConnectionMap, obj.getID(), obj.getID()));
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getID(), obj.getID()));
				kayaModel.setTableId(ConnectionMap.get(obj.getID()).replace("-", "_"));
				KayaModelTables.add(obj.getID());
			} else {
				// RootFolederConnection
				ConnectionMap.put(obj.getID(), RootFolderId);
				kayaModel.setParentId(RootFolderId);

				KayaModelParentIdMap.put(obj.getID(), RootFolderId);
				kayaModel.setOrientationKey(getOrientationKey(ConnectionMap, obj.getID(), obj.getID()));
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getID(), obj.getID()));
				// KaYaDiagrem Table
				KayaModelTables.add(obj.getID());
				// KaYaDiagram
				if (obj.getMetaBase().getName().equals(Constant.KAYADIAGRAM) && MgaUtil.getChildAtomList(mgaModel, Constant.G_PRODUCT).size() ==0) {
					kayaLoger.error("Specify the Product object as the system root node identity." + "\n");
					return;
				}  else {
					// KaYaDiagram
					kayaModel.setTableId(RootFolderId.replace("-", "_"));
					for (MgaAtom mgaAtom:MgaUtil.getChildAtomList(mgaModel, Constant.G_PRODUCT)) {
						if (!ConnectionMap.containsKey(mgaAtom.getID()) && ConnectionMap.containsValue(mgaAtom.getID())) {
							ConnectionMap.put(mgaAtom.getID(), obj.getID());
						}
					}
				}
			} 
			// Attributes
			MgaAttributes modelMgaAttributes = new MgaAttributes(((Dispatch)obj.get(Constant.ATTRIBUTES)));
			for (int j=0; j<modelMgaAttributes.getCount(); j++) {
				// Orgnizations
				if (Constant.ORGANIZATION.equals(modelMgaAttributes.getItem(j).getMeta().getName())) {
					List<KayaModelOrganizationItem> organizationItems = new ArrayList<KayaModelOrganizationItem>();
					for (String organizationItem : modelMgaAttributes.getItem(j).getValue().toString().trim().split("\n")) {
						KayaModelOrganizationItem kayaModelOrganizationItem = new KayaModelOrganizationItem();
						List<String> valueList = Arrays.asList(organizationItem.split("="));
						if(valueList.size()>1){
							kayaModelOrganizationItem.setRef(true);
							kayaModelOrganizationItem.setRefSrc(valueList.get(0));
							kayaModelOrganizationItem.setRefDst(valueList.get(1));
							kayaModelOrganizationItem.setText(Constant.EMPTY);
						} else {
							kayaModelOrganizationItem.setRef(false);
							kayaModelOrganizationItem.setRefSrc(Constant.EMPTY);
							kayaModelOrganizationItem.setRefDst(Constant.EMPTY);
							kayaModelOrganizationItem.setText(organizationItem);
						}
						organizationItems.add(kayaModelOrganizationItem);
					}
					modelAttributesMap.put(modelMgaAttributes.getItem(j).getMeta().getName(), organizationItems);
					// Permissions
				} else if (Constant.WF_PERMISSIONS.equals(modelMgaAttributes.getItem(j).getMeta().getName())) {
					List<KayaModelPermissionsItem> permissionItems = new ArrayList<KayaModelPermissionsItem>();
					for (String permissionItem : modelMgaAttributes.getItem(j).getValue().toString().trim().split("\n")) {
						if(StringUtil.isNotEmpty(permissionItem)) {
							KayaModelPermissionsItem perItem = new KayaModelPermissionsItem();
							permissionItem = permissionItem.replace("\"", "");
							String[] perStr = permissionItem.split(":|==", 2);
							if(perStr.length > 1) {
								perItem.setId(perStr[0].toString());
								String perValue = perStr[1].toString();
								if(perValue.startsWith("{") || perValue.startsWith("[") ){
									String newString = perValue.substring(1, perValue.length() - 1);
									List<String> valueList = Arrays.asList(newString.split(","));
									perItem.setText(Constant.EMPTY);
									perItem.setTextList(valueList);
								} else {
									perItem.setText(perValue);
								}
								permissionItems.add(perItem);
							} else {								
								kayaLoger.error("(" + obj.getName() + ":" + obj.getID() + ")Permissions format was invalid. "+ "\n");
								kayaLoger.error("Please use the format: userInfo:{M,GL} " + "\n");
							}
						}
					}
					modelAttributesMap.put(modelMgaAttributes.getItem(j).getMeta().getName(), permissionItems);
					//
				} else if (Constant.WORKFLOWID.equals(modelMgaAttributes.getItem(j).getMeta().getName())) { 
					if (!UtilTools.isEmpty(modelMgaAttributes.getItem(j).getValue())) {
						WorkFlowItems.put(obj.getID(),modelMgaAttributes.getItem(j).getValue().toString().trim());
					}
					modelAttributesMap.put(modelMgaAttributes.getItem(j).getMeta().getName(), modelMgaAttributes.getItem(j).getValue().toString().trim());
				} else if (Constant.MULTILANGUAGE.equals(modelMgaAttributes.getItem(j).getMeta().getName())) {
					for (String language : modelMgaAttributes.getItem(j).getValue().toString().trim().split("\n")) {
						if (language.contains("=") && Language.equals(language.split("=")[0].trim())) {
							kayaModel.setName(language.split("=")[1].trim());
						}
					}
				} else {
					modelAttributesMap.put(modelMgaAttributes.getItem(j).getMeta().getName(), modelMgaAttributes.getItem(j).getValue().toString().trim());
				}

			}

			// RowSpan is 1
			modelAttributesMap.put(Constant.ROWSPAN, 1);
			kayaModel.setAttributesMap(modelAttributesMap);

			// kayaModel
			KayaModelMap.put(obj.getID(), kayaModel);

			MgaObjects model_children = obj.getChildObjects();
			for (int i=0;i < model_children.getCount(); i++) {
				parseMga (model_children.getItem(i),kayaModel);
			}
			kayaLoger.info(kayaModel.toString() + "\n");
			break;

			// Product&Servide   Property&Ref
		case MgaObject.OBJTYPE_ATOM:

			Map<String,Object> atomAttributesMap = new HashMap<String,Object>();
			KayaMetaModel kayaAtom = new KayaMetaModel();
			kayaAtom.setGmeId(obj.getID());
			kayaAtom.setMetaModelType(obj.getMetaBase().getName());

			if (Constant.LANGUAGE_DEFAULT.equals(Language)) {
				kayaAtom.setName(obj.getName().trim());
			}
			// Atom Attributes
			MgaAttributes mgaAttributes = new MgaAttributes(((Dispatch)obj.get(Constant.ATTRIBUTES)));
			for (int j=0; j<mgaAttributes.getCount(); j++) {
				// MasterItem
				if (Constant.FIELD_ATTRIBUTES.equals(mgaAttributes.getItem(j).getMeta().getName())) {
					List<KayaModelMasterItem> MasterItemList = new ArrayList<KayaModelMasterItem>();
					for (String master : mgaAttributes.getItem(j).getValue().toString().trim().split("\n")) {
						KayaModelMasterItem masterItem = new KayaModelMasterItem();
						String[] languageStr = master.trim().split(":", -1);// 当：：中间为空时返回空字符串
						masterItem.setId(languageStr[0].trim());

						if (Constant.LANGUAGE_DEFAULT.equals(Language)) {
							masterItem.setText(languageStr[1].trim());
						} else {
							for (String language : languageStr) {
								if (language.contains("=") && Language.equals(language.split("=")[0].trim())) {
									masterItem.setText(language.split("=")[1].trim());
								}
							}
						}
						MasterItemList.add(masterItem);
					}
					atomAttributesMap.put(mgaAttributes.getItem(j).getMeta().getName(), MasterItemList);
					// 
				} else if (Constant.ORGANIZATION.equals(mgaAttributes.getItem(j).getMeta().getName())) {
					List<KayaModelOrganizationItem> organizationItems = new ArrayList<KayaModelOrganizationItem>();
					for (String organizationItem : mgaAttributes.getItem(j).getValue().toString().trim().split("\n")) {
						KayaModelOrganizationItem kayaModelOrganizationItem = new KayaModelOrganizationItem();
						List<String> valueList = Arrays.asList(organizationItem.split("="));
						if(valueList.size()>1){
							kayaModelOrganizationItem.setRef(true);
							kayaModelOrganizationItem.setRefSrc(valueList.get(0));
							kayaModelOrganizationItem.setRefDst(valueList.get(1));
						} else {
							kayaModelOrganizationItem.setRef(false);
							kayaModelOrganizationItem.setRefSrc(Constant.EMPTY);
							kayaModelOrganizationItem.setRefDst(Constant.EMPTY);
							kayaModelOrganizationItem.setText(organizationItem);
						}
						organizationItems.add(kayaModelOrganizationItem);
					}
					atomAttributesMap.put(mgaAttributes.getItem(j).getMeta().getName(), organizationItems);
					// 
				} else if (Constant.WF_PERMISSIONS.equals(mgaAttributes.getItem(j).getMeta().getName())) {
					List<KayaModelPermissionsItem> permissionItems = new ArrayList<KayaModelPermissionsItem>();
					for (String permissionItem : mgaAttributes.getItem(j).getValue().toString().trim().split("\n")) {
						if(StringUtil.isNotEmpty(permissionItem)) {
							KayaModelPermissionsItem perItem = new KayaModelPermissionsItem();
							permissionItem = permissionItem.replace("\"", "");
							String[] perStr = permissionItem.split(":|==", 2);
							if(perStr.length > 1) {
								perItem.setId(perStr[0].toString());
								String perValue = perStr[1].toString();
								if(perValue.startsWith("{") || perValue.startsWith("[") ){
									String newString = perValue.substring(1, perValue.length() - 1);
									List<String> valueList = Arrays.asList(newString.split(","));
									perItem.setText(Constant.EMPTY);
									perItem.setTextList(valueList);
								}else {
									perItem.setText(perValue);
								}
								permissionItems.add(perItem);
							} else {								
								kayaLoger.error("(" + obj.getName() + ":" + obj.getID() + ")Permissions format was invalid. "+ "\n");
								kayaLoger.error("Please use the format: userInfo:{M,GL} " + "\n");
							}
						}
					}
					atomAttributesMap.put(mgaAttributes.getItem(j).getMeta().getName(), permissionItems);
					//
				} else if (Constant.MULTILANGUAGE.equals(mgaAttributes.getItem(j).getMeta().getName())) {
					for (String language : mgaAttributes.getItem(j).getValue().toString().trim().split("\n")) {
						if (language.contains("=") && Language.equals(language.split("=")[0].trim())) {
							kayaAtom.setName(language.split("=")[1].trim());
						}
					}
				} else {
					atomAttributesMap.put(mgaAttributes.getItem(j).getMeta().getName(), mgaAttributes.getItem(j).getValue().toString().trim());
				}
			}
			kayaAtom.setAttributesMap(atomAttributesMap);
			// Product（产品&服务）
			if (obj.getMetaBase().getName().equals(Constant.G_PRODUCT)) {

				// 没有被链接的Product添加到异常ModelList里面
				if (!ConnectionMap.containsValue(obj.getID())
						&& !ConnectionMap.containsKey(obj.getID())) {
					kayaLoger.error("(" + obj.getName() + ":" + obj.getID() + ")No superclass link object is specified");
					ErrKayaModelList.add(obj.getID());
					// Diagram视图根节点设定（Product指向Diagram）
				} else if (KayaProductConnectionsMap.containsValue(obj.getID())	&& !KayaProductConnectionsMap.containsKey(obj.getID())){
					// 设置
					KayaModelTables.add(obj.getID());
					KayaModelParentIdMap.put(obj.getID(),obj.getParent().getID());
					kayaAtom.setParentId(obj.getParent().getID());
					//ConnectionMap.put(obj.getID(), obj.getParent().getID());

				} else {
					KayaModelTables.add(obj.getID());
					KayaModelParentIdMap.put(obj.getID(), ConnectionMap.get(obj.getID()));
					kayaAtom.setParentId(ConnectionMap.get(obj.getID()));
				}

				kayaAtom.setTableId(obj.getID().replace("-", "_"));
				kayaAtom.setOrientationKey(getOrientationKey(ConnectionMap, obj.getID(),obj.getID()));
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getID(),obj.getID()));

				// UniqueProperty
			} else if (obj.getMetaBase().getName().equals(Constant.UNIQUEPROPERTY)) {
				kayaAtom.setParentId(obj.getParent().getID());
				KayaModelParentIdMap.put(obj.getID(), obj.getParent().getID());
				if (kayaGroupConnectionsMap.containsKey(obj.getID())) {
					kayaAtom.setGroupId(kayaGroupConnectionsMap.get(obj.getID()));
				}
				kayaAtom.setOrientationKey(getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");

				kayaAtom.setTableId(KayaModelMap.get(obj.getParent().getID()).getTableId().replace("-", "_"));
				kayaAtom.setUniqueKey(true);
				kayaMetaModelMga.getBusinessKeys().add(atomAttributesMap.get(Constant.KINDKEY).toString());
				// Property
			} else if (obj.getMetaBase().getName().equals(Constant.G_PROPERTY)) {
				kayaAtom.setParentId(obj.getParent().getID());
				if (kayaGroupConnectionsMap.containsKey(obj.getID())) {
					kayaAtom.setGroupId(kayaGroupConnectionsMap.get(obj.getID()));
				}

				KayaModelParentIdMap.put(obj.getID(), obj.getParent().getID());
				kayaAtom.setOrientationKey(getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				kayaAtom.setTableId(KayaModelMap.get(obj.getParent().getID()).getTableId().replace("-", "_"));
				kayaAtom.setUniqueKey(false);
				// Start And End
			} else if (obj.getMetaBase().getName().equals(Constant.START) || obj.getMetaBase().getName().equals(Constant.END)){
				kayaAtom.setParentId(obj.getParent().getID());
				KayaModelParentIdMap.put(obj.getID(), obj.getParent().getID());
				kayaAtom.setOrientationKey(getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				kayaAtom.setTableId(KayaModelMap.get(obj.getParent().getID()).getTableId().replace("-", "_"));
				KayaModelMap.get(obj.getParent().getID()).put(obj.getMetaBase().getName(), obj.getID());
				// Action And Conditions
			} else if (obj.getMetaBase().getName().equals(Constant.ACTION) || obj.getMetaBase().getName().equals(Constant.CONDITIONS)){
				kayaAtom.setParentId(obj.getParent().getID());
				KayaModelParentIdMap.put(obj.getID(), obj.getParent().getID());
				kayaAtom.setOrientationKey(KayaModelIdOrientationKeyMap.get(obj.getParent().getID())+ obj.getID() + ".");
				KayaModelIdOrientationKeyMap.put(obj.getID(), KayaModelIdOrientationKeyMap.get(obj.getParent().getID())+ obj.getID() + ".");

				kayaAtom.setTableId(KayaModelMap.get(obj.getParent().getID()).getTableId().replace("-", "_"));
				//ParallelGateway
			} else if (obj.getMetaBase().getName().equals(Constant.PARALLELGATEWAY) ||
					obj.getMetaBase().getName().equals(Constant.SCRIPTTASK) || 
					obj.getMetaBase().getName().equals(Constant.JAVATASK) ||
					obj.getMetaBase().getName().equals(Constant.RULETASK) ||
					obj.getMetaBase().getName().equals(Constant.MAILTASK)){
				kayaAtom.setParentId(obj.getParent().getID());
				KayaModelParentIdMap.put(obj.getID(), obj.getParent().getID());
				kayaAtom.setOrientationKey(getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");

				kayaAtom.setTableId(KayaModelMap.get(obj.getParent().getID()).getTableId());
			} else if(obj.getMetaBase().getName().equals(Constant.MASTER_ITEM)) {
				kayaAtom.setParentId(obj.getParent().getID());
				kayaAtom.setTableId(obj.getParent().getID().replace("-", "_"));
				KayaModelParentIdMap.put(obj.getID(), obj.getParent().getID());
				kayaAtom.setOrientationKey(getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				kayaAtom.setTableId(KayaModelMap.get(obj.getParent().getID()).getTableId());
			} else if (obj.getMetaBase().getName().equals(Constant.G_GROUP)) {


				if (kayaGroupToGroupConnectionsMap.containsKey(obj.getID())) {
					kayaAtom.setParentId(kayaGroupToGroupConnectionsMap.get(obj.getID()));
					KayaModelParentIdMap.put(obj.getID(), kayaGroupToGroupConnectionsMap.get(obj.getID()));
				} else {
					kayaAtom.setParentId(obj.getParent().getID());
					KayaModelParentIdMap.put(obj.getID(), obj.getParent().getID());
				}


				kayaAtom.setOrientationKey(getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
				kayaAtom.setTableId(KayaModelMap.get(obj.getParent().getID()).getTableId());
				//Set GroupItem
				List<String> groupItems = new ArrayList<String>();
				for (Map.Entry<String, String> entity : kayaGroupConnectionsMap.entrySet()) {
					if (entity.getValue().equals(obj.getID())) {
						groupItems.add(entity.getKey());
					}
				} 

				kayaAtom.setUniqueKey(false);
				// child Group
				kayaAtom.getAttributesMap().put(Constant.GROUP_ITEMS, groupItems);
				kayaAtom.getAttributesMap().put(Constant.COLSPAN, getColSpan(obj.getID(),0));

				kayaAtom.getAttributesMap().put(Constant.GROUP_LEVEL, getGroupLevel(obj.getID(),1));

				int rowspan = (int)KayaModelMap.get(obj.getParent().getID()).getAttributesMap().get(Constant.ROWSPAN);
				// Get MaxValue
				rowspan = rowspan >= getGroupLevel(obj.getID(),2)? rowspan:getGroupLevel(obj.getID(),2);
				KayaModelMap.get(obj.getParent().getID()).getAttributesMap().put(Constant.ROWSPAN,rowspan);
			}


			// kayaModel
			KayaModelMap.put(obj.getID(), kayaAtom);
			kayaLoger.info(kayaAtom.toString() + "\n");
			break;
		case MgaObject.OBJTYPE_REFERENCE:

			Map<String,Object> referenceAttributesMap = new HashMap<String,Object>();
			KayaMetaModel kayaReference = new KayaMetaModel();
			kayaReference.setGmeId(obj.getID());
			kayaReference.setMetaModelType(obj.getMetaBase().getName());

			if (Constant.LANGUAGE_DEFAULT.equals(Language)) {
				kayaReference.setName(obj.getName().trim());
			}

			kayaReference.setParentId(obj.getParent().getID());
			kayaReference.setTableId(obj.getParent().getID().replace("-", "_"));

			if (kayaGroupConnectionsMap.containsKey(obj.getID())) {
				kayaReference.setGroupId(kayaGroupConnectionsMap.get(obj.getID()));
			}

			KayaModelParentIdMap.put(obj.getID(), obj.getParent().getID());

			// Attributes
			MgaAttributes referenceAttributes = new MgaAttributes(((Dispatch)obj.get(Constant.ATTRIBUTES)));
			for (int j=0; j<referenceAttributes.getCount(); j++) {
				referenceAttributesMap.put(referenceAttributes.getItem(j).getMeta().getName() , referenceAttributes.getItem(j).getValue().toString());
				// 多语言对应
				if (Constant.MULTILANGUAGE.equals(referenceAttributes.getItem(j).getMeta().getName())) {
					for (String language : referenceAttributes.getItem(j).getValue().toString().trim().split("\n")) {
						if (language.contains("=") && Language.equals(language.split("=")[0].trim())) {
							kayaReference.setName(language.split("=")[1].trim());
						}
					}
				}
			}

			//  Reference is unique key
			if (Constant.TRUE.equals(referenceAttributesMap.get(Constant.ISUNIQUEKEY))) {
				kayaMetaModelMga.getBusinessKeys().add(
						referenceAttributesMap.get(Constant.KINDKEY).toString()
						);
				kayaReference.setUniqueKey(true);
			} else {
				kayaReference.setUniqueKey(false);
			}

			kayaReference.setTableId(KayaModelMap.get(obj.getParent().getID()).getTableId());
			// Referred
			try{
				referenceAttributesMap.put(Constant.REFERRED, (new MgaReference((Dispatch) obj)).getReferred().getID());
			} catch (Exception e) {
				kayaLoger.error("The Product[" + kayaReference.getGmeId() + "]reference object cannot be empty " + "\n");
			}
			kayaReference.setOrientationKey(getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
			KayaModelIdOrientationKeyMap.put(obj.getID(), getOrientationKey(ConnectionMap, obj.getParent().getID(), obj.getParent().getID()) + obj.getID() + ".");
			kayaReference.setAttributesMap(referenceAttributesMap);
			KayaModelMap.put(obj.getID(), kayaReference);
			kayaLoger.info(kayaReference.toString() + "\n");
			break;
		case MgaObject.OBJTYPE_CONNECTION:
			break;
		default:
			// Model creation error, contact model designer.
			kayaLoger.error("Model creation error, contact model designer.");
			break;
		}
	}
	/**
	 * XME 文件解析处理
	 * @param mainElement
	 * @param kayaMetaModelXme
	 */
	@SuppressWarnings("unchecked")
	private void parseXme(Element mainElement,KayaMetaModel kayaMetaModelXme){
		for (int i=0;i<mainElement.getChildNodes().getLength();i++){
			Node mainNode = mainElement.getChildNodes().item(i);
			if (mainNode.getNodeType() == Node.ELEMENT_NODE){
				Element eElement = (Element) mainNode;
				switch (eElement.getNodeName()) {
				case Constant.NAME:
					if (Constant.LANGUAGE_DEFAULT.equals(Language)) {
						kayaMetaModelXme.setName(eElement.getTextContent().trim());
					}
					break;
				case Constant.MODEL:
					KayaMetaModel modelKayaMetaModel = new KayaMetaModel();
					Map<String,Object> modelAttributesMap = new HashMap<String,Object>();
					List<String> modelBusinessKeys = new ArrayList<String>();
					modelKayaMetaModel.setMetaModelType(eElement.getAttribute(Constant.KIND));
					// Rowspan is 1
					modelAttributesMap.put(Constant.ROWSPAN, 1);
					modelKayaMetaModel.setAttributesMap(modelAttributesMap);
					modelKayaMetaModel.setBusinessKeys(modelBusinessKeys);
					// Role Master
					if (Constant.G_ROLE.equals(eElement.getAttribute(Constant.KIND)) || 
							Constant.MASTER.equals(eElement.getAttribute(Constant.KIND))) {
						modelKayaMetaModel.setParentId(ConnectionMap.get(eElement.getAttribute(Constant.ID)));
						modelKayaMetaModel.setTableId(getTableId(ConnectionMap,eElement.getAttribute(Constant.ID)));
						modelKayaMetaModel.setOrientationKey(getOrientationKey(ConnectionMap, eElement.getAttribute(Constant.ID),
								eElement.getAttribute(Constant.ID)));
						KayaModelIdOrientationKeyMap.put(eElement.getAttribute(Constant.ID), getOrientationKey(ConnectionMap, eElement.getAttribute(Constant.ID),
								eElement.getAttribute(Constant.ID)));
						KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID),
								ConnectionMap.get(eElement.getAttribute(Constant.ID)));
						modelKayaMetaModel.setGmeId(eElement.getAttribute(Constant.ID));
						modelKayaMetaModel.setMetaModelType(eElement.getAttribute(Constant.KIND));

						// WorkFlow
					} else if (Constant.G_WORKFLOW.equals(eElement.getAttribute(Constant.KIND))) {

						modelKayaMetaModel.setParentId(ConnectionMap.get(eElement.getAttribute(Constant.ID)));
						//modelKayaMetaModel.setTableId(Schema + "." + eElement.getAttribute(Constant.ID).replace("-", "_"));
						modelKayaMetaModel.setTableId(eElement.getAttribute(Constant.ID).replace("-", "_"));
						modelKayaMetaModel.setOrientationKey(getOrientationKey(ConnectionMap, kayaMetaModelXme.getGmeId(), kayaMetaModelXme.getGmeId()) +eElement.getAttribute(Constant.ID) + ".");
						// Set itself as a tableId
						WorkFlowTables.add(eElement.getAttribute(Constant.ID));
						KayaModelIdOrientationKeyMap.put(eElement.getAttribute(Constant.ID), getOrientationKey(ConnectionMap, kayaMetaModelXme.getGmeId(), kayaMetaModelXme.getGmeId()) +eElement.getAttribute(Constant.ID) + ".");	
						modelAttributesMap.put(Constant.GROUP_ITEMS, new ArrayList<String>());
						modelAttributesMap.put(Constant.INDEXNO, "999");
						modelAttributesMap.put(Constant.ROWSPAN, 2);
						KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID),mainElement.getAttribute(Constant.ID));
						modelKayaMetaModel.setGmeId(eElement.getAttribute(Constant.ID));
						modelKayaMetaModel.setMetaModelType(eElement.getAttribute(Constant.KIND));

					} else if (Constant.USERTASK.equals(eElement.getAttribute(Constant.KIND)) 
							|| Constant.E_GateWay.equals(eElement.getAttribute(Constant.KIND))) {

						modelKayaMetaModel.setParentId(kayaMetaModelXme.getGmeId());
						modelKayaMetaModel.setTableId(KayaModelMap.get(kayaMetaModelXme.getGmeId()).getTableId());

						modelKayaMetaModel.setOrientationKey(getOrientationKey(ConnectionMap, kayaMetaModelXme.getGmeId(),
								kayaMetaModelXme.getGmeId()) + eElement.getAttribute(Constant.ID) + ".");
						KayaModelIdOrientationKeyMap.put(eElement.getAttribute(Constant.ID), getOrientationKey(ConnectionMap, kayaMetaModelXme.getGmeId(),
								kayaMetaModelXme.getGmeId()) + eElement.getAttribute(Constant.ID) + ".");
						KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID),kayaMetaModelXme.getGmeId());
						modelKayaMetaModel.setGmeId(eElement.getAttribute(Constant.ID));
						modelKayaMetaModel.setMetaModelType(eElement.getAttribute(Constant.KIND));
						ConnectionMap.put(eElement.getAttribute(Constant.ID), kayaMetaModelXme.getGmeId());
						((List<String>) KayaModelMap.get(kayaMetaModelXme.getGmeId()).getAttributesMap().get(Constant.GROUP_ITEMS)).add(eElement.getAttribute(Constant.ID));

						// Diagram
					} else if (Constant.KAYAWORKFLOWDIAGRAM.equals(eElement.getAttribute(Constant.KIND))) {
						modelKayaMetaModel.setParentId(RootFolderId);
						modelKayaMetaModel.setTableId(eElement.getAttribute(Constant.ID).replace("-", "_"));
						modelKayaMetaModel.setOrientationKey(RootFolderId + "." + eElement.getAttribute(Constant.ID)+ ".");
						KayaModelIdOrientationKeyMap.put(eElement.getAttribute(Constant.ID), modelKayaMetaModel.getOrientationKey());
						// Diagram table
						KayaModelTables.add(eElement.getAttribute(Constant.ID));
						KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID), RootFolderId);
						KayaModelMap.put(eElement.getAttribute(Constant.ID), modelKayaMetaModel);
						modelKayaMetaModel.setGmeId(eElement.getAttribute(Constant.ID));
						modelKayaMetaModel.setMetaModelType(eElement.getAttribute(Constant.KIND));

						// Get root node
						NodeList productRefNodeList = mainElement.getElementsByTagName(Constant.REFERENCE);

						for (int prcount=0;prcount<productRefNodeList.getLength();prcount++){
							Node connectionNode = productRefNodeList.item(prcount);
							Element productRefElement = (Element) connectionNode;
							if ((Constant.PRODUCTREF).equals(productRefElement.getAttribute(Constant.KIND))) {
								ConnectionMap.put(productRefElement.getAttribute(Constant.ID), eElement.getAttribute(Constant.ID));
							} 
						}

						ConnectionMap.put(eElement.getAttribute(Constant.ID), RootFolderId);
					} else if(Constant.KAYADIAGRAM.equals(eElement.getAttribute(Constant.KIND))){
						modelKayaMetaModel.setParentId(RootFolderId);
						modelKayaMetaModel.setTableId(eElement.getAttribute(Constant.ID).replace("-", "_"));
						modelKayaMetaModel.setOrientationKey(RootFolderId + "." + eElement.getAttribute(Constant.ID)+ ".");
						KayaModelIdOrientationKeyMap.put(eElement.getAttribute(Constant.ID), modelKayaMetaModel.getOrientationKey());
						// Diagram table
						KayaModelTables.add(eElement.getAttribute(Constant.ID));
						KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID), RootFolderId);
						KayaModelMap.put(eElement.getAttribute(Constant.ID), modelKayaMetaModel);
						modelKayaMetaModel.setGmeId(eElement.getAttribute(Constant.ID));
						modelKayaMetaModel.setMetaModelType(eElement.getAttribute(Constant.KIND));

						// Get root node
						for (int l=0;l<eElement.getChildNodes().getLength();l++){
							Node productNode = eElement.getChildNodes().item(l);
							if (productNode.getNodeType() == Node.ELEMENT_NODE){
								Element productElement = (Element) productNode;
								switch (productElement.getNodeName()) {
								case Constant.ATOM:
									eElement.getAttributeNode(Constant.G_PRODUCT);
									if (!ConnectionMap.containsKey(productElement.getAttribute(Constant.ID)) && ConnectionMap.containsValue(productElement.getAttribute(Constant.ID))) {
										ConnectionMap.put(productElement.getAttribute(Constant.ID), eElement.getAttribute(Constant.ID));
									}
									break;
								default:
									break;
								}
							}
						}

						ConnectionMap.put(eElement.getAttribute(Constant.ID), RootFolderId);
					}
					KayaModelMap.put(eElement.getAttribute(Constant.ID), modelKayaMetaModel);
					parseXme(eElement,modelKayaMetaModel);
					kayaLoger.info(modelKayaMetaModel.toString() + "\n");

					break;
				case Constant.ATTRIBUTE:
					kayaMetaModelXme.put(
							eElement.getAttribute(Constant.KIND),
							eElement.getTextContent().trim());

					if (Constant.MULTILANGUAGE.equals(eElement.getAttribute(Constant.KIND))) {
						for (String language : eElement.getTextContent().trim().split("\n")) {
							if (language.contains("=") && language.contains("=") && Language.equals(language.split("=")[0].trim())) {
								kayaMetaModelXme.setName(language.split("=")[1].trim());
							}
						}
					}

					if (Constant.WORKFLOWID.equals(eElement.getAttribute(Constant.KIND)) && !UtilTools.isEmpty(eElement.getTextContent().trim())) { 
						WorkFlowItems.put(kayaMetaModelXme.getGmeId(),eElement.getTextContent().trim());
					} 
					break;
				case Constant.ATOM:
					Map<String,Object> atomAttributesMap = new HashMap<String,Object>();
					KayaMetaModel atomKayaMetaModel = new KayaMetaModel();

					List<String> atomBusinessKeys = new ArrayList<String>();
					atomKayaMetaModel.setAttributesMap(atomAttributesMap);
					atomKayaMetaModel.setBusinessKeys(atomBusinessKeys);

					atomKayaMetaModel.setGmeId(eElement.getAttribute(Constant.ID));
					atomKayaMetaModel.setMetaModelType(eElement.getAttribute(Constant.KIND));
					if ((Constant.G_PRODUCT).equals(eElement.getAttribute(Constant.KIND))) {
						// Unlinked products are added to the exception ModelList
						if (!ConnectionMap.containsValue(eElement.getAttribute(Constant.ID))
								&& !ConnectionMap.containsKey(eElement.getAttribute(Constant.ID))) {
							ErrKayaModelList.add(eElement.getAttribute(Constant.ID));
							// Diagram View root node setting（Product points to Diagram）
						} else if (ConnectionMap.containsValue(eElement.getAttribute(Constant.ID))
								&& !ConnectionMap.containsKey(eElement.getAttribute(Constant.ID))){
							// Set up the
							KayaModelTables.add(eElement.getAttribute(Constant.ID));
							KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID), 
									mainElement.getAttribute(Constant.ID));
							atomKayaMetaModel.setParentId(mainElement.getAttribute(Constant.ID));
							ConnectionMap.put(eElement.getAttribute(Constant.ID), mainElement.getAttribute(Constant.ID));

						} else {
							KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID), 
									ConnectionMap.get(eElement.getAttribute(Constant.ID)));
							atomKayaMetaModel.setParentId(ConnectionMap.get(eElement.getAttribute(Constant.ID)));
						}

						atomKayaMetaModel.setTableId(eElement.getAttribute(Constant.ID).replace("-", "_"));
						atomKayaMetaModel.setOrientationKey(
								getOrientationKey(
										ConnectionMap, eElement.getAttribute(Constant.ID),
										eElement.getAttribute(Constant.ID)));

					} else if (Constant.START.equals(eElement.getAttribute(Constant.KIND)) || Constant.END.equals(eElement.getAttribute(Constant.KIND))){
						atomKayaMetaModel.setParentId(kayaMetaModelXme.getGmeId());
						KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID), kayaMetaModelXme.getGmeId());
						atomKayaMetaModel.setOrientationKey(getOrientationKey(ConnectionMap, kayaMetaModelXme.getGmeId(), kayaMetaModelXme.getGmeId()) + eElement.getAttribute(Constant.ID) + ".");
						KayaModelIdOrientationKeyMap.put(eElement.getAttribute(Constant.ID), getOrientationKey(ConnectionMap, kayaMetaModelXme.getGmeId(), kayaMetaModelXme.getGmeId()) +eElement.getAttribute(Constant.ID) + ".");
						atomKayaMetaModel.setTableId(KayaModelMap.get(kayaMetaModelXme.getGmeId()).getTableId());
						KayaModelMap.get(kayaMetaModelXme.getGmeId()).put(eElement.getAttribute(Constant.KIND), eElement.getAttribute(Constant.ID));

						// Action And Conditions
					} else if (Constant.ACTION.equals(eElement.getAttribute(Constant.KIND)) || Constant.CONDITIONS.equals(eElement.getAttribute(Constant.KIND))){
						atomKayaMetaModel.setParentId(kayaMetaModelXme.getGmeId());
						KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID), kayaMetaModelXme.getGmeId());
						atomKayaMetaModel.setOrientationKey(KayaModelIdOrientationKeyMap.get(kayaMetaModelXme.getGmeId())+ eElement.getAttribute(Constant.ID) + ".");
						KayaModelIdOrientationKeyMap.put(eElement.getAttribute(Constant.ID), KayaModelIdOrientationKeyMap.get(kayaMetaModelXme.getGmeId())+ eElement.getAttribute(Constant.ID) + ".");

						atomKayaMetaModel.setTableId(KayaModelMap.get(kayaMetaModelXme.getGmeId()).getTableId());
						//ParallelGateway
					} else if (Constant.PARALLELGATEWAY.equals(eElement.getAttribute(Constant.KIND)) ||
							Constant.SCRIPTTASK.equals(eElement.getAttribute(Constant.KIND)) || 
							Constant.JAVATASK.equals(eElement.getAttribute(Constant.KIND)) ||
							Constant.RULETASK.equals(eElement.getAttribute(Constant.KIND)) ||
							Constant.MAILTASK.equals(eElement.getAttribute(Constant.KIND))){
						atomKayaMetaModel.setParentId(kayaMetaModelXme.getGmeId());
						KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID), kayaMetaModelXme.getGmeId());
						atomKayaMetaModel.setOrientationKey(getOrientationKey(ConnectionMap, kayaMetaModelXme.getGmeId(), kayaMetaModelXme.getGmeId()) + eElement.getAttribute(Constant.ID) + ".");
						KayaModelIdOrientationKeyMap.put(eElement.getAttribute(Constant.ID), getOrientationKey(ConnectionMap,kayaMetaModelXme.getGmeId(), kayaMetaModelXme.getGmeId()) + eElement.getAttribute(Constant.ID) + ".");

						atomKayaMetaModel.setTableId(KayaModelMap.get(kayaMetaModelXme.getGmeId()).getTableId());
						// Property MasterRef
					} else if (Constant.G_GROUP.equals(eElement.getAttribute(Constant.KIND))) {

						if (kayaGroupToGroupConnectionsMap.containsKey(eElement.getAttribute(Constant.ID))) {
							atomKayaMetaModel.setParentId(kayaGroupToGroupConnectionsMap.get(eElement.getAttribute(Constant.ID)));
							KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID),kayaGroupToGroupConnectionsMap.get(eElement.getAttribute(Constant.ID)));
						} else {
							atomKayaMetaModel.setParentId(kayaMetaModelXme.getGmeId());
							KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID), kayaMetaModelXme.getGmeId());
						}

						atomKayaMetaModel.setOrientationKey(KayaModelIdOrientationKeyMap.get(kayaMetaModelXme.getGmeId())+ eElement.getAttribute(Constant.ID) + ".");
						KayaModelIdOrientationKeyMap.put(eElement.getAttribute(Constant.ID),KayaModelIdOrientationKeyMap.get(kayaMetaModelXme.getGmeId())+ eElement.getAttribute(Constant.ID) + ".");
						atomKayaMetaModel.setTableId(KayaModelMap.get(kayaMetaModelXme.getGmeId()).getTableId());
						//Set GroupItem
						List<String> groupItems = new ArrayList<String>();
						for (Map.Entry<String, String> entity : kayaGroupConnectionsMap.entrySet()) {
							if (entity.getValue().equals(eElement.getAttribute(Constant.ID))) {
								groupItems.add(entity.getKey());
							}
						}    
						atomKayaMetaModel.getAttributesMap().put(Constant.GROUP_ITEMS, groupItems);

						// Group
						atomKayaMetaModel.getAttributesMap().put(Constant.COLSPAN, getColSpan(eElement.getAttribute(Constant.ID),0));
						atomKayaMetaModel.getAttributesMap().put(Constant.GROUP_LEVEL, getGroupLevel(eElement.getAttribute(Constant.ID),1));

						int rowspan = (int)KayaModelMap.get(kayaMetaModelXme.getGmeId()).getAttributesMap().get(Constant.ROWSPAN);
						// Get MaxValue
						rowspan = rowspan >= getGroupLevel(eElement.getAttribute(Constant.ID),2)? rowspan:getGroupLevel(eElement.getAttribute(Constant.ID),2);
						KayaModelMap.get(kayaMetaModelXme.getGmeId()).getAttributesMap().put(Constant.ROWSPAN,rowspan);


					}else {
						atomKayaMetaModel.setParentId(kayaMetaModelXme.getGmeId());
						// Set GroupId
						if (kayaGroupConnectionsMap.containsKey(eElement.getAttribute(Constant.ID))) {
							atomKayaMetaModel.setGroupId(kayaGroupConnectionsMap.get(eElement.getAttribute(Constant.ID)));
						}

						atomKayaMetaModel.setTableId(KayaModelMap.get(kayaMetaModelXme.getGmeId()).getTableId());

						atomKayaMetaModel.setOrientationKey(
								getOrientationKey(ConnectionMap, kayaMetaModelXme.getGmeId(), 
										kayaMetaModelXme.getGmeId()) 
								+ eElement.getAttribute(Constant.ID) + ".");

						KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID),
								kayaMetaModelXme.getGmeId());
						if (Constant.UNIQUEPROPERTY.equals(eElement.getAttribute(Constant.KIND))) {
							atomKayaMetaModel.setUniqueKey(true);
						} else {
							atomKayaMetaModel.setUniqueKey(false);
						}
					}
					// Attribute
					for (int attributeCount=0;attributeCount<eElement.getChildNodes().getLength();attributeCount++){
						Node atrributeNode = eElement.getChildNodes().item(attributeCount);
						if (atrributeNode.getNodeType() == Node.ELEMENT_NODE){
							Element attributeElement = (Element) atrributeNode;

							// multiple languages
							if (Constant.NAME.equals(attributeElement.getTagName())) {

								if (Constant.LANGUAGE_DEFAULT.equals(Language)) {
									atomKayaMetaModel.setName(attributeElement.getTextContent().trim());
								}

							} else if (Constant.ATTRIBUTE.equals(attributeElement.getTagName())){
								atomKayaMetaModel.put(
										attributeElement.getAttribute(Constant.KIND), 
										attributeElement.getTextContent().trim());

								// MasterItem
								if (Constant.FIELD_ATTRIBUTES.equals(attributeElement.getAttribute(Constant.KIND))) {
									List<KayaModelMasterItem> MasterItemList = new ArrayList<KayaModelMasterItem>();
									for (String master : attributeElement.getTextContent().trim().split("\n")) {
										KayaModelMasterItem masterItem = new KayaModelMasterItem();
										String[] languageStr = master.split(":", -1);// 默认第二个参数为-1
										masterItem.setId(languageStr[0].trim());

										if (Constant.LANGUAGE_DEFAULT.equals(Language)) {
											masterItem.setText(languageStr[1].trim());
										} else {
											for (String language : languageStr) {
												if (language.contains("=") && Language.equals(language.split("=")[0].trim())) {
													masterItem.setText(language.split("=")[1].trim());
												}
											}
										}
										MasterItemList.add(masterItem);
									}
									atomAttributesMap.put(attributeElement.getAttribute(Constant.KIND), MasterItemList);
									// Other
								} else if (Constant.ORGANIZATION.equals(attributeElement.getAttribute(Constant.KIND))) {
									List<KayaModelOrganizationItem> organizationItems = new ArrayList<KayaModelOrganizationItem>();
									for (String organizationItem : attributeElement.getTextContent().trim().split("\n")) {
										KayaModelOrganizationItem kayaModelOrganizationItem = new KayaModelOrganizationItem();
										List<String> valueList = Arrays.asList(organizationItem.split("="));
										if(valueList.size()>1){
											kayaModelOrganizationItem.setRef(true);
											kayaModelOrganizationItem.setRefSrc(valueList.get(0));
											kayaModelOrganizationItem.setRefDst(valueList.get(1));
										} else {
											kayaModelOrganizationItem.setRef(false);
											kayaModelOrganizationItem.setRefSrc(Constant.EMPTY);
											kayaModelOrganizationItem.setRefDst(Constant.EMPTY);
											kayaModelOrganizationItem.setText(organizationItem);
										}
										organizationItems.add(kayaModelOrganizationItem);
									}
									atomAttributesMap.put(attributeElement.getAttribute(Constant.KIND), organizationItems);
									// 
								} else if (Constant.WF_PERMISSIONS.equals(attributeElement.getAttribute(Constant.KIND))) {
									List<KayaModelPermissionsItem> permissionItems = new ArrayList<KayaModelPermissionsItem>();
									for (String permissionItem : attributeElement.getTextContent().trim().split("\n")) {
										if(StringUtil.isNotEmpty(permissionItem)) {
											KayaModelPermissionsItem perItem = new KayaModelPermissionsItem();
											permissionItem = permissionItem.replace("\"", "");
											String[] perStr = permissionItem.split(":|==", 2);
											if(perStr.length > 1) {
												perItem.setId(perStr[0].toString());
												String perValue = perStr[1].toString();
												if(perValue.startsWith("{") || perValue.startsWith("[") ){
													String newString = perValue.substring(1, perValue.length() - 1);
													List<String> valueList = Arrays.asList(newString.split(","));
													perItem.setText(Constant.EMPTY);
													perItem.setTextList(valueList);
												}else {
													perItem.setText(perValue);
												}
												permissionItems.add(perItem);
											} else {								
												kayaLoger.error("(" + eElement.getTextContent() + ":" + eElement.getAttribute(Constant.ID) + ")Permissions format was invalid. "+ "\n");
												kayaLoger.error("Please use the format: userInfo:{M,GL} " + "\n");
											}
										}
									}
									atomAttributesMap.put(attributeElement.getAttribute(Constant.KIND), permissionItems);
									//
								} else if (Constant.MULTILANGUAGE.equals(attributeElement.getAttribute(Constant.KIND))) {
									for (String language : attributeElement.getTextContent().trim().split("\n")) {
										if (language.contains("=") && Language.equals(language.split("=")[0].trim())) {
											atomKayaMetaModel.setName(language.split("=")[1].trim());
										}
									}
								} else {
									atomAttributesMap.put(attributeElement.getAttribute(Constant.KIND), attributeElement.getTextContent().trim());
								}

								if (Constant.UNIQUEPROPERTY.equals(eElement.getAttribute(Constant.KIND))
										&& Constant.KINDKEY.equals(attributeElement.getAttribute(Constant.KIND))){
									kayaMetaModelXme.getBusinessKeys().add(attributeElement.getTextContent());
									//atomKayaMetaModel.setUniqueKey(true);
								} 
								//else {
								//atomKayaMetaModel.setUniqueKey(false);
								//}
							} else if (Constant.REGNODE.equals(attributeElement.getTagName())) {
								//TODO： Element location information
								atomKayaMetaModel.put(Constant.INDEXNO, attributeElement.getTextContent().split(",")[0]);
								//Logger.log(Level.WARNING,attributeElement.getTextContent() + "\n");
							}
						}
					}


					KayaModelMap.put(eElement.getAttribute(Constant.ID), atomKayaMetaModel);
					kayaLoger.info(atomKayaMetaModel.toString() + "\n");
					break;
				case Constant.REFERENCE:
					KayaMetaModel refKayaMetaModel = new KayaMetaModel();
					Map<String,Object> refAttributesMap = new HashMap<String,Object>();
					List<String> refBusinessKeys = new ArrayList<String>();
					refKayaMetaModel.setAttributesMap(refAttributesMap);
					refKayaMetaModel.setBusinessKeys(refBusinessKeys);

					refKayaMetaModel.setGmeId(eElement.getAttribute(Constant.ID));
					refKayaMetaModel.setMetaModelType(eElement.getAttribute(Constant.KIND));
					refKayaMetaModel.setParentId(kayaMetaModelXme.getGmeId());
					// Group id
					if (kayaGroupConnectionsMap.containsKey(eElement.getAttribute(Constant.ID))) {
						refKayaMetaModel.setGroupId(kayaGroupConnectionsMap.get(eElement.getAttribute(Constant.ID)));
					}

					refKayaMetaModel.setTableId(getTableId(ConnectionMap,kayaMetaModelXme.getGmeId()));
					refKayaMetaModel.setOrientationKey(
							getOrientationKey(
									ConnectionMap, kayaMetaModelXme.getGmeId(),
									kayaMetaModelXme.getGmeId())
							+ eElement.getAttribute(Constant.ID) + ".");

					KayaModelParentIdMap.put(eElement.getAttribute(Constant.ID), 
							kayaMetaModelXme.getGmeId());

					// Attribute
					for (int attributeCount=0;attributeCount<eElement.getChildNodes().getLength();attributeCount++){
						Node atrributeNode = eElement.getChildNodes().item(attributeCount);
						if (atrributeNode.getNodeType() == Node.ELEMENT_NODE){
							Element attributeElement = (Element) atrributeNode;
							// multiple languages
							if (Constant.NAME.equals(attributeElement.getTagName())) {

								if (Constant.LANGUAGE_DEFAULT.equals(Language)) {
									refKayaMetaModel.setName(attributeElement.getTextContent().trim());
								}
							} else if (Constant.ATTRIBUTE.equals(attributeElement.getTagName())){
								refKayaMetaModel.put(
										attributeElement.getAttribute(Constant.KIND),
										attributeElement.getTextContent().trim());

								if (Constant.MULTILANGUAGE.equals(attributeElement.getAttribute(Constant.KIND))) {
									for (String language : attributeElement.getTextContent().trim().split("\n")) {
										if (language.contains("=") && Language.equals(language.split("=")[0].trim())) {
											refKayaMetaModel.setName(language.split("=")[1].trim());
										}
									}
								}
							}
						}
					}
					// referred
					refKayaMetaModel.put(Constant.REFERRED,eElement.getAttribute(Constant.REFERRED));
					if (Constant.TRUE.equals(refKayaMetaModel.get(Constant.ISUNIQUEKEY))){
						kayaMetaModelXme.getBusinessKeys().add(refKayaMetaModel.get(Constant.KINDKEY));
						refKayaMetaModel.setUniqueKey(true);
					} else {
						refKayaMetaModel.setUniqueKey(false);
					}
					if ((Constant.PRODUCTREF).equals(eElement.getAttribute(Constant.KIND))) {
						ProductRefMap.put(eElement.getAttribute(Constant.ID), eElement.getAttribute(Constant.REFERRED));
						ConnectionMap.put(eElement.getAttribute(Constant.ID), kayaMetaModelXme.getGmeId());
					} 
					KayaModelMap.put(eElement.getAttribute(Constant.ID), refKayaMetaModel);
					kayaLoger.info(refKayaMetaModel.toString() + "\n");
					break;
				default:
					break;
				}
			}
		}
	}

	/*
	 * Get Element Connection
	 * @param element
	 */
	private void ParseKayaModel_Connection(Element element){
		NodeList connectionNodeList = element.getElementsByTagName(Constant.CONNECTION);

		for (int i=0;i<connectionNodeList.getLength();i++){
			Node connectionNode = connectionNodeList.item(i);
			Element connectionElement = (Element) connectionNode;
			switch (connectionElement.getAttribute(Constant.KIND)){
			case Constant.RRC:
				setConnpointInf(connectionNode,KayaRoleConnectionsMap);
				break;
			case Constant.RPC:
			case Constant.MPC:
				KayaModelTables.add(setConnpointInf(connectionNode,KayaModelRoleToProductConnectionMap));
				break;
			case Constant.PPC:
				KayaModelTables.add(setConnpointInf(connectionNode,KayaProductConnectionsMap));
				break;
			case Constant.WPC:
				setConnpointInf(connectionNode,WorkFlowProductRefMap);
				break;
			case Constant.EAC:
			case Constant.MAC:
				setWorkFlowConnpointInf(connectionNode,true);
				break;

			case Constant.PGC:
			case Constant.MGC:
			case Constant.PRGC:
				setConnpointInf(connectionNode,kayaPropertyToGroupConnectionsMap);
				setConnpointInf(connectionNode,kayaGroupConnectionsMap);
				break;
			case Constant.GGC:
				setConnpointInf(connectionNode,kayaGroupConnectionsMap);
				setConnpointInf(connectionNode,kayaGroupToGroupConnectionsMap);
				break;
			case Constant.ATC:
			case Constant.TTC:
			case Constant.TMC:
			case Constant.AMC:
			case Constant.AEC:
			case Constant.TEC:
			case Constant.STA:
			case Constant.SMC:
				setWorkFlowConnpointInf(connectionNode,false);
				break;
			default:
				break;
			}
		}
	}

	/*
	 * get model element connection
	 * @param element
	 */
	private void ParseKayaModel_ProductRef(Element element){
		NodeList productRefNodeList = element.getElementsByTagName(Constant.REFERENCE);

		for (int i=0;i<productRefNodeList.getLength();i++){
			Node connectionNode = productRefNodeList.item(i);
			Element productRefElement = (Element) connectionNode;
			if ((Constant.PRODUCTREF).equals(productRefElement.getAttribute(Constant.KIND))) {
				ProductRefMap.put(productRefElement.getAttribute(Constant.ID), productRefElement.getAttribute(Constant.REFERRED));
			} 
		}
	}

	/*
	 * element connection info
	 * @param node
	 * @param classificationMap
	 * @return
	 */
	private String setConnpointInf(Node node,Map<String,String>  classificationMap) {
		NodeList connectionList = node.getChildNodes();
		String dstStr = "";
		String srcStr = "";
		for(int index=0;index < connectionList.getLength();index++) {
			Node conpoint = connectionList.item(index);
			if (conpoint.getNodeType() == Node.ELEMENT_NODE){
				Element eElement = (Element) conpoint;
				switch (eElement.getAttribute(Constant.ROLE)){
				case Constant.DST:
					dstStr = eElement.getAttribute(Constant.TARGET);
					break;
				case  Constant.SRC:
					srcStr = eElement.getAttribute(Constant.TARGET);
					break;
				default:
					break;
				}
			}
		}
		classificationMap.put(srcStr, dstStr);
		ConnectionMap.put(srcStr, dstStr);
		return srcStr;
	}

	/*
	 * workflow element connection info
	 * @param node
	 * @param isReverse
	 */
	private void setWorkFlowConnpointInf(Node node, boolean isReverse) {
		NodeList connectionList = node.getChildNodes();
		String dstStr = "";
		String srcStr = "";
		for(int index=0;index < connectionList.getLength();index++) {
			Node conpoint = connectionList.item(index);
			if (conpoint.getNodeType() == Node.ELEMENT_NODE){
				Element eElement = (Element) conpoint;
				switch (eElement.getAttribute(Constant.ROLE)){
				case Constant.DST:
					dstStr = eElement.getAttribute(Constant.TARGET);
					break;
				case  Constant.SRC:
					srcStr = eElement.getAttribute(Constant.TARGET);
					break;
				default:
					break;
				}
			}
		}
		if (isReverse) {
			if (WorkFlowConnectionMap.containsKey(dstStr)) {
				WorkFlowConnectionMap.get(dstStr).setKayaModelId(srcStr);
			} else {
				ConnectionMap.put(dstStr,"Warning:Please confirm the operation object.");
				WorkFlowConnection workFlowConnection = new WorkFlowConnection();
				workFlowConnection.setReverse(isReverse);
				workFlowConnection.setKayaModelId(srcStr);
				WorkFlowConnectionMap.put(dstStr, workFlowConnection);
			}
		} else {
			if (WorkFlowConnectionMap.containsKey(srcStr)) {
				WorkFlowConnectionMap.get(srcStr).setKayaModelId(dstStr);
			} else {
				ConnectionMap.put(srcStr,dstStr);
				WorkFlowConnection workFlowConnection = new WorkFlowConnection();
				workFlowConnection.setReverse(isReverse);
				workFlowConnection.setKayaModelId(dstStr);
				WorkFlowConnectionMap.put(srcStr, workFlowConnection);
			}
		}

	}


	/*
	 * get orientation key
	 * @param connectionMap
	 * @param gmeId
	 * @param emptyKey
	 * 
	 */
	private static String getOrientationKey(Map<String,String> connectionMap, String gmeId, String emptyKey) {

		if (connectionMap.containsKey(gmeId)) {
			if (!Constant.EMPTY.equals(connectionMap.get(gmeId))) {
				emptyKey = connectionMap.get(gmeId) + "." +emptyKey;
			}

			return getOrientationKey(connectionMap,connectionMap.get(gmeId),emptyKey);
		} else {
			return emptyKey + ".";
		}
	}

	private String getTableId(Map<String,String> connectionMap, String gmeId) {

		if (KayaModelTables.contains(gmeId)) {
			return gmeId.replace("-", "_");
		} if (KayaRoleConnectionsMap.containsKey(gmeId)) {
			return getTableId(connectionMap,KayaRoleConnectionsMap.get(gmeId));
		} if(KayaModelTables.contains(ProductRefMap.get(connectionMap.get(gmeId)))){
			return ProductRefMap.get(connectionMap.get(gmeId)).replace("-", "_");
		} else {
			return getTableId(connectionMap,connectionMap.get(gmeId));
		}
	}

	private String CreatePostegreSqlTreeTables(String tableName) {
		String createTablseSQL = "";
		createTablseSQL = createTablseSQL 
				+ " CREATE TABLE IF NOT Exists " + tableName + "("
				+  "gmeid      varchar(16) NOT NULL,"
				+  "businessid   varchar(25) NOT NULL,"
				+  "businesssubid   varchar(25) ,"
				+  "relid   varchar(25) ,"
				+  "kind      varchar(40) ,"
				+  "name      varchar(255),"
				+  "kindvalue    varchar(255),"
				+  "kindtype    varchar(20),"
				+  "currentno    varchar(25 ),"
				+  "securitycode  varchar(25),"
				+  "flowcode    varchar(16),"
				+  "flowsubcode   varchar(16),"
				+  "startdate    timestamp                      NOT NULL,"
				+  "enddate     timestamp                      NOT NULL,"
				+  "withdrawaldate timestamp                      NOT NULL,"
				+  "orientationkey varchar(500),"
				+  "parentid    varchar(16),"
				+  "groupid    varchar(16),"
				+  "createdate   timestamp,"
				+  "createuser   varchar(25),"
				+  "updatedate   timestamp,"
				+  "updateuser   varchar(25),"
				+  "lockflg     boolean,"
				+  "lockuser    varchar(25),"
				+  "lockdate    timestamp,"
				+  "updatemachine  varchar(25),"
				+  "PRIMARY KEY (businessid,gmeid,businesssubid));";
		return createTablseSQL;
	}

	private String CreateMysqlTreeTables(String tableName) {
		String createTablseSQL = "";
		createTablseSQL = createTablseSQL 
				+ "CREATE TABLE IF NOT Exists " + tableName + "("
				+   "gmeid           VARCHAR(16) CHARSET utf8 COLLATE utf8_general_ci    NOT NULL,"
				+   "businessid      VARCHAR(125) CHARSET utf8 COLLATE utf8_general_ci    NOT NULL,"
				+   "businesssubid      VARCHAR(125) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "relid      VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "kind            VARCHAR(40) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "name            VARCHAR(255) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "kindvalue       VARCHAR(255) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "kindtype        VARCHAR(20) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "securitycode    VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "flowcode        VARCHAR(16) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "flowsubcode     VARCHAR(16) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "startdate       DATETIME                                            NOT NULL,"
				+   "enddate         DATETIME                                            NOT NULL,"
				+   "withdrawaldate  DATETIME                                            NOT NULL,"
				+   "orientationkey  VARCHAR(500) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "parentid        VARCHAR(16) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "groupid        VARCHAR(16) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "createdate      DATETIME,"
				+   "createuser      VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "updatedate      DATETIME,"
				+   "updateuser      VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "lockflg         BIT,"
				+   "lockuser        VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "lockdate        DATETIME,"
				+   "updatemachine   VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "PRIMARY KEY (businessid,gmeid,businesssubid))"
				+   "ENGINE=MyISAM,"
				+   "COLLATE=utf8_general_ci;";
		return createTablseSQL;
	}

	private String CreateH2TreeTables(String tableName) {
		String createTablseSQL = "";

		createTablseSQL = createTablseSQL 
				+ " CREATE TABLE IF NOT Exists " + tableName + "("
				+ "gmeid           VARCHAR(16) NOT NULL,"
				+ "businessid      VARCHAR(125) NOT NULL,"
				+ "businesssubid   VARCHAR(125) ,"
				+ "relid   VARCHAR(25) ,"
				+ "kind            VARCHAR(40) ,"
				+ "name            VARCHAR(255) ,"
				+ "kindvalue       VARCHAR(255) ,"
				+ "kindtype        VARCHAR(20) ,"
				+ "securitycode    VARCHAR(25) ,"
				+ "flowcode        VARCHAR(16) ,"
				+ "flowsubcode     VARCHAR(16) ,"
				+ "startdate       DATE      NOT NULL,"
				+ "enddate         DATE      NOT NULL,"
				+ "withdrawaldate  DATE     NOT NULL,"
				+ "orientationkey  VARCHAR(500) ,"
				+ "parentid        VARCHAR(16) ,"
				+ "groupid        VARCHAR(16) ,"
				+ "createdate      DATE,"
				+ "createuser      VARCHAR(25) ,"
				+ "updatedate      DATE,"
				+ "updateuser      VARCHAR(25) ,"
				+ "lockflg         BIT,"
				+ "lockuser        VARCHAR(25) ,"
				+ "lockdate        DATE,"
				+ "updatemachine   VARCHAR(25) ,"
				+ "PRIMARY KEY (businessid, gmeid, businesssubid));";
		return createTablseSQL;
	}

	private String CreateMysqlWorkflowTables(String tableName) {
		String createTablseSQL = "";
		createTablseSQL = createTablseSQL 
				+ "CREATE TABLE IF NOT Exists " + tableName + "("
				+   "gmeid           VARCHAR(16) CHARSET utf8 COLLATE utf8_general_ci    NOT NULL,"
				+   "relid      VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "kind            VARCHAR(40) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "name            VARCHAR(255) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "kindvalue       VARCHAR(255) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "kindtype        VARCHAR(20) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "securitycode    VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "flowcode        VARCHAR(16) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "flowsubcode     VARCHAR(16) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "startdate       DATETIME                                            NOT NULL,"
				+   "enddate         DATETIME                                            NOT NULL,"
				+   "withdrawaldate  DATETIME                                            NOT NULL,"
				+   "orientationkey  VARCHAR(255) CHARSET utf8 COLLATE utf8_general_ci    NOT NULL,"
				+   "parentid        VARCHAR(16) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "groupid        VARCHAR(16) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "createdate      DATETIME,"
				+   "createuser      VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "updatedate      DATETIME,"
				+   "updateuser      VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "lockflg         BIT,"
				+   "lockuser        VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "lockdate        DATETIME,"
				+   "updatemachine   VARCHAR(25) CHARSET utf8 COLLATE utf8_general_ci,"
				+   "PRIMARY KEY (gmeid,relid,orientationkey)) "
				+   "ENGINE=MyISAM,"
				+   "COLLATE=utf8_general_ci;";
		return createTablseSQL;
	}
	private String CreateH2WorkflowTables(String tableName) {
		String createTablseSQL = "";

		createTablseSQL = "DROP TABLE IF EXISTS " + tableName + ";" ;
		createTablseSQL = createTablseSQL 
				+ " CREATE TABLE IF NOT Exists " + tableName + "("
				+ "gmeid           VARCHAR(16) NOT NULL,"
				+ "businessid      VARCHAR(125) NOT NULL,"
				+ "businesssubid   VARCHAR(125) ,"
				+ "relid   VARCHAR(25) ,"
				+ "kind            VARCHAR(40) ,"
				+ "name            VARCHAR(255) ,"
				+ "kindvalue       VARCHAR(255) ,"
				+ "kindtype        VARCHAR(20) ,"
				+ "securitycode    VARCHAR(25) ,"
				+ "flowcode        VARCHAR(16) ,"
				+ "flowsubcode     VARCHAR(16) ,"
				+ "startdate       DATE      NOT NULL,"
				+ "enddate         DATE      NOT NULL,"
				+ "withdrawaldate  DATE     NOT NULL,"
				+ "orientationkey  VARCHAR(255) ,"
				+ "parentid        VARCHAR(16) ,"
				+ "groupid        VARCHAR(16) ,"
				+ "createdate      DATE,"
				+ "createuser      VARCHAR(25) ,"
				+ "updatedate      DATE,"
				+ "updateuser      VARCHAR(25) ,"
				+ "lockflg         BIT,"
				+ "lockuser        VARCHAR(25) ,"
				+ "lockdate        DATE,"
				+ "updatemachine   VARCHAR(25) ,"
				+ "PRIMARY KEY (businessid, gmeid, businesssubid,relid));";
		return createTablseSQL;
	}

	public static Properties getProperties(String fileName) {
		try {
			// The configuration directory is read first and the class path is not loaded again
			String outpath = System.getProperty("user.dir")+File.separator+"config"+File.separator;

			Properties properties = new Properties();
			InputStream in = new FileInputStream(new File(outpath + fileName));
			properties.load(in);
			return properties;
		} catch (IOException e) {

			try {
				Properties properties = new Properties();
				// The class path is loaded by default
				InputStream in = ParseKayaModel_XPATH.class.getClassLoader().getResourceAsStream(fileName);
				properties.load(in);
				return properties;
			} catch (IOException es) {
				kayaLoger.error(es.getMessage() + "\n");
				return null;
			}
		}
	}

	/*
	 * Gets all child elements (without child roles), but with their own roles
	 * @param parentId
	 * @return
	 */
	private List<KayaMetaModel> getKayaModelByParentIdNotRole(String parentId){
		List<KayaMetaModel> kayaModelList=new Vector<>();

		//kayaModelList.add(KayaModelMap.get(parentId));
		for (Map.Entry<String, String> entity : KayaModelParentIdMap.entrySet()) {
			if(entity.getValue().equals(parentId) && !Constant.G_ROLE.equals(KayaModelMap.get(entity.getKey()).getMetaModelType())
					&& !Constant.G_GROUP.equals(KayaModelMap.get(entity.getKey()).getMetaModelType())
					&& !Constant.ACTION.equals(KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(KayaModelMap.get(entity.getKey()));
			}
		}
		// IndexNo
		Collections.sort(kayaModelList);
		return kayaModelList;
	}

	private String getCreateTablesSqlText(String parentId) {
		String createTablseSQL = "";
		//  get Role info
		String tableName = KayaModelMap.get(parentId).getTableId();
		if(KayaModelMap.get(parentId).get(Constant.CREATETABLETYPE).equals(Constant.UPDATE)) {
			createTablseSQL = "DROP TABLE IF EXISTS " + tableName + ";";
		}
		createTablseSQL = createTablseSQL 
				+ "CREATE TABLE IF NOT Exists " + tableName + "(";
		for(KayaMetaModel kayaMetaModel:getKayaModelByParentIdNotRole(parentId)) {

			// 自动字段

			// 必须入力字段

			// 主键

			// 通用属性处理


			createTablseSQL = createTablseSQL + kayaMetaModel.get(Constant.KINDKEY) + " VARCHAR(" 
					+ kayaMetaModel.get(Constant.DATALENGTH) + ")  CHARSET utf8 COLLATE utf8_general_ci";
			if (kayaMetaModel.get(Constant.REQUIRED).equals("True")) {

			}
		}
		return "";
	}

	/*
	 * Group RowSpan
	 * @param kayaMetaGroupId
	 * @param level
	 * @return
	 */
	private int getGroupLevel(String kayaMetaGroupId,int level) {

		if (kayaGroupToGroupConnectionsMap.containsKey(kayaMetaGroupId)) {
			level = level + 1;
			level = getGroupLevel(kayaGroupToGroupConnectionsMap.get(kayaMetaGroupId),level);
		} else {
			return level;
		}

		return level;

	}

	/*
	 * Group ColSpan
	 * @param kayaMetaGroupId
	 * @param colspan
	 * @return
	 */
	private int getColSpan(String kayaMetaGroupId,int colspan) {

		// How many children does a Group contain (not a Group)
		for (Map.Entry<String, String> entity1 : kayaPropertyToGroupConnectionsMap.entrySet()) {
			if (entity1.getValue().equals(kayaMetaGroupId)) {
				colspan = colspan + 1;
			}
		} 
		// If there are subgroups
		for (Map.Entry<String, String> entity2 : kayaGroupToGroupConnectionsMap.entrySet()) {
			if (entity2.getValue().equals(kayaMetaGroupId)) {
				colspan = getColSpan(entity2.getKey(),colspan);
			}
		}

		return colspan;
	}
}
