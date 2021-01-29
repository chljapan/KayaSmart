package com.smartkaya.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartkaya.api.utils.StringUtil;
import com.smartkaya.bean.CalendarTaskVo;
import com.smartkaya.bean.Mapping;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.dao.KayaSQLExecute;
import com.smartkaya.dao.KayaWorkFlow;
import com.smartkaya.model.KayaModelMasterItem;
import com.smartkaya.user.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * KaYa
 * 
 * @author LiangChen 2018/4/30
 * @version 1.0.0
 */
@Controller
@RestController
public class HelloKayaCrud {
	// 用户信息初期化（测试用）

	private enum UserType {

		E,EO,PEM,PM,GL,PL;
	};
	private User initUserInfo(UserType userType) {
		User user= new User();
		List<String> roleList = new ArrayList<String>();
		roleList.add("E");
		roleList.add("EO");
		user.setRoleList(roleList);
		
		
		List<String> permissions = new ArrayList<String>();
		permissions.add("E");
		permissions.add("M");
		permissions.add("GL");
		user.setPermissions(permissions);
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("PemId","ChenLiang");
		userMap.put("BumenCode","2");
		userMap.put("ZhiweiCode","99");
		user.setUserMap(userMap);
		switch (userType){
		case E:
			
			break;
		case EO:
			
			break;
		default:
			break;
		}
		
		return user;
	}
	@RequestMapping(value = "/kayaselect", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> HelloKayaSelect(final HttpServletRequest request, final HttpServletResponse response) {
		// 获取前台参数
		String kayaModelId = request.getParameter("kayaModelId");
		// 获取前台参数
		String searchName = request.getParameter("searchname");
		String searchValue = request.getParameter("searchvalue");

		Paramater paramater = new Paramater();
		paramater.setId(kayaModelId);
		paramater.setMapping(new Mapping());
		// 检索Map
		Map<String, Object> propertys = reqParaToMap(request.getParameter("searchParamaterList"));
		propertys.put(searchName, searchValue == null ? "" : searchValue);

		// 业务Map
		paramater.setBusinessKeyMap(reqParaToMap(request.getParameter("kvParamaterList")));
		paramater.getMapping().setPropertys(propertys);
		paramater.setOrientationKey(request.getParameter("orientationKey"));
		KayaSQLExecute dao = new KayaSQLExecute();

		// List<Map<String,String>> mapList = dao.selectByFreeKind(paramater);
		List<Map<String, String>> resultList = dao.selectMuiltKindByOrientationkey(paramater);

		RestHelper helper = new RestHelper(null, resultList);
		return helper.getSimpleSuccess();
	}

	@RequestMapping(value = "/kayaselectall", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> HelloKayaSelectAll(final HttpServletRequest request,
			final HttpServletResponse response) {
		// 获取前台参数
		String kayaModelId = request.getParameter("kayaModelId");
		// 获取前台参数
		String searchName = request.getParameter("searchname");
		String searchValue = request.getParameter("searchvalue");

		Paramater paramater = new Paramater();
		paramater.setId(kayaModelId);
		paramater.setMapping(new Mapping());
		// 检索Map
		Map<String, Object> propertys = reqParaToMap(request.getParameter("searchParamaterList"));
		propertys.put(searchName, searchValue == null ? "" : searchValue);

		// 业务Map
		paramater.setBusinessKeyMap(reqParaToMap(request.getParameter("kvParamaterList")));
		paramater.getMapping().setPropertys(propertys);
		paramater.setOrientationKey(request.getParameter("orientationKey"));
		KayaSQLExecute dao = new KayaSQLExecute();

		// List<Map<String,String>> mapList = dao.selectByFreeKind(paramater);
		Map<String, Object> resultList = dao.selectMuiltKindAllInfo(paramater);

		RestHelper helper = new RestHelper(null, resultList);
		return helper.getSimpleSuccess();
	}

	@RequestMapping(value = "/kayainsert", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> HelloKayaInsert(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String kayaModelId = request.getParameter("kayaModelId");
		String actionId = request.getParameter("actionId");

		JSONArray jsonarray = JSONArray.fromObject(request.getParameter("kvParamaterList"));

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> kvParamaterList = (List<Map<String, Object>>) JSONArray.toCollection(jsonarray,
				Map.class);

		Paramaters paramaters = new Paramaters();
		paramaters.setId(kayaModelId);
		paramaters.setOrientationKey(request.getParameter("orientationKey"));
		paramaters.setUsrinfo(user);
		List<Mapping> mappings = new ArrayList<Mapping>();
		paramaters.setMappings(mappings);
		for (Map<String, Object> emptyMap : kvParamaterList) {
			Mapping mapping = new Mapping();
			mapping.setPropertys(emptyMap);
			paramaters.setMapping(mapping);
			paramaters.setActionid(actionId);
		}
		KayaSQLExecute dao = new KayaSQLExecute();
		dao.insert(paramaters);

		RestHelper helper = new RestHelper();
		return helper.getSimpleSuccess();
	}

	@RequestMapping(value = "/kayaupdate", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> HelloKayaUpdate(HttpServletRequest request, HttpServletResponse response) {
		KayaSQLExecute dao = new KayaSQLExecute();

		String kayaModelId = request.getParameter("kayaModelId");

		Paramaters paramaters = new Paramaters();

		JSONArray jsonarray = JSONArray.fromObject(request.getParameter("kvParamaterList"));
		JSONArray insertField = JSONArray.fromObject(request.getParameter("insertField"));

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> kvParamaterList = (List<Map<String, Object>>) JSONArray.toCollection(jsonarray,
				Map.class);

		// 新追加的项目
		@SuppressWarnings("unchecked")
		List<String> insertFieldList = (List<String>) JSONArray.toCollection(insertField);

		paramaters.setId(kayaModelId);
		// multipleParamater.setKvParamaterList(kvParamaterList);
		paramaters.setOrientationKey(request.getParameter("orientationKey"));
		List<Mapping> mappings = new ArrayList<Mapping>();
		paramaters.setMappings(mappings);

		for (Map<String, Object> emptyMap : kvParamaterList) {
			Mapping mapping = new Mapping();
			mapping.setPropertys(emptyMap);
			paramaters.setMapping(mapping);
		}

		// 有新追加项目时
		if (insertFieldList.size() > 0) {
			dao.update(paramaters, insertFieldList);
			// 普通更新时
		} else {
			dao.update(paramaters);
		}

		RestHelper helper = new RestHelper();
		return helper.getSimpleSuccess();
	}

	@RequestMapping(value = "/kayadelete", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> HelloKayaDelete(HttpServletRequest request, HttpServletResponse response) {

		KayaSQLExecute dao = new KayaSQLExecute();

		String kayaModelId = request.getParameter("kayaModelId");

		Paramaters paramaters = new Paramaters();

		JSONArray jsonarray = JSONArray.fromObject(request.getParameter("kvParamaterList"));
		JSONArray businessKeyList = JSONArray.fromObject(request.getParameter("kvBusinessKeyList"));

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> kvParamaterList = (List<Map<String, Object>>) JSONArray.toCollection(jsonarray,
				Map.class);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> kvBusinessKeyList = (List<Map<String, Object>>) JSONArray
				.toCollection(businessKeyList, Map.class);

		paramaters.setId(kayaModelId);
		// multipleParamater.setKvParamaterList(kvParamaterList);
		paramaters.setOrientationKey(request.getParameter("orientationKey"));
		List<Mapping> mappings = new ArrayList<Mapping>();
		paramaters.setMappings(mappings);

		for (Map<String, Object> bisinessKeys : kvParamaterList) {
			Mapping mapping = new Mapping();
			for (Map<String, Object> parentBisinessKeys : kvBusinessKeyList) {
				bisinessKeys.putAll(parentBisinessKeys);
			}
			mapping.setPropertys(bisinessKeys);
			paramaters.setMapping(mapping);

		}

		dao.delete(paramaters);

		RestHelper helper = new RestHelper();
		return helper.getSimpleSuccess();
	}
	
	@RequestMapping(value = "/workflowselect", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> HelloKayaWorkflowSelect(final HttpServletRequest request, final HttpServletResponse response) {
		// 获取前台参数
		String kayaModelId = request.getParameter("kayaModelId");
		// 获取前台参数
		String searchUsr = request.getParameter("searchUser");
		String searchName = request.getParameter("searchname");
		String searchValue = request.getParameter("searchvalue");
		HttpSession session = request.getSession();
		User userInfo = (User) session.getAttribute("user");
		boolean mgrFlg = false;
		if(kayaModelId.length()>3){
			if("mg".equals(kayaModelId.substring(0,2))) {
				kayaModelId = kayaModelId.substring(3);
				mgrFlg = true;
			}			
		}

		Paramater paramater = new Paramater();
		paramater.setId(kayaModelId);
		paramater.setMapping(new Mapping());
		// 检索Map
		Map<String, Object> propertys = reqParaToMap(request.getParameter("searchParamaterList"));
		propertys.put("searchUser", searchUsr);
		propertys.put("kind", searchName);
		propertys.put("kindvalue", searchValue);
				

		// 业务Map
		paramater.setBusinessKeyMap(reqParaToMap(request.getParameter("kvParamaterList")));
		paramater.getMapping().setPropertys(propertys);
		paramater.setOrientationKey(request.getParameter("orientationKey"));
		KayaWorkFlow dao = new KayaWorkFlow();

		// List<Map<String,String>> mapList = dao.selectByFreeKind(paramater);
		List<Map<String, String>> resultList = dao.selectForWorkflow(paramater,mgrFlg,userInfo);

		RestHelper helper = new RestHelper(null, resultList);
		return helper.getSimpleSuccess();
	}

	private HashMap<String, Object> reqParaToMap(Object paramater) {

		HashMap<String, Object> keyMap = new HashMap<String, Object>();
		if (paramater != null) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> kvParamaterList = (List<Map<String, Object>>) JSONArray
					.toCollection(JSONArray.fromObject(paramater), Map.class);
			for (Map<String, Object> emptyMap : kvParamaterList) {
				keyMap.putAll(emptyMap);
			}
		}

		return keyMap;
	}

	@RequestMapping(value = "/workflowhandle", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> WorkflowHandle(HttpServletRequest request, HttpServletResponse response) {

		String kayaModelId = request.getParameter("kayaModelId");

		Paramaters paramaters = new Paramaters();
		JSONArray jsonarray = JSONArray.fromObject(request.getParameter("kvParamaterList"));
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> kvParamaterList = (List<Map<String, Object>>) JSONArray.toCollection(jsonarray,
				Map.class);

		// 画面值设定
		paramaters.setId(kayaModelId);
		paramaters.setCrud(Constant.WORKFLOW);
		paramaters.setActionid((request.getParameter("actionId")));
		HashMap<String, String> businessKeyMap = new HashMap<String, String>();
		businessKeyMap.put(Constant.BUSINESSID, request.getParameter(Constant.BUSINESSID));
		businessKeyMap.put("businesssubid", request.getParameter("businesssubid"));
		paramaters.setBusinessKeyMap(businessKeyMap);
		// multipleParamater.setKvParamaterList(kvParamaterList);
		paramaters.setOrientationKey(request.getParameter(Constant.ORIENTATIONKEY));
		List<Mapping> mappings = new ArrayList<Mapping>();
		paramaters.setMappings(mappings);

		for (Map<String, Object> emptyMap : kvParamaterList) {
			Mapping mapping = new Mapping();
			mapping.setPropertys(emptyMap);
			paramaters.setMapping(mapping);
		}

		// workflow实行
		KayaWorkFlow kayaWorkflow = new KayaWorkFlow();
		kayaWorkflow.excuteKayaWorkFlow(paramaters);

		RestHelper helper = new RestHelper();
		return helper.getSimpleSuccess();
	}

	@RequestMapping(value = "/getCalenderList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> getCalendarList(final HttpServletRequest request, final HttpServletResponse response)
			throws ParseException {
		// 获取前台参数
		String kayaModelId = request.getParameter("kayaModelId");
		// 获取前台参数
		String userId = request.getParameter("userId");
		String stime = request.getParameter("stime");
		String etime = request.getParameter("etime");
		Paramater paramater = new Paramater();
		paramater.setId(kayaModelId);
		paramater.setMapping(new Mapping());
		// 检索Map
		HashMap<String, Object> propertys = new HashMap<String, Object>();
		propertys.put("userId", userId);
		propertys.put("stime", stime);
		propertys.put("etime", etime);

		// 业务Map
		paramater.getMapping().setPropertys(propertys);

		KayaSQLExecute dao = new KayaSQLExecute();

		List<CalendarTaskVo> data = dao.getCalendarList(paramater);
		RestHelper helper = new RestHelper(null, data);
		return helper.getSimpleSuccess();
	}

	@RequestMapping(value = "/kayagetmaster", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> KayaGetMaster(final HttpServletRequest request, final HttpServletResponse response) {
		// 获取前台参数
		String kayaModelId = request.getParameter("kayaModelId");
		List<KayaModelMasterItem> masterItemList =  AccessKayaModel
				.getKayaModelId(kayaModelId).getMasterItems();
		RestHelper helper = new RestHelper();
		Map<String, Object> ret = helper.getSimpleSuccess();
		ret.put("options", masterItemList);
		//ret.put(Constant.ITEMS, true);
		return ret;
	}

	// 多表 多种可能操作
	@RequestMapping(value = "/insDelUptMultiTable", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> focusCheckInfoUpdate(HttpServletRequest request, HttpServletResponse response) {

		KayaSQLExecute dao = new KayaSQLExecute();

		List<Paramaters> paramatersList = formatToParamatersList(request);

		dao.execute(paramatersList);


		RestHelper helper = new RestHelper();
		return helper.getSimpleSuccess();
	}

	/**
	 * 
	 * @param request
	 * @return 前端传来的参数一览
	 */
	private List<Paramaters> formatToParamatersList(HttpServletRequest request) {

		JSONArray jsonarray = JSONArray.fromObject(request.getParameter("kvParamaterList"));

		List<Paramaters> paramatersList = new ArrayList<Paramaters>();
		int parameterSize = jsonarray.size();
		for (int i = 0; i < parameterSize; i++) {
			JSONObject jsonarraySub = JSONObject.fromObject(jsonarray.get(i));
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> kvParamaterList = (List<Map<String, Object>>) JSONArray
					.toCollection(jsonarraySub.getJSONArray("propertys"), Map.class);
			String kayaModelId = getInputValue(jsonarraySub, "kayaModelId");
			String actionId = getInputValue(jsonarraySub, "actionId");
			String orientationKey = getInputValue(jsonarraySub, "orientationKey");
			String targetTable = getInputValue(jsonarraySub, "targetTable");
			// 目标表指定
			List<String> targetTableList = new ArrayList<String>();
			if (StringUtil.isNotEmpty(targetTable)) {
				String[] targetTableArr = targetTable.split(",");
				for (String tableName : targetTableArr) {
					targetTableList.add(tableName);
				}
			}
			Paramaters paramaters = new Paramaters();
			paramaters.setId(kayaModelId);
			paramaters.setCrud(actionId);
			paramaters.setActionid(actionId);
			paramaters.setOrientationKey(orientationKey);
			paramaters.setTargetTableList(targetTableList);
			for (Map<String, Object> emptyMap : kvParamaterList) {
				Mapping mapping = new Mapping();
				mapping.setPropertys(emptyMap);
				paramaters.setMapping(mapping);
			}
			paramatersList.add(paramaters);

		}
		return paramatersList;
	}

	/**
	 * 
	 * @param key
	 * @return 前端传来的值
	 */
	private String getInputValue(JSONObject jsonarray, String key) {
		if (jsonarray.containsKey(key)) {
			return jsonarray.getString(key);
		} else {
			return StringUtil.EMPTY;
		}
	}
}
