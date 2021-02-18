package com.smartkaya.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartkaya.api.utils.StringUtil;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.dao.KayaSQLExecute;
import com.smartkaya.model.KayaModelMasterItem;
import com.smartkaya.user.User;
import com.smartkaya.user.User.UserType;

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

	@RequestMapping(value = "/kayaselect", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> HelloKayaSelect(final HttpServletRequest request, final HttpServletResponse response) {
		// 获取前台参数
		String kayaModelId = request.getParameter("kayaModelId");
		// 获取前台参数
		String searchName = request.getParameter("searchname");
		String searchValue = request.getParameter("searchvalue");
		//String wftype = request.getParameter("wftype");

		Paramater paramater = new Paramater();
		paramater.setId(kayaModelId);
		paramater.setPropertys(new HashMap<String,Object>());
		
		
		User user = new User();
		paramater.setUsrinfo(user.initUserInfo(UserType.E1));
		
		// 检索Map
		HashMap<String, Object> propertys = reqParaToMap(request.getParameter("searchParamaterList"));
		propertys.put(searchName, searchValue == null ? "" : searchValue);

		// 业务Map
		paramater.setBusinessKeyMap(reqParaToMap(request.getParameter("kvParamaterList")));
		paramater.setPropertys(propertys);
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
		paramater.setPropertys(new HashMap<String,Object>());
		// 检索Map
		HashMap<String, Object> propertys = reqParaToMap(request.getParameter("searchParamaterList"));
		propertys.put(searchName, searchValue == null ? "" : searchValue);

		// 业务Map
		paramater.setBusinessKeyMap(reqParaToMap(request.getParameter("kvParamaterList")));
		paramater.setPropertys(propertys);
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
		//HttpSession session = request.getSession();

		String kayaModelId = request.getParameter("kayaModelId");
		String actionId = request.getParameter("actionId");

		JSONArray jsonarray = JSONArray.fromObject(request.getParameter("kvParamaterList"));

		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> kvParamaterList = (List<HashMap<String, Object>>) JSONArray.toCollection(jsonarray,
				HashMap.class);

		
		Paramaters paramaters = new Paramaters();
		String WFType = request.getParameter("wftype");
		// 流程Pending状态的TaskId取得处理


		// 申请者（Add）
		if(Constant.APPLY.equals(WFType) && !Constant.TRUE.equals(request.getParameter("isEdit"))) {
			//User user=new User();
			// 取得活性化Actions（开始Action和指向自己的Action）
			
			
			// Set Orientationkey

			
			
			
			// 申请者（Edit）
		}  else if (Constant.APPLY.equals(WFType) && "true".equals(request.getParameter("isEdit"))) {
			//User user=new User();
			
			// 审批者（ReView）
		} else if (Constant.APPROVAL.equals(WFType)) {
			//User user=new User();
			
		} else if (Constant.EMPTY.equals(WFType)) {
			paramaters.setOrientationKey(request.getParameter("orientationKey"));
		}
		
		
		
		
		
		
		
		
		if (Constant.TRUE.equals(request.getParameter("isEdit"))) {
			paramaters.setCrud(Constant.UPDATE);
		} else {
			paramaters.setCrud(Constant.INSERT);
		}
		
		// TODO:普通用户(E1)
		User user = new User();
		
		paramaters.setUsrinfo(user.initUserInfo(UserType.E1));
		
		
		paramaters.setId(kayaModelId);
		
		//paramaters.setUsrinfo(user);

		paramaters.setListPropertys(kvParamaterList);
		paramaters.setActionid(actionId);
		paramaters.setBusinessKeyMap(user.getUserMap());
		
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
		List<HashMap<String, Object>> kvParamaterList = (List<HashMap<String, Object>>) JSONArray.toCollection(jsonarray,
				HashMap.class);

		// 新追加的项目
		@SuppressWarnings("unchecked")
		List<String> insertFieldList = (List<String>) JSONArray.toCollection(insertField);

		paramaters.setId(kayaModelId);
		// multipleParamater.setKvParamaterList(kvParamaterList);
		paramaters.setOrientationKey(request.getParameter("orientationKey"));
		paramaters.setListPropertys(kvParamaterList);


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
		List<HashMap<String, Object>> kvParamaterList = (List<HashMap<String, Object>>) JSONArray.toCollection(jsonarray,
				HashMap.class);

		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> kvBusinessKeyList = (List<HashMap<String, Object>>) JSONArray
				.toCollection(businessKeyList, Map.class);

		paramaters.setId(kayaModelId);
		// multipleParamater.setKvParamaterList(kvParamaterList);
		paramaters.setOrientationKey(request.getParameter("orientationKey"));

		paramaters.setListPropertys(kvParamaterList);
		paramaters.setBusinessKeyMap(kvBusinessKeyList.get(0));

//		for (Map<String, Object> bisinessKeys : kvParamaterList) {
//			Propertys propertys = new Propertys();
//			for (Map<String, Object> parentBisinessKeys : kvBusinessKeyList) {
//				bisinessKeys.putAll(parentBisinessKeys);
//			}
//			propertys.setPropertys(bisinessKeys);
//			paramaters.setMapping(propertys);
//
//		}

		dao.delete(paramaters);

		RestHelper helper = new RestHelper();
		return helper.getSimpleSuccess();
	}
	
//	@RequestMapping(value = "/workflowselect", produces = "application/json;charset=utf-8")
//	@ResponseBody
//	public Map<String, Object> HelloKayaWorkflowSelect(final HttpServletRequest request, final HttpServletResponse response) {
//		// 获取前台参数
//		String kayaModelId = request.getParameter("kayaModelId");
//		// 获取前台参数
//		String searchUsr = request.getParameter("searchUser");
//		String searchName = request.getParameter("searchname");
//		String searchValue = request.getParameter("searchvalue");
//		HttpSession session = request.getSession();
//		User userInfo = (User) session.getAttribute("user");
//		boolean mgrFlg = false;
//		if(kayaModelId.length()>3){
//			if("mg".equals(kayaModelId.substring(0,2))) {
//				kayaModelId = kayaModelId.substring(3);
//				mgrFlg = true;
//			}			
//		}
//
//		Paramater paramater = new Paramater();
//		paramater.setId(kayaModelId);
//		paramater.setMapping(new Propertys());
//		// 检索Map
//		Map<String, Object> propertys = reqParaToMap(request.getParameter("searchParamaterList"));
//		propertys.put("searchUser", searchUsr);
//		propertys.put("kind", searchName);
//		propertys.put("kindvalue", searchValue);
//				
//
//		// 业务Map
//		paramater.setBusinessKeyMap(reqParaToMap(request.getParameter("kvParamaterList")));
//		paramater.getMapping().setPropertys(propertys);
//		paramater.setOrientationKey(request.getParameter("orientationKey"));
//		KayaWorkFlow dao = new KayaWorkFlow();
//
//		// List<Map<String,String>> mapList = dao.selectByFreeKind(paramater);
//		List<Map<String, String>> resultList = dao.selectForWorkflow(paramater,mgrFlg,userInfo);
//
//		RestHelper helper = new RestHelper(null, resultList);
//		return helper.getSimpleSuccess();
//	}

	private HashMap<String, Object> reqParaToMap(Object paramater) {

		HashMap<String, Object> keyMap = new HashMap<String, Object>();
		if (paramater != null) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> kvParamaterList = (List<Map<String, Object>>) JSONArray
					.toCollection(JSONArray.fromObject(paramater), HashMap.class);
			for (Map<String, Object> emptyMap : kvParamaterList) {
				keyMap.putAll(emptyMap);
			}
		}

		return keyMap;
	}

