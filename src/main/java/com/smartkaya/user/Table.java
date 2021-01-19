package com.smartkaya.user;

public class Table {
    private String id; // 表明
    private String tableName; // 角色名称,如 admin/user
    //private List<String> cloums; // TODO：字段级别控制
    private String description; // 角色描述,用于UI显示
    
    private boolean rw;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isRw() {
		return rw;
	}
	public void setRw(boolean rw) {
		this.rw = rw;
	}
}
