package com.smartkaya.bean;
import java.util.Map;
/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public class Mapping implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 更新对象Map
	 */
	private Map<String,Object> propertys;
	/**
	 * 更新对象主键
	 */
	private Map<String,Object> keys;

	public Map<String, Object> getPropertys() {
		return propertys;
	}
	public void setPropertys(Map<String, Object> propertys) {
		this.propertys = propertys;
	}
	public void setProperty(String key,Object value) {
		this.propertys.put(key, value);
	}
	public Object getProperty(String key) {
		return this.propertys.get(key);
	}
	public Map<String, Object> getKeys() {
		return keys;
	}
	public void setKeys(Map<String, Object> bisinessKeys) {
		this.keys = bisinessKeys;
	}
	public Object getKey(String key) {
		return this.keys.get(key);
	}
	public void setKey(String key,Object value) {
		this.keys.put(key, value);
	}
}
