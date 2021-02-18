package com.smartkaya.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.smartkaya.user.User;

/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public class Paramater implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 更新对象RoleID
	 */
	private List<String> targetTableList = new ArrayList<String>();

	/** 更新对象RoleID */
	private String id;
	/** 任意文字列（用于全文检索） */
	private String text;
	/** 流程ActionID*/
	private String actionid;
	/** 则删改查标识*/
	private String crud;
	/** 检索，删除类型*/
	private Type type;
	/** 主键信息*/
	private HashMap<String,Object> businessKeyMap;
	/** 返回信息（错误信息等）*/
	private List<Message> messages = new ArrayList<Message>();
	/** 模型（对象）属性信息*/
	private HashMap<String,Object> propertys;
	/** 联合主键信息（判断是否主键重复）*/
	private Set<String> orientationKeySet;
	/** 错误状态(true:有错误  )*/
	private boolean error;
	/** 联合主键信息（整形加工后）*/
	private String orientationKey;
	/** login User信息*/
	private User usrinfo;
	
	
	public String getId() {
		return id;
	}
	public void setId(String kayaModelId) {
		this.id = kayaModelId;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String fullText) {
		this.text = fullText;
	}

	public User getUsrinfo() {
		return usrinfo;
	}
	public void setUsrinfo(User usrinfo) {
		this.usrinfo = usrinfo;
	}
	

	public String getOrientationKey() {
		return orientationKey;
	}
	public void setOrientationKey(String orientationKey) {
		this.orientationKey = orientationKey;
	}
	public void setActionid(String actionid) {
		this.actionid = actionid;
	}
	public String getActionid() {
		return actionid;
	}
	public String getCrud() {
		return crud;
	}
	public void setCrud(String crud) {
		this.crud = crud;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public HashMap<String, Object> getBusinessKeyMap() {
		return businessKeyMap;
	}
	public void setBusinessKeyMap(HashMap<String, Object> businessKeyMap) {
		this.businessKeyMap = businessKeyMap;
	}
	public String messagesToString() {
		StringBuilder messageText = new StringBuilder("");
		boolean flg = true;
		for (Message message:messages) {
			if (flg) {
				messageText.append("Lever:" + message.getLever() + " CODE:" + message.getCode() + " Message:" + message.getMesage());
				flg = false;
			} else {
				// 依赖系统的换行符（Java 1.7以上）
				messageText.append(System.lineSeparator());
				messageText.append("Lever:" + message.getLever() + " CODE:" + message.getCode() + " Message:" + message.getMesage());
			}
		}
		
		return messageText.toString();
	}
	
	public void setMessages(Message message) {
		this.messages.add(message);
	}

	public HashMap<String, Object> getPropertys() {
		return propertys;
	}
	public void setPropertys(HashMap<String, Object> propertys) {
		this.propertys = propertys;
	}
	
	public Set<String> getOrientationKeySet() {
		return orientationKeySet;
	}
	public void setOrientationKeySet(HashSet<String> orientationKeySet) {
		this.orientationKeySet = orientationKeySet;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public List<String> getTargetTableList() {
		return targetTableList;
	}
	public void setTargetTableList(List<String> targetTableList) {
		this.targetTableList = targetTableList;
	}
}
