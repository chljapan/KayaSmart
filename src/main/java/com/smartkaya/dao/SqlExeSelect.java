package com.smartkaya.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;

public final class SqlExeSelect extends AbstractSqlExE{

	@Override
	public List<Map<String,Object>> exe(Paramater paramater) {
		List<Map<String,Object>> kayaEntityList =  new ArrayList<Map<String,Object>>();
		return kayaEntityList;
	}

	@Override
	public List<Map<String,Object>> exe(Paramaters paramaters) {
		List<Map<String,Object>> kayaEntityList =  new ArrayList<Map<String,Object>>();
		return kayaEntityList;
	}

	@Override
	public List<Map<String,Object>> exe(List<Paramaters> paramatersList) {
		List<Map<String,Object>> kayaEntityList =  new ArrayList<Map<String,Object>>();
		return kayaEntityList;
	}
}
