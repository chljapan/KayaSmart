package com.smartkaya.script;

import java.util.HashMap;
class SalaryInfo {
	private  HashMap<String,Object> property;
	SalaryInfo(HashMap<String,Object> property) {
		this.property = property;
	}
	public String 工资类型 = (String)this.property.get("SalaryType");
	public String 金额 = (String)this.property.get("Amount");
	public String 日期 = (String)this.property.get("PayoffDate");
	public String 备注 = (String)this.property.get("Other");
}

def sayHello(HashMap<String,Object> property) {
	SalaryInfo salaryInfo = new SalaryInfo(property);
	
	println "你好， " + name
	
	"GroovyShell_3_2中的sayHello()方法的返回值"
}
 
sayHello(name)