package com.smartkaya.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smartkaya.bean.GridColumn;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.utils.UtilTools;

/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public class KayaMetaModel implements java.io.Serializable,Comparable<Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String guid;
	private String metaModelType;
	private String gmeId;
	private String name;
	private String parentId;
	private String orientationKey;
	private Map<String,Object> attributesMap;
	private List<String> businessKeys;
	private String tableId;
	private String message;
	private String groupId;
	private boolean uniqueKey;

	
	public String getWorkFlowId() {
		return UtilTools.isEmpty(attributesMap.get(Constant.WORKFLOWID)) == true? Constant.EMPTY:attributesMap.get(Constant.WORKFLOWID).toString();
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public List<String> getBusinessKeys() {
		return businessKeys;
	}
	public void setBusinessKeys(List<String> businessKeys) {
		this.businessKeys = businessKeys;
	}
	public String getMetaModelType() {
		return metaModelType;
	}
	public void setMetaModelType(String metaModelType) {
		this.metaModelType = metaModelType;
	}
	public String getGmeId() {
		return gmeId;
	}
	public void setGmeId(String gmeId) {
		this.gmeId = gmeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getOrientationKey() {
		return orientationKey;
	}
	public void setOrientationKey(String orientationKey) {
		this.orientationKey = orientationKey;
	}

	
	// 任意属性取得(MasterItems除外)
	public String get(String key){

		return attributesMap.get(key) == null?"":attributesMap.get(key).toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getOrganizationItems(){
		return ((List<String>)attributesMap.get(Constant.ORGANIZATION));
	}
	@SuppressWarnings("unchecked")
	public List<KayaModelPermissionsItem> getPermissionsItems(){
		return ((List<KayaModelPermissionsItem>)attributesMap.get(Constant.WF_PERMISSIONS));
	}
	@SuppressWarnings("unchecked")
	public List<KayaModelMasterItem> getMasterItems(){
		return ((List<KayaModelMasterItem>)attributesMap.get(Constant.FIELD_ATTRIBUTES));
	}
	
	@SuppressWarnings("unchecked")
	public List<KayaMetaModel> getGroupItems(){
		List<KayaMetaModel> kayaMetaModel = new ArrayList<KayaMetaModel>();
		for (String groupItem:(List<String>)attributesMap.get(Constant.GROUP_ITEMS)) {
			kayaMetaModel.add(AccessKayaModel.getKayaModelId(groupItem));
		}

		return kayaMetaModel;
	}
	public int getLevel() {
		return (int) attributesMap.get(Constant.GROUP_LEVEL);
	}
	public int getRowspan() {
		return (int) attributesMap.get(Constant.ROWSPAN);
	}
	public int getColspan() {
		return (int) attributesMap.get(Constant.COLSPAN);
	}
	
	public Map<String, Object> getAttributesMap() {
		return attributesMap;
	}
	public void setAttributesMap(Map<String, Object> attributesMap) {
		this.attributesMap = attributesMap;
	}
	public void put(String key, Object value) {
		this.attributesMap.put(key, value);
	}
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(boolean uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	public String toString() {
		return 	"\n" + "kayaModelId:[" + this.gmeId + "],"
				+ "kindType:[" + this.getMetaModelType() + "],"
				+ "kind:[" + this.get("KindKey") + "],"
				+ "parentId:[" + this.parentId + "],"
				+ "groupId:[" + this.groupId + "],"
				+ "tableId:[" + this.tableId + "],"
				+ "name:[" + this.name + "],"
				+ "businessKeys:[" + getBusinessKeysString(this.businessKeys) + "],"
				+ "\n" + "attributesMap:[" + getAttributesMapString(this.attributesMap) + "],"
				+ "\n" + "orientationKey:[" + this.orientationKey + "]";
	}
	
	private String getBusinessKeysString(List<String> businessList) {
		StringBuffer strBuf = new StringBuffer("");
		if (businessList != null) {
			for (String str:businessList) {
				strBuf.append("[" + str + "]");
			}
		}

		return strBuf.toString();
	}

	private String getAttributesMapString(Map<String, Object> attributesMap) {
		StringBuffer strBuf = new StringBuffer("");
		for(String tempStr:attributesMap.keySet()) {
			
			strBuf.append("[" + tempStr + ":" + attributesMap.get(tempStr) + "]");
		}

		return strBuf.toString();
	}
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		GridColumn column = (GridColumn) o;  
		Integer v1 = Integer.valueOf(column.getIndex());
		
		Integer v2 = Integer.valueOf(this.get(Constant.INDEXNO));
        if(v1 != null){
            return v2.compareTo(v1);
        } 
        return 0; // Index比较大小，用于默认排序  
	}  

}
