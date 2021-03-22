package com.smartkaya.core;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Vector;
//
//import com.smartkaya.constant.Constant;
//import com.smartkaya.model.KayaMetaModel;

/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public final class AccessKayaWorkFlow {
	//private static ParseKayaModel_XPATH kayaModelAccess= ParseKayaModel_XPATH.getInstance();

//	public AccessKayaWorkFlow() {
//	   kayaModelAccess = ParseKayaModel_XPATH.getInstance();
//		
//	}
//	
//	public static Map<String,String> getDbConnectionMap() {
//		return ParseKayaModel_XPATH.DBConnectionMap;
//	}
//
//	/**
//	 * Get KayaModel All Element
//	 * @param kayaModelId
//	 * @return
//	 */
//	public static List<KayaMetaModel> getChildrenByKayaModelId(String kayaModelId){
//		List<KayaMetaModel> kayaModelList=new Vector<>();
//		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
//			if(entity.getValue().indexOf(kayaModelId)>-1){
//				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
//			}
//		}    
//		return kayaModelList;
//	}
//
//	// Like by GmeModelId
//	/**
//	 * Get All child elements of a Role().
//	 * @param parentId
//	 * @return
//	 */
//	public static List<KayaMetaModel> getKayaModelByParentId(String parentId){
//		List<KayaMetaModel> kayaModelList=new Vector<>();        
//		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
//			if(entity.getValue().equals(parentId)){
//				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
//			}
//		}    
//		return kayaModelList;
//	}
//	
//	// Like by GmeModelId
//	/**
//	 * 取得所有子元素（不包含子Role），但是包含自身Role
//	 * @param parentId
//	 * @return
//	 */
//	public static List<KayaMetaModel> getKayaModelByParentIdNotRole(String parentId){
//		List<KayaMetaModel> kayaModelList=new Vector<>();
//		kayaModelList.add(kayaModelAccess.KayaModelMap.get(parentId));
//		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
//			if(entity.getValue().equals(parentId) && !"Role".equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
//				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
//			}
//		}    
//		return kayaModelList;
//	}
//	
//	/**
//	 * Get All child elements of a Role().
//	 * @param parentId
//	 * @return
//	 */
//	public static List<KayaMetaModel> getKayaModelRolesByParentId(String parentId){
//		List<KayaMetaModel> kayaModelList=new Vector<>();        
//		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
//			if(entity.getValue().equals(parentId) && Constant.G_ROLE.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
//				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
//			}
//		}    
//		return kayaModelList;
//	}
//	
//	/**
//	 * Get All 子孙Role().
//	 * @param parentId
//	 * @return
//	 */
//	public static List<KayaMetaModel> getKayaModelAllRolesByParentId(String parentId){
////		List<KayaMetaModel> kayaModelList=new Vector<>();        
////		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelParentIdMap.entrySet()) {
////			if(entity.getValue().equals(parentId) && Constant.G_ROLE.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
////				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
////			}
////		}    
////		return kayaModelList;
//		
//		
//		List<KayaMetaModel> kayaModelList=new Vector<>();
//		for (Map.Entry<String, String> entity : kayaModelAccess.KayaModelIdOrientationKeyMap.entrySet()) {
//			if(entity.getValue().indexOf(parentId+".id")>-1 && Constant.G_ROLE.equals(kayaModelAccess.KayaModelMap.get(entity.getKey()).getMetaModelType())){
//				kayaModelList.add(kayaModelAccess.KayaModelMap.get(entity.getKey()));
//			}
//		}    
//		return kayaModelList;
//	}
//	
//	/**
//	 * 递归确认是否含有Role项
//	 * @param connections
//	 * @param parentId
//	 * @param orientationStr
//	 * @return orientationKey
//	 */
////	public String getKayaModelRoles(HashMap<String, String> connections, String parentId) {
////		List<KayaModel> kayaModelList=new Vector<>();
////		kayaModelList = getKayaModelRolesByParentId(parentId);
////		if (kayaModelList.size() > 0) {
////			//orientationStr = connections.get(parentId) + "." + orientationStr;
////			return getKayaModelRoles(connections, connections.get(parentId), orientationStr);
////		} else {return orientationStr + ".";}
////	}
//
//	
//
////	/**
////	 * 
////	 * @param keyLike(Contains the role)
////	 * @return
////	 */
////	public List<KayaModel> getRoleByKayaModelId(String kayaModelId){
////		List<KayaModel> kayaModelList= getPropertysByParentId(kayaModelId);
////		// add role
////		//kayaModelList.add(kayaModelAccess.kayaModelMap.get(kayaModelId));
////
////		return kayaModelList;
////	}
//
//	/**
//	 * 
//	 * @param kayaModelId
//	 * @return
//	 */
//	public static KayaMetaModel getKayaModelId(String kayaModelId){
//		return kayaModelAccess.KayaModelMap.get(kayaModelId);
//	}
//	
//	/**
//	 * 
//	 * @param kayaModelId
//	 * @return
//	 */
//	public static KayaMetaModel getParentKayaModel(String kayaModelId){
//		return kayaModelAccess.KayaModelMap.get(kayaModelAccess.KayaModelMap.get(kayaModelId).getParentId());
//	}
//	
//	/**
//	 * 
//	 * @param kayaModelId
//	 * @return
//	 */
//	public static String getParentId(String kayaModelId){	
//		return kayaModelAccess.KayaModelParentIdMap.get(kayaModelId);
//	}
//	/**
//	 * 
//	 * @param kayaModelId
//	 * @return
//	 */
//	public static String getConnec(String kayaModelId){	
//		return kayaModelAccess.KayaModelParentIdMap.get(kayaModelId);
//	}
	
	
	
}
