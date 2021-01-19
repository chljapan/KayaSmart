package com.smartkaya.bean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.smartkaya.user.User;
/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public class Paramaters implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	// RoleID
	private String id;
	private String text;
	private String crud;
	private HashMap<String,String> businessKeyMap;
//	private String workflowid;
	private String actionid;
	private List<String> targetTableList = new ArrayList<String>();
	private List<Message> messages = new ArrayList<Message>();
	//private String message;
	private boolean error;
	private List<Mapping> mappings = new ArrayList<Mapping>();
	private String orientationKey;
	private User usrinfo;
	
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
	
	public HashMap<String, String> getBusinessKeyMap() {
		return businessKeyMap;
	}
	public void setBusinessKeyMap(HashMap<String, String> businessKeyMap) {
		this.businessKeyMap = businessKeyMap;
	}
	//	public String getWorkflowid() {
//		return workflowid;
//	}
//	public void setWorkflowid(String workflowid) {
//		this.workflowid = workflowid;
//	}
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
	
	public User getUsrinfo() {
		return usrinfo;
	}
	public void setUsrinfo(User usrinfo) {
		this.usrinfo = usrinfo;
	}
	
//	public String getMesages() {
//		StringBuilder mesageText = new StringBuilder("");
//		boolean flg = true;
//		for (Message mesage:mesages) {
//			if (flg) {
//				mesageText.append("Lever:" + mesage.getLever() + " CODE:" + mesage.getCode() + " Message:" + mesage.getMesage());
//				flg = false;
//			} else {
//				// 依赖系统的换行符（Java 1.7以上）
//				mesageText.append(System.lineSeparator());
//				mesageText.append("Lever:" + mesage.getLever() + " CODE:" + mesage.getCode() + " Message:" + mesage.getMesage());
//			}
//		}
//		
//		return mesageText.toString();
//	}
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
	
//	public String getMessage() {
//		return message;
//	}
//	public void setMessage(String message) {
//		this.message = message;
//	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public List<Mapping> getMappings() {
		return mappings;
	}
	public void setMappings(List<Mapping> mappings) {
		this.mappings = mappings;
	}
	
	public void setMapping(Mapping mapping) {
		this.mappings.add(mapping);
	}
	
	public void removeMapping(int i) {
		this.mappings.remove(i);
	}
	public List<String> getTargetTableList() {
		return targetTableList;
	}
	public void setTargetTableList(List<String> targetTableList) {
		this.targetTableList = targetTableList;
	}
}
