package com.smartkaya.basebusiness;

import java.util.ArrayList;
import java.util.HashMap;

import com.smartkaya.bean.Paramaters;
import com.smartkaya.service.BusinessAnnotation;
import com.smartkaya.service.KayaBaseService;

@BusinessAnnotation(maxMoney = 15000,objectId = "")
public class id006500000003 extends KayaBaseService {


	@Override
	public void before() {
		// TODO Auto-generated method stub
		Paramaters paramaters = super.getParamaters();
		paramaters.getId();
		
		System.out.println(paramaters.getListPropertys().get(0).get("PhoneNumber"));
		paramaters.getListPropertys().get(0).put("PhoneNumber", "66666666");
	}

	public void excutBusiness() {
		EmployeeInfo em = new EmployeeInfo(this.getParamater().getPropertys());
		
		
		
		// TODO Auto-generated method stub
		Paramaters paramaters = super.getParamaters();
		paramaters.getId();
		System.out.println("excutBusiness");
	}

	@Override
	public void after() {
		Paramaters paramaters = super.getParamaters();
		ArrayList<Paramaters> ss = super.getParamatersList();
		paramaters.getId();
		System.out.println("after");
	}

	

}
