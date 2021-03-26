package com.smartkaya.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.smartkaya.constant.Constant;
import com.smartkaya.model.KayaMetaModel;
import com.smartkaya.model.KayaModelPermissionsItem;
import com.smartkaya.user.User;


/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public final class AccessKayaModel {
	private static ParseKayaModel_XPATH kayaModelAccess= null;
	public DbConnection dbConnection; 


	public static void ResetKayaModel() {
		//ParseKayaModel_XPATH.resetXPATH();
		kayaModelAccess = ParseKayaModel_XPATH.getInstance();
	}

	public AccessKayaModel() {
		//kayaModelAccess = ParseKayaModel_XPATH.getInstance();
	}
	public AccessKayaModel(String version) {
		kayaModelAccess = ParseKayaModel_XPATH.getInstance(version);
	}
	public static DbConnection getDbConnection(){
		return ParseKayaModel_XPATH.getDbConnection();
	}

	//	public static String getLanguage() {
	//		if (ParseKayaModel_XPATH.DBConnectionInfoMap == null) {
	//			return Constant.EMPTY;
	//		} else {
	//			ParseKayaModel_XPATH.DBConnectionInfoMap.get(Constant.LANGUAGE);
	//		}
	//		//return ParseKayaModel_XPATH.DBConnectionInfoMap;
	//	}

	/**
	 * Get KayaModel All Element
	 * @param kayaModelId
	 * @return
	 */
	public static List<KayaMetaModel> getChildrenByKayaModelId(String kayaModelId){
		List<KayaMetaModel> kayaModelList=new Vector<>();
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
			if(entity.getValue().indexOf(kayaModelId)>-1){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	/**
	 * Get KayaModel All Element
	 * @param kayaModelId
	 * @return
	 */
	public static List<KayaMetaModel> getWorkFlowItemByKayaModelId(String kayaModelId){
		List<KayaMetaModel> kayaModelList=new Vector<>();
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
			if(entity.getValue().indexOf(kayaModelId)>-1 && !Constant.USERTASK.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}



	/**
	 * Get KayaModel All Element
	 * @param kayaModelId
	 * @return
	 */
	public static List<KayaMetaModel> getWorkFlowItemByKayaModelId(String kayaModelId,List<String> indexNoSet ){
		List<KayaMetaModel> kayaModelList=new Vector<>();
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
			if(entity.getValue().indexOf(kayaModelId)>-1 
					&& !Constant.USERTASK.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())
					&& indexNoSet.contains(kayaModelAccess.KayaModelMap.get(entity.getKey()).get(Constant.INDEXNO))){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}
	/**
	 * Get KayaModel All Element
	 * @param kayaModelId
	 * @return
	 */
	public static List<KayaMetaModel> getWorkFlowItem(String kayaModelId,List<String> indexNoSet,String wfType){
		List<KayaMetaModel> kayaModelList=new Vector<>();
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
			if(entity.getValue().indexOf(kayaModelId)>-1 
					&& !Constant.USERTASK.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())
					&& indexNoSet.contains(kayaModelAccess.KayaModelMap.get(entity.getKey()).get(Constant.INDEXNO))){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}
	/**
	 * Get KayaModel All Element
	 * @param kayaModelId
	 * @return
	 */
	public static List<KayaMetaModel> getExclusiveGatewayChildrenByKayaModelId(String kayaModelId){
		List<KayaMetaModel> kayaModelList=new Vector<>();
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
			if(entity.getValue().indexOf(kayaModelId)>-1 && !Constant.G_EXCLUSIVEGATEWAY.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	/**
	 * Get All child elements of a Role().
	 * @param parentId
	 * @return
	 */
	public static List<KayaMetaModel> getKayaModelByParentId(String parentId){
		List<KayaMetaModel> kayaModelList=new Vector<>();        
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
			if(entity.getValue().equals(parentId)){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	/**
	 * Get All child elements of a Role.
	 * @param parentId
	 * @return
	 */
	public static List<KayaMetaModel> getKayaModelByParentIdNoAction(String parentId){
		List<KayaMetaModel> kayaModelList=new Vector<>();        
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
			if(entity.getValue().equals(parentId) 
					&& !Constant.ACTION.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())
					&& !Constant.G_GROUP.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	/**
	 * Get All child elements of a Role(for Group).
	 * @param parentId
	 * @return
	 */
	public static List<KayaMetaModel> getKayaModelByParentIdNoAction_UI(String parentId){
		List<KayaMetaModel> kayaModelList=new Vector<>();        
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
			if(entity.getValue().equals(parentId)
					&& kayaModelAccess.KayaModelMap.get(entity.getKey()).getGroupId()==null
					&& !Constant.ACTION.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){


				if (Constant.G_GROUP.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())) {
					kayaModelAccess.KayaModelMap.get(entity.getKey()).getAttributesMap().put(Constant.G_GROUP, getGroupItems(entity.getKey()));
				}
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	// Like by GmeModelId
	/**
	 * Gets all child elements (without child roles), but with their own roles
	 * @param parentId
	 * @return
	 */
	public static List<KayaMetaModel> getKayaModelByParentIdNotRole(String parentId){
		List<KayaMetaModel> kayaModelList=new Vector<>();

		kayaModelList.add(kayaModelAccess.KayaModelMap.get(parentId));
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
			if(entity.getValue().equals(parentId) && !Constant.G_ROLE.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())
					&& !Constant.G_GROUP.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())
					&& !Constant.ACTION.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}
	/**
	 * Gets all child elements (without child roles), but with their own roles(for Group)
	 * @param parentId
	 * @return
	 */
	public static List<KayaMetaModel> getKayaModelByParentIdNotRole_UI(String parentId){
		List<KayaMetaModel> kayaModelList=new Vector<>();

		kayaModelList.add(kayaModelAccess.KayaModelMap.get(parentId));
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
			if(entity.getValue().equals(parentId) && !Constant.G_ROLE.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())
					&& kayaModelAccess.KayaModelMap.get(entity.getKey()).getGroupId()==null
					&& !Constant.ACTION.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	/**
	 * Gets all Action elements (without child roles), but with their own roles
	 * @param parentId
	 * @return
	 */
	public static List<KayaMetaModel> getKayaModelByParentIdActions(String parentId){
		List<KayaMetaModel> kayaModelList=new Vector<>();

		kayaModelList.add(kayaModelAccess.KayaModelMap.get(parentId));
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
			if(entity.getValue().equals(parentId) && !Constant.ACTION.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	/**
	 * Gets all child element information
	 * @param parentId
	 * @return
	 */
	public static List<KayaMetaModel> getKayaModelRolesByParentId(String parentId){
		List<KayaMetaModel> kayaModelList=new Vector<>();        
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
			if(entity.getValue().equals(parentId) && Constant.G_ROLE.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	/**
	 * Gets all of the business flow child elements (not including child roles), but including their own roles
	 * @param parentId
	 * @return
	 */
	public static List<KayaMetaModel> getKayaWorkFlowRole(String childKayaModelId){
		List<KayaMetaModel> kayaModelList=new Vector<>();
		// 添加元素本身
		kayaModelList.add(kayaModelAccess.KayaModelMap.get(childKayaModelId));
		// 只添加子属性元素
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
			if(entity.getValue().equals(kayaModelAccess.KayaModelMap.get(childKayaModelId).getParentId()) 
					&& Constant.G_PROPERTY.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	/**
	 * Gets all of the business flow child elements (not including child roles), but including their own roles
	 * @param parentId
	 * @return
	 */
	public static List<KayaMetaModel> getKayaWorkFlowUI(String wfKayaModelId){
		List<KayaMetaModel> kayaModelList=new Vector<>();
		// 添加元素本身
		kayaModelList.add(kayaModelAccess.KayaModelMap.get(wfKayaModelId));
		// Colspan
		int colspan = 0;
		// 只添加子属性元素
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
			if(wfKayaModelId.equals(entity.getValue()) 
					&& Constant.USERTASK.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));

				colspan++;
			}
		}
		kayaModelAccess.KayaModelMap.get(wfKayaModelId).getAttributesMap().put(Constant.COLSPAN, colspan);
		return kayaModelList;
	}

	/**
	 * Get all the business process Action elements (without child roles), but with their own roles
	 * @param parentId
	 * @return
	 */
	public static List<KayaMetaModel> getKayaWorkFlowAction(String actionId){
		List<KayaMetaModel> kayaModelList=new Vector<>();
		// 添加元素本身
		kayaModelList.add(kayaModelAccess.KayaModelMap.get(actionId));
		// 只添加子属性元素(不包含Action)
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
			if(entity.getValue().equals(kayaModelAccess.KayaModelMap.get(actionId).getParentId()) 
					&& Constant.G_PROPERTY.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	/**
	 * Get All Role.
	 * @param parentId
	 * @return
	 */
	public static List<KayaMetaModel> getKayaModelAllRolesByParentId(String parentId){
		List<KayaMetaModel> kayaModelList=new Vector<>();
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
			if(entity.getValue().indexOf(parentId+".id")>-1 && Constant.G_ROLE.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	/**
	 * Get all model information for the Group
	 * @param kayaGroupId
	 * @return
	 */
	public List<KayaMetaModel> getGouprModel(String kayaGroupId) {
		List<KayaMetaModel> kayaModelList=new Vector<>();

		for (Map.Entry<String, String> entity : kayaModelAccess.kayaGroupConnectionsMap.entrySet()) {
			if(entity.getValue().equals(kayaGroupId)){
				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}    
		return kayaModelList;
	}

	/**
	 * Self Model acquisition method
	 * @param kayaModelId
	 * @return
	 */
	public static KayaMetaModel getKayaModelId(String kayaModelId){
		return kayaModelAccess.KayaModelMap.get(kayaModelId);
	}


	/**
	 * The superclass Model retrieves the method
	 * @param kayaModelId 元素自身ID
	 * @return
	 */
	public static KayaMetaModel getParentKayaModel(String kayaModelId){
		return kayaModelAccess.KayaModelMap.get(kayaModelAccess.KayaModelMap.get(kayaModelId).getParentId());
	}

	/**
	 * The parent class ID gets the method
	 * @param kayaModelId
	 * @return
	 */
	public static String getParentId(String kayaModelId){	
		return kayaModelAccess.KayaModelParentIdMap.get(kayaModelId);
	}


	/**
	 * Get the action object
	 * @param kayaModelId
	 * @return
	 */
	public static String getWorkFlowConnectionDes(String kayaModelId) {

		// 取得撤销动作对象
		if (kayaModelAccess.WorkFlowConnectionMap.get(kayaModelId).isReverse()) {
			return kayaModelAccess.KayaModelMap.get(kayaModelId).getParentId();
			// 顺序动作对象
		} else {
			return kayaModelAccess.ConnectionMap.get(kayaModelId);
		}
	}

	/**
	 * Gets the information for all former userTasks of UserTask
	 * @param userTaskId
	 * @return
	 */
	public static List<KayaMetaModel> getSrcUserTask(String userTaskId) {
		List<KayaMetaModel> kayaModelList=new Vector<>();
		// UserTask自身Connection
		for (Map.Entry<String, WorkFlowConnection> entity : kayaModelAccess.WorkFlowConnectionMap.entrySet()) {
			if (kayaModelAccess.WorkFlowConnectionMap.get(entity.getKey()).isReverse()) {
				if (kayaModelAccess.KayaModelParentIdMap.get(entity.getKey()).equals(userTaskId)) {
					kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getValue().getKayaModelId()));
				}
			} else {
				if (userTaskId.equals(entity.getValue().getKayaModelId())) {

					kayaModelList.add(kayaModelAccess.KayaModelMap.get(kayaModelAccess.KayaModelMap.get(entity.getKey()).getParentId()));
				} 
			}

		}

		return kayaModelList;
	}

	/**
	 * Gets the action object element
	 * @param kayaModelId  ActionId
	 * @return
	 */
	public static String getWorkFlowConnectionSrc(String kayaModelId) {

		// 取得撤销动作对象
		if (kayaModelAccess.WorkFlowConnectionMap.get(kayaModelId).isReverse()) {
			return kayaModelAccess.WorkFlowConnectionMap.get(kayaModelId).getKayaModelId();
			// 顺序动作对象
		} else {
			return kayaModelAccess.KayaModelMap.get(kayaModelId).getParentId();
		}
	}

	/**
	 * Table exists confirmation method
	 * @param kayaModelId TableID
	 * @return
	 */
	public static boolean checkTable(String kayaModelId) {
		KayaMetaModel model = AccessKayaModel.getKayaModelId(kayaModelId);

		if (model == null) {
			return false;
		} else {
			kayaModelId = model.getTableId();
		}
		return kayaModelAccess.KayaModelTables.contains(kayaModelId);
	}


	/**
	 * Gets the default language for the system
	 * @return
	 */
	public static String getLanguage() {
		if (kayaModelAccess == null) {
			return Constant.EMPTY;
		} else {
			return kayaModelAccess.Language;
		}

	}

	public static String getKayaBaseBusinessName() {
		if (kayaModelAccess == null) {
			return Constant.EMPTY;
		} else {
			return ParseKayaModel_XPATH.kayaBaseBusiness;
		}
	}
	
	/**
	 * 取得参照元的KayaModelId
	 * @param kayaModelId
	 * @param FilesStr
	 * @return
	 */
	public static String isRef(String kayaModelId,String FilesStr) {
		String kindkeyRet = null;
		// 从元模型中取出表信息
		List<KayaMetaModel> kayamodelList = AccessKayaModel.getKayaModelByParentIdNoAction(kayaModelId);
		for (KayaMetaModel kayamodel : kayamodelList) {
			if(kayamodel.get(Constant.KINDKEY).equals(FilesStr)){
				if (Constant.PROPERTYREF.equals(kayamodel.getMetaModelType())) {
					kindkeyRet = AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED)).get(Constant.KINDKEY);
				} else {
					kindkeyRet = FilesStr;
				}
			}

		}

		return kindkeyRet;
	}

	/**
	 * Get the Menu tree structure information (for LayUI)
	 * @return
	 */
	public static List<Map<String, Object>> getMenuTree() {
		List<Map<String, Object>> menuTreeList = new ArrayList<Map<String,Object>>();
		if (kayaModelAccess != null) {
			Map<String,Object> menuTree = new HashMap<String,Object>();
			String KaYaDiagramId = kayaModelAccess.KaYaDiagramId;

			for (Map.Entry<String, String> entity : kayaModelAccess.ConnectionMap.entrySet()) {
				if(KaYaDiagramId.equals(entity.getValue())){
					menuTree.put("id", entity.getKey());
					menuTree.put("text",getKayaModelId(entity.getKey()).getName());
					menuTree.put("kindtype",getKayaModelId(entity.getKey()).getMetaModelType());
					menuTree.put("wftype","");
					menuTree.put("attributes",getKayaModelId(entity.getKey()).getAttributesMap());
					if (Constant.G_PRODUCT.equals(getKayaModelId(entity.getKey()).getMetaModelType())) {
						getMenu(entity.getKey(),menuTree);
					}


				}
			}
			menuTreeList.add(menuTree);


			for (Map.Entry<String, String> entity : kayaModelAccess.ProductRefMap.entrySet()) {
				Map<String,Object> menuTreeWorkFlow = new HashMap<String,Object>();
				menuTreeWorkFlow.put("id", entity.getKey());
				menuTreeWorkFlow.put("text",getKayaModelId(entity.getKey()).getName() + "(申请)");
				menuTreeWorkFlow.put("kindtype",getKayaModelId(entity.getKey()).getMetaModelType());
				menuTreeWorkFlow.put("wftype",Constant.APPLY);
				menuTreeWorkFlow.put("attributes",getKayaModelId(entity.getKey()).getAttributesMap());

				getMenuWorkflow_html5(entity.getKey(),menuTreeWorkFlow,Constant.APPLY);
				menuTreeList.add(menuTreeWorkFlow);
			}

			// 审批者菜单根据权限动态加载
			for (Map.Entry<String, String> entity : kayaModelAccess.ProductRefMap.entrySet()) {
				Map<String,Object> menuTreeWorkFlow = new HashMap<String,Object>();
				menuTreeWorkFlow.put("id", entity.getKey());
				menuTreeWorkFlow.put("text",getKayaModelId(entity.getKey()).getName() + "(审批)");
				menuTreeWorkFlow.put("kindtype",getKayaModelId(entity.getKey()).getMetaModelType());
				menuTreeWorkFlow.put("wftype",Constant.APPROVAL);
				menuTreeWorkFlow.put("attributes",getKayaModelId(entity.getKey()).getAttributesMap());

				getMenuWorkflow_html5(entity.getKey(),menuTreeWorkFlow,Constant.APPROVAL);
				menuTreeList.add(menuTreeWorkFlow);
			}

		}


		return menuTreeList;
	}

	/**
	 * Get the Menu item for the Menu (LayUI)
	 * @param kayaModelId
	 * @param menuTree
	 * @return
	 */
	private static void getMenu(String kayaModelId,Map<String,Object> menuTree) {
		List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
		for (Map.Entry<String, String> entity : kayaModelAccess.ConnectionMap.entrySet()) {
			if(kayaModelId.equals(entity.getValue()) 
					&& Constant.G_PRODUCT.equals(getKayaModelId(entity.getValue()).getMetaModelType())
					&& !Constant.MASTER.equals(getKayaModelId(entity.getKey()).getMetaModelType())){
				Map<String,Object> childrenMenuTree = new HashMap<String,Object>();
				childrenMenuTree.put("id",entity.getKey());
				childrenMenuTree.put("text",getKayaModelId(entity.getKey()).getName());
				childrenMenuTree.put("kindtype",getKayaModelId(entity.getKey()).getMetaModelType());
				childrenMenuTree.put("wftype","");
				if (Constant.G_ROLE.equals(getKayaModelId(entity.getKey()).getMetaModelType())){
					childrenMenuTree.put("iconCls", Constant.G_ROLE);
				}
				childrenMenuTree.put("state","closed");
				childrenMenuTree.put("attributes",getKayaModelId(entity.getKey()).getAttributesMap());
				children.add(childrenMenuTree);
				if (!Constant.G_ROLE.equals(getKayaModelId(entity.getKey()).getMetaModelType())) {
					getMenu(entity.getKey(),childrenMenuTree);
				}

			}
		} 
		if (children.size() > 0) {
			menuTree.put("children",children);
			menuTree.put("state","closed");
		} else {
			menuTree.put("state","open");
		}
	}

	/*
	 * Get the Workflow's Menu for the Menu
	 * @param kayaModelId
	 * @param menuTree
	 * @return
	 */
	private static void getMenuWorkflow_html5(String kayaModelId,Map<String,Object> menuTree,String wfType) {
		List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
		for (Map.Entry<String, String> entity : kayaModelAccess.WorkFlowItems.entrySet()) {

			// 菜单权限验证
			if(kayaModelId.equals(getKayaModelId(entity.getValue()).getParentId())){
				Map<String,Object> childrenMenuTree = new HashMap<String,Object>();
				childrenMenuTree.put("id",entity.getKey());
				childrenMenuTree.put("text",getKayaModelId(entity.getKey()).getName());
				childrenMenuTree.put("kindtype",getKayaModelId(entity.getKey()).getMetaModelType());
				childrenMenuTree.put("wftype",wfType);
				childrenMenuTree.put("state","closed");
				childrenMenuTree.put("attributes",getKayaModelId(entity.getKey()).getAttributesMap());
				children.add(childrenMenuTree);
			}
		} 
		if (children.size() > 0) {
			menuTree.put("children",children);
			menuTree.put("state","closed");
		} else {
			menuTree.put("state","open");
		}

		//return menuTree;
	}


	/*
	 * Get the Group Items
	 * @param groupId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<KayaMetaModel> getGroupItems(String groupId) {
		List<KayaMetaModel> kayaModelList=new Vector<>();
		for (String groupItem : (List<String>)kayaModelAccess.KayaModelMap.get(groupId).getAttributesMap().get(Constant.GROUP_ITEMS)) {
			if(kayaModelAccess.KayaModelMap.get(groupItem).getMetaModelType().equals(Constant.G_GROUP)){
				kayaModelAccess.KayaModelMap.get(groupItem).getAttributesMap().put(Constant.G_GROUP, getGroupItems(groupItem));
			}
			kayaModelList.add(kayaModelAccess.KayaModelMap.get(groupItem));
		}
		return kayaModelList;
	}

	/**
	 * Get WF Start Actions And Property
	 * @param workFlowId
	 * @param type(add,edit,review)
	 * @return
	 */
	public static List<KayaMetaModel> getWorkFlowItemByStartKayaModelId(String workFlowId,User user){

		// Start 指向的Action 

		KayaMetaModel kayaMetaWorkFlowModel = AccessKayaModel.getKayaModelId(workFlowId);
		String startUserTaskId = kayaModelAccess.KayaModelMap.get(AccessKayaModel
				.getWorkFlowConnectionDes(kayaMetaWorkFlowModel.get(Constant.START))).getParentId();

		List<KayaMetaModel> kayaModelList=new Vector<>();

		// Start Action(Start 指向的Action)
		kayaModelList.add(kayaModelAccess.KayaModelMap.get(kayaModelAccess.ConnectionMap.get(kayaMetaWorkFlowModel.get(Constant.START))));
		if (!checkUserPermission(user,AccessKayaModel.getKayaModelId(startUserTaskId)).contains(false)) {
			for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
				if(entity.getValue().indexOf(startUserTaskId)>-1 && !Constant.USERTASK.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
					// 指向自己的Action
					if (Constant.ACTION.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType()) && kayaModelAccess.WorkFlowConnectionMap.get(entity.getKey()).getKayaModelId().equals(startUserTaskId)) {
						kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
						// 所有的Property
					} else if (Constant.G_PROPERTY.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())) {
						kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
					}

				}
			}    
		}
		
		return kayaModelList;
	}

	/**
	 * Get KayaModel All Element
	 * @param kayaModelId
	 * @return
	 */
	public static List<KayaMetaModel> getWorkFlowItemByNextKayaModelId(String workFlowId,User user,Map<String,Object> rowData){
		List<KayaMetaModel> kayaModelList=new Vector<>();
		KayaMetaModel kayaMetaWorkFlowModel = AccessKayaModel.getKayaModelId(workFlowId);
		String startUserTaskId = kayaModelAccess.KayaModelMap.get(AccessKayaModel
				.getWorkFlowConnectionDes(kayaMetaWorkFlowModel.get(Constant.START))).getParentId();

		// 申请者

		if ("Pending".equals(rowData.get(AccessKayaModel.getKayaModelId(startUserTaskId).get(Constant.KINDKEY)))) {
			kayaModelList = getWorkFlowItemByStartKayaModelId(workFlowId,user);
		} else {
			if (!checkUserPermission(user,AccessKayaModel.getKayaModelId(startUserTaskId)).contains(false)) {
				for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
					if(entity.getValue().indexOf(startUserTaskId)>-1 && !Constant.USERTASK.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
						// 指向自己的Action
						// 指向自己的Action
						if (Constant.ACTION.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())
								&& kayaModelAccess.WorkFlowConnectionMap.get(entity.getKey()).isReverse()
								&& "Pending".equals(rowData.get(kayaModelAccess.KayaModelMap.get(kayaModelAccess.WorkFlowConnectionMap.get(entity.getKey()).getKayaModelId()).get(Constant.KINDKEY)))) {
							kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
							// 所有的Property
						} else if (Constant.G_PROPERTY.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())) {
							kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
						}

					}
				}    
			}
			
		}



		// 审批者
		// 等于自身的User Task的场合




		return kayaModelList;
	}
	/**
	 * 通过User权限获取User Task
	 * @param workFlowId
	 * @param user
	 * @return kayaMetaModel List
	 */
	public static List<KayaMetaModel> chekPermission(String workFlowId, User user, Map<String,Object> rowData) {

		List<KayaMetaModel> userTaskList = new ArrayList<KayaMetaModel>();
		KayaMetaModel kayaMetaWorkFlowModel = AccessKayaModel.getKayaModelId(workFlowId);
		String startUserTaskId = kayaModelAccess.KayaModelMap.get(AccessKayaModel
				.getWorkFlowConnectionDes(kayaMetaWorkFlowModel.get(Constant.START))).getParentId();
		
		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
			// 获得指定WorkFlowId的所有UserTask(非申请者)
			if(entity.getValue().equals(workFlowId) 
					&& Constant.USERTASK.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())
					&& !startUserTaskId.equals(entity.getKey())){
				userTaskList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
			}
		}
		// 是否是开始User Task
		List<KayaMetaModel> actionList = new ArrayList<KayaMetaModel>();

		for (KayaMetaModel metamodel:userTaskList) {
			// 如果匹配的场合1(Pending的是自身User TaskId)
			//if (!checkSet.contains(false) && "Pending".equals(rowData.get(metamodel.get(Constant.KINDKEY)))) {
			// 具有权限  + UserTask 是自己所属
			if (!checkUserPermission(user,metamodel).contains(false) && "Pending".equals(rowData.get(metamodel.get(Constant.KINDKEY)))) {
				for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
					if(entity.getValue().indexOf(metamodel.getGmeId())>-1 && !Constant.USERTASK.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
						// 指向自己的Action
						if (Constant.ACTION.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())
								&& !kayaModelAccess.WorkFlowConnectionMap.get(entity.getKey()).isReverse() && "Pending".equals(rowData.get(metamodel.get(Constant.KINDKEY)))) {
							actionList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
						}else if (Constant.G_PROPERTY.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType()) && "Pending".equals(rowData.get(metamodel.get(Constant.KINDKEY)))) {
							actionList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
						}
					}
				} 
				// 具有权限 + 具有撤销权限
			} else if (!checkUserPermission(user,metamodel).contains(false)) {
				for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
					if(entity.getValue().indexOf(metamodel.getGmeId())>-1 && !Constant.USERTASK.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
						// 指向自己的Action
						if (Constant.ACTION.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())
								&& kayaModelAccess.WorkFlowConnectionMap.get(entity.getKey()).isReverse()
								&& "Pending".equals(rowData.get(kayaModelAccess.KayaModelMap.get(kayaModelAccess.WorkFlowConnectionMap.get(entity.getKey()).getKayaModelId()).get(Constant.KINDKEY)))) {
							actionList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
							// 所有的Property
						} else if (Constant.G_PROPERTY.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())) {
							actionList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
						}
					}
				} 
			}
			
		}
		return actionList;
	}

	@SuppressWarnings("unchecked")
	private static Set<Boolean> checkUserPermission(User user,KayaMetaModel kayaMetaModel) {
		Set<Boolean> checkSet = new HashSet<Boolean>();
		for (KayaModelPermissionsItem kp : kayaMetaModel.getPermissionsItems()) {
			// 用户信息同MetaModel中的信息进行匹配
			// 用户信息中存在模型设定信息的场合
			if (user.getUserMap().containsKey(kp.getId())) {
				// List(复数项)的场合
				if (Constant.EMPTY.equals(kp.getText())) {
					checkSet.add(((List<String>) user.getUserMap().get(kp.getId())).containsAll(kp.getTextList()));
					// 单项的场合
				} else {
					checkSet.add(user.getUserMap().get(kp.getId()).equals(kp.getText()));
				}
				// 不存在的场合
			} else {
				checkSet.add(false);
				// 如果不匹配，短路终止循环
				break;
			}
		}
		return checkSet;
	}
	/**
	 * 通过User权限获取User Task
	 * @param workFlowId
	 * @param user
	 * @return kayaMetaModel List
	 */
	public static List<KayaMetaModel> getActions(String taskStetus, String userTaskId) {
		List<KayaMetaModel> actionList = new ArrayList<KayaMetaModel>();
		return actionList;
	}

}
