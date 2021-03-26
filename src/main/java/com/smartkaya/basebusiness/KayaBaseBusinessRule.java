package com.smartkaya.basebusiness;

import java.util.HashMap;

import com.smartkaya.service.KayaBaseService;

public class KayaBaseBusinessRule  extends KayaBaseService{
	class emlInfo {
		private  HashMap<String,Object> property;
		emlInfo(HashMap<String,Object> property) {
			this.property = property;
		}
		
		public String 姓名 = (String) this.property.get("name");
	}
	
	
	
	public KayaBaseBusinessRule() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void before() {
		// TODO Auto-generated method stub
		emlInfo 员工信息 = new emlInfo(this.getParamater().getPropertys());
		
		if( 员工信息.姓名 == "") {
			员工信息.姓名 ="xxxxx";
			
		}
		System.out.println("Base excutBusiness!");
//		this.getParamater().getPropertys(){
//			
//		}
//		.get(员工信息.姓名)
//		.get("")
//
		
	}

	public void excutBusiness(){
		System.out.println("Base excutBusiness!");

		
	}
	
	@Override
	public void after() {
		// TODO Auto-generated method stub
		
	}

}