//	@RequestMapping(value = "/workflowhandle", produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public Map<String, Object> WorkflowHandle(HttpServletRequest request, HttpServletResponse response) {
//
//		String kayaModelId = request.getParameter("kayaModelId");
//
//		Paramaters paramaters = new Paramaters();
//		JSONArray jsonarray = JSONArray.fromObject(request.getParameter("kvParamaterList"));
//		@SuppressWarnings("unchecked")
//		List<Map<String, Object>> kvParamaterList = (List<Map<String, Object>>) JSONArray.toCollection(jsonarray,
//				Map.class);
//
//		// 画面值设定
//		paramaters.setId(kayaModelId);
//		paramaters.setCrud(Constant.WORKFLOW);
//		paramaters.setActionid((request.getParameter("actionId")));
//		HashMap<String, Object> businessKeyMap = new HashMap<String, Object>();
//		businessKeyMap.put(Constant.BUSINESSID, request.getParameter(Constant.BUSINESSID));
//		businessKeyMap.put("businesssubid", request.getParameter("businesssubid"));
//		paramaters.setBusinessKeyMap(businessKeyMap);
//		// multipleParamater.setKvParamaterList(kvParamaterList);
//		paramaters.setOrientationKey(request.getParameter(Constant.ORIENTATIONKEY));
//		List<Propertys> propertys = new ArrayList<Propertys>();
//		paramaters.setMappings(propertys);
//
//		for (Map<String, Object> emptyMap : kvParamaterList) {
//			Propertys propertys = new Propertys();
//			propertys.setPropertys(emptyMap);
//			paramaters.setMapping(propertys);
//		}
//
//		// workflow实行
//		KayaWorkFlow kayaWorkflow = new KayaWorkFlow();
//		kayaWorkflow.excuteKayaWorkFlow(paramaters);
//
//		RestHelper helper = new RestHelper();
//		return helper.getSimpleSuccess();
//	}

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
			List<HashMap<String, Object>> kvParamaterList = (List<HashMap<String, Object>>) JSONArray
					.toCollection(jsonarraySub.getJSONArray("propertys"), HashMap.class);
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
			
			paramaters.setListPropertys(kvParamaterList);
//			for (Map<String, Object> emptyMap : kvParamaterList) {
//				Propertys propertys = new Propertys();
//				propertys.setPropertys(emptyMap);
//				paramaters.setMapping(propertys);
//			}
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
