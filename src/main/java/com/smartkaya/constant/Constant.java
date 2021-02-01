package com.smartkaya.constant;
/**
 * KaYa 元模型固定变量
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public class Constant {
	
	public static final String KAYA = "KaYa";
	public static final String MGA = "MGA=";
	public static final String DTD = "classpath:mga.dtd";
	public static final String LOG = "Log";
	// KAYA Settings Start
	public static final String DATABASESETTINGS = "DataBaseSettings";
	public static final String DBTYPE = "DbType";
	public static final String DRIVER = "Driver";
	public static final String URL = "URL";
	public static final String HOST = "Host";
	public static final String DBNAME = "DatabaseName";
	public static final String DBPROPERTYS = "DbPropertys";
	
	public static final String USER = "UserName";
	public static final String PWD = "Password";
	public static final String SCHEMA = "Schema";
	public static final String KAYAMETAMODELPATH = "metamodelPath";
	public static final String KAYAMETAMODELPARSELOGPATH = "kayaMetaModelParseLogPath";
	public static final String KAYAMETAMODELSQLLOGPATH = "kayaMetaModelSqlLogPath";
	public static final String KAYAMETAMODELCONNECTIONLOGPATH = "kayaMetaModelConnectionLogPath";
	public static final String KAYAMODELBASEPATH = "baseModel.basePath";// model file path
	public static final String KAYAMODELMGAV = "baseModel.mgaVersion";// gmeVersion model name
	public static final String KAYAMODELXMEV = "baseModel.xmeVersion";// xmeVersion model name
	
	
	

	public static final String BUSINESSID = "businessid";
	public static final String ORIENTATIONKEY = "orientationkey";
	public static final String KINDTYPE = "kindtype";
	public static final String KINDVALUE = "kindvalue";
	public static final String GMEID = "gmeid";
	public static final String PARENTID = "parentid";

	public static final String NM = "_NM";
	public static final String CD_NM = "_CD_NM";
	
	
	/**
	 * 是否显示（隐藏项目）
	 */
	public static final String DISPLAY = "Display";
	/**
	 * 必须填写项目
	 */
	public static final String REQUIRED = "Required";
	/****/
	public static final String LANGUAGE = "Language";
	public static final String LANGUAGE_DEFAULT = "Default";
	public static final String LANGUAGE_1 = "Language1";
	public static final String LANGUAGE_2 = "Language2";
	
	public static final String KAYAMODELPATH = "modelPath";
	public static final String KAYARULEPATH = "rulePath";
	public static final String KAYAWORKFLOWPATH = "workFlowPath";
	public static final String OPERATIONALMODE = "OperationalMode";
	public static final String MODE = "mode";
	public static final String DEVELOPER = "dev";
	public static final String PRODUCTION = "prod";
	
	// KAYA Settings End
	public static final String EMPTY = "";
	
	public static final String NAME = "name";
	public static final String KIND = "kind";
	public static final String ATTRIBUTES = "Attributes";
	public static final String RELID = "relid";
	public static final String GUID = "guid";
	public static final String VERSION = "version";
	public static final String TRUE = "true";
	
	public static final String PROPERTYREF = "PropertyReference";
	
	public static final String G_PRODUCT = "Product";
	public static final String G_ROLE = "Role";
	public static final String G_RULE = "Rule";
	public static final String G_PROPERTY = "Property";
	public static final String G_EXCLUSIVEGATEWAY = "ExclusiveGateway";
	public static final String G_GROUP = "Group";
	public static final String GROUP_ITEMS = "GroupItems";
	public static final String GROUP_LEVEL = "GroupLevel";
	public static final String ROWSPAN = "Rowspan";
	public static final String COLSPAN = "colspan";
	
	
	public static final String ID = "id";
	public static final String ROLE = "role";
	public static final String INDEXNO = "IndexNo";
	public static final String DATATYPE = "DataType";
	public static final String FORMAT = "Format";
	public static final String DATALENGTH = "DataLength";
	public static final String DEFAULTVALUE = "DefaultValue";
	public static final String ISENCRYPTION = "IsEncryption";

	// Project
	public static final String PROJECT = "project";
	public static final String PROJECT_GUID = "guid";
	public static final String PROJECT_METAGUID = "metaguid";
	public static final String PROJECT_METANAME = "metaname";
	public static final String KAYADIAGRAM = "KaYaDiagram";
	public static final String KAYAWORKFLOWDIAGRAM = "KayaWorkFlowDiagram";
	public static final String KAYARULESDIAGRAM = "KayaRulesDiagram";
	
	public static final String PROJECT_MATEVERSION = "metaversion";
	public static final String COMMENT = "comment";
	public static final String AUTHOR = "author";
	
	// Folder
	public static final String FOLDER = "folder";
	
	// Model
	public static final String MODEL = "model";
	
	//DataType
	public static final String AUTO="Auto";
	
	// Atom
	public static final String ATOM = "atom";
	public static final String ATTRIBUTE = "attribute";
	public static final String REGNODE ="regnode";
	public static final String VALUE = "value";
	public static final String UNIQUEPROPERTY = "UniqueProperty";
	public static final String ISUNIQUEKEY = "IsUniqueKey";
	
	public static final String KINDKEY = "KindKey";
	public static final String STARTDATE = "StartDate";
	public static final String ENDDATE = "EndDate";
	public static final String WITHDRAWALDATE = "withdrawaldate";

	public static final String REFERENCE = "reference";
	public static final String REFERRED = "referred";
	
	
	
	// Connection
	public static final String CONNECTION = "connection";
	public static final String CONNPOINT = "connpoint";
	public static final String SRC = "src";
	public static final String DST = "dst";
	public static final String TARGET = "target";
	public static final String RRC = "RoleToRoleConnection";
	public static final String PPC = "ProductToProductConnection";
	public static final String RPC = "RoleToProductConnection";
	public static final String MPC = "MasterToProductiConnection";
	public static final String PSC = "ProductToSystemConnection";
	
	// GroupConnection
	public static final String PGC = "PropertyToGroupConnection";
	public static final String MGC = "MasterRefToGroupConnection";
	public static final String PRGC = "PropertyRefToGroupConnection";
	public static final String GGC = "GroupToGroupConnection";
	
	// workFlow
	public static final String WPC = "WorkFlowToProductRefConnection";
	public static final String EAC = "EndToActionConnection";
	public static final String MAC = "ModelTaskToActionConnection";
	public static final String ATC = "ActionToTaskConnection";
	public static final String TTC = "TaskToTaskConnection";
	public static final String TMC = "TaskToModelTaskConnection";
	public static final String AMC = "ActionToModelTaskConnection";
	public static final String AEC = "ActionToEndConnection";
	public static final String TEC = "TaskToEndConnection";
	public static final String STA = "StartToActionConnection";
	public static final String SMC = "StartToModelTaskConnection";
	public static final String SCRIPT_ENGINE = "groovy";
	public static final String SCRIPTSTRING = "GroovyScript";
	public static final String ORGANIZATION= "Organization";
	public static final String WF_PERMISSIONS= "Permissions";
	
	public static final String FLOWCODE = "flowcode";
	public static final String FLOWSUBCODE = "flowsubcode";
	public static final String APPLY = "apply";
	public static final String APPROVAL = "approval";
	
	// login user information
	public static final String P_USERTABLEID = "user.tableId";
	public static final String P_USERID = "user.userId";
	public static final String P_USERNAME = "user.userName";
	public static final String P_PASSWORD = "user.password";
	public static final String P_SALT = "user.salt";
	public static final String P_ROLES = "user.role";
	public static final String P_ROLEID = "user.role.id";
	public static final String P_ROLER = "user.role.role";
	public static final String P_ROLEDESCRIBE = "user.role.describe";
	public static final String P_ROLEENABLED = "user.role.enabled";
	public static final String P_PERMISSIONS = "user.role.permission";
	public static final String P_PERMISSIONID = "user.role.permission.id";
	public static final String P_PERMISSIONNAME = "user.role.permission.name";
	public static final String P_PERMISSIONP = "user.role.permission.permission";
	
	// Master
	public static final String MASTER = "Master";
	public static final String MASTER_REFERNCE= "MasterReference";
	public static final String MASTER_ITEM= "MasterItem";
	public static final String FIELD_ATTRIBUTES = "FieldAttributes";
	
	public static final String G_WORKFLOW = "WorkFlow";
	public static final String WORKFLOWID = "WorkFlowId";
	public static final String PRODUCTREF = "ProductReference";
	public static final String USERTASK = "UserTask";
	public static final String E_GateWay = "ExclusiveGateway";
	public static final String P_GateWay = "ParallelGateway";
	public static final String START = "Start";
	public static final String END = "End";
	public static final String ACTION = "Action";
	public static final String BACKACTION = "BackAction";
	public static final String CANCELACTION = "CancelAction";
	public static final String CONDITIONS = "Conditions";
	public static final String PARALLELGATEWAY = "ParallelGateway";
	public static final String SCRIPTTASK = "ScriptTask";
	public static final String JAVATASK = "JavaTask";
	public static final String RULETASK = "RuleTask";
	public static final String MAILTASK = "MailTask";

	public static final String EDITOR = "editor";
	public static final String VALIDATION = "validation";
	public static final String OPTIONS = "options";
	public static final String FIELD = "field";
	public static final String INDEX = "index";
	public static final String WIDTH = "Width";
	public static final String LABEL = "label";
	public static final String KAYAMODELID = "kayamodelid";
	public static final String LABELLIST = "labelList";
	public static final String BUSINESSKEYLIST = "businessKeyList";
	public static final String WORKFLOWLIST = "workflowList";
	public static final String BUSINESSSUBKEYLIST = "businessSubKeyList";
	public static final String MENUTREE = "menuTree";
	public static final String COLUMNSLIST = "columnsList";
	public static final String COMPONENTTYPE = "ComponentType";
	public static final String ISSEARCHKEY = "IsSearchKey";

    // login user information
	public static final String USERTABLEID = "emploeeInfo.tableId";
	public static final String USERID = "user.userId";
	public static final String USERNAME = "user.userName";
	public static final String PASSWORD = "user.password";
	public static final String SALT = "user.salt";
	public static final String ROLES = "user.role";
	public static final String ROLEID = "user.role.id";
	public static final String ROLER = "user.role.role";
	public static final String ROLEDESCRIBE = "user.role.describe";
	public static final String ROLEENABLED = "user.role.enabled";
	public static final String PERMISSIONS = "user.role.permission";
	public static final String PERMISSIONID = "user.role.permission.id";
	public static final String PERMISSIONNAME = "user.role.permission.name";
	public static final String PERMISSIONP = "user.role.permission.permission";
	
	// HTTP

	/**
	 * CreateTableType
	 */
	public static final String CREATETABLETYPE="CreateTableType";
	
	/**
	 * Database Type
	 */
	public static final String MYSQL = "MySql";
	public static final String H2 = "H2";
	public static final String POSTGRESQL = "PostgreSql";
	public static final String DB2 = "DB2";
	public static final String ORACLE = "Oracle";
	/**
	 * insert
	 */
	public static final String INSERT="insert";
	/**
	 * select
	 */
	public static final String SELECT="select";
	/**
	 * update
	 */
	public static final String UPDATE="update";
	/**
	 * Delete
	 */
	public static final String DELETE="delete";
	/**
	 * Delete
	 */
	public static final String WORKFLOW="workflow";
	
	// Message
	public static final String MSG_TABLECHECK="Table does not exist.";


    /**
     * shiro采用加密算法
     */
    public static final String HASH_ALGORITHM = "SHA-1";
    /**
     *系统用户默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 生成盐的长度
     */
    public static final int SALT_SIZE = 8;

    /**
     * 生成Hash值的迭代次数
     */
    public static final int HASH_INTERATIONS = 1024;

    /**
     * 验证码
     */
    public static final String VALIDATE_CODE = "validateCode";

    /**
     * fork/join 阈值
     */
    public static final int THRESHOLD = 10;
	
}
