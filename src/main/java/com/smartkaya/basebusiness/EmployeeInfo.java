package com.smartkaya.basebusiness;

//员工信息
import java.util.HashMap;
class EmployeeInfo {
	private  HashMap<String,Object> property;
	EmployeeInfo(HashMap<String,Object> property) {
		this.property = property;
	}
	public String 用户状态 = (String)this.property.get("UserStatus");
	public String 性别 = (String)this.property.get("Sex");
	public String 市 = (String)this.property.get("AddressCity");
	public String 区 = (String)this.property.get("AddressDistrict");
	public String 经理ID = (String)this.property.get("PemId");
	public String 员工ID = (String)this.property.get("EmployeeId");
	public String 姓名 = (String)this.property.get("EmployeeName");
	public String 省 = (String)this.property.get("AddressProvince");
	public String 联系电话 = (String)this.property.get("PhoneNumber");
	public String 密码 = (String)this.property.get("Password");
}