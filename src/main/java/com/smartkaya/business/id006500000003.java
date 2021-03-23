package com.smartkaya.business;

import com.smartkaya.bean.Paramaters;
import com.smartkaya.service.KayaBaseService;

@BusinessAnnotation(maxMoney = 15000,objectId = "")
public class id006500000003 extends KayaBaseService {

	@Override
	public void before() {
		// TODO Auto-generated method stub
		Paramaters paramaters = super.getParamaters();
		paramaters.getId();
		System.out.println("before");
		
	}

//	@Override
	public void excutBusiness() {
		// TODO Auto-generated method stub
		Paramaters paramaters = super.getParamaters();
		paramaters.getId();
		System.out.println("excutBusiness");
	}

	@Override
	public void after() {
		Paramaters paramaters = super.getParamaters();
		paramaters.getId();
		System.out.println("after");
	}

	

}
