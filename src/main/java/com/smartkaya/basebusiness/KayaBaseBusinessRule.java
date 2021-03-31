package com.smartkaya.basebusiness;

import com.smartkaya.service.KayaBaseService;

public class KayaBaseBusinessRule  extends KayaBaseService{
	
	/**
	 * 业务数据库操作前处理
	 */
	@Override
	public void before() {
		// TODO Auto-generated method stub
		kayaLoger.info("before");
		//paramater.getPropertys();
	}
	
//	/**
//	 * 业务数据库操作 
//	 */
//	public void excutBusiness(){
//		this.kayaLoger.info("excutBusiness");
//		dao.execute(this.getParamater());
//			// 查询情况下返回查询结果
//	}
	
	/**
	 * 业务数据库操作后处理
	 */
	@Override
	public void after() {
		// TODO Auto-generated method stub
		this.kayaLoger.info("after");
	}

}
