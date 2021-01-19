package com.smartkaya.api.utils;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.smartkaya.constant.ConditionEnum;

/**
 * StringUtils Class
 * 
 * @author LiangChen 2018/4/27
 * @version 1.0.0
 */
public class StringUtil extends StringUtils{

	/**
	 * Get OrientationKey
	 * 
	 * @param connections
	 * @param parentId
	 * @param orientationStr
	 * @return orientationKey
	 */
	public static String getOrientationKey(HashMap<String, String> connections, String parentId,
			String orientationStr) {
		if (null != connections.get(parentId)) {
			orientationStr = connections.get(parentId) + "." + orientationStr;
			return getOrientationKey(connections, connections.get(parentId), orientationStr);
		} else {
			return orientationStr + ".";
		}
	}

	public static String getInsertSqlStr() {
		return "";
	}

	/**
	 * 校验是否是In操作
	 * 
	 * @param values
	 * @return boolean
	 */
	public static String getCondition(String values) {
//		String pattern = "(([\'|\"]{0,1}[0-9a-zA-Z]*[\'|\"]{0,1})[,])*([\'|\"]{1}[0-9a-zA-Z]*[\'|\"]{1})";
//		return Pattern.matches(pattern, values);
		values = defaultIfEmpty(values, EMPTY);
		
		if(values.contains(",")) {
			return ConditionEnum.IN.getCode();
		}else if(values.contains("～")) {
			return ConditionEnum.DATE.getCode();
		}
		return ConditionEnum.LIKE.getCode();
	}

	// public static Map<String,String> getMasterItem(String kayaModelId) {
	// Map<String,String> masterItem = new HashMap<String,String>();
	// String KaYaDiagramId = KayaModelAccess.get;
	// for (Map.Entry<String, String> entity :
	// kayaModelAccess.ConnectionMap.entrySet()) {
	// if(KaYaDiagramId.equals(entity.getValue())){
	// masterItem.put("id", entity.getKey());
	// masterItem.put("text",getKayaModelId(entity.getKey()).getName());
	// }
	// }
	// return masterItem;
	// }
}
