package com.smartkaya.bean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartkaya.user.SysUser;
/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Paramaters implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;// RoleID
	private String text;
	private String crud;
	private String servicename;
	private HashMap<String,Object> businessKeyMap;
	private String actionid;
	private List<String> targetTableList = new ArrayList<String>();
	private List<Message> messages = new ArrayList<Message>();
	private boolean error;
	private List<HashMap<String,Object>> listPropertys = new ArrayList<HashMap<String,Object>>();
	private String orientationKey;
	private SysUser usrinfo;
	
	public String getId() {
		return id;
	}
	public void setId(String objectId) {
		this.id = objectId;
	}

	public String getText() {
		return text;
	}
	public void setText(String fullText) {
		this.text = fullText;
	}
	public String getCrud() {
		return crud;
	}
	public void setCrud(String crud) {
		this.crud = crud;
	}
	
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public HashMap<String, Object> getBusinessKeyMap() {
		return businessKeyMap;
	}
	public void setBusinessKeyMap(HashMap<String, Object> businessKeyMap) {
		this.businessKeyMap = businessKeyMap;
	}

	public String getOrientationKey() {
		return orientationKey;
	}
	public void setOrientationKey(String orientationKey) {
		this.orientationKey = orientationKey;
	}
	
	public String getActionid() {
		return actionid;
	}

	public void setActionid(String actionid) {
		this.actionid = actionid;
	}
	
	public SysUser getUsrinfo() {
		return usrinfo;
	}
	public void setUsrinfo(SysUser usrinfo) {
		this.usrinfo = usrinfo;
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

	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public List<HashMap<String,Object>> getListPropertys() {
		return listPropertys;
	}
	public void setListPropertys(List<HashMap<String,Object>> listPropertys) {
		this.listPropertys = listPropertys;
	}
	
	public void addPropertys(HashMap<String,Object> propertys) {
		this.listPropertys.add(propertys);
	}
	
	public void removePropertys(int i) {
		this.listPropertys.remove(i);
	}
	public List<String> getTargetTableList() {
		return targetTableList;
	}
	public void setTargetTableList(List<String> targetTableList) {
		this.targetTableList = targetTableList;
	}
}
