package com.smartkaya.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartkaya.api.utils.MapUtils;
import com.smartkaya.bean.Editor;
import com.smartkaya.bean.GridColumn;
import com.smartkaya.bean.Options;
import com.smartkaya.bean.Paramater;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.dao.KayaWorkFlow;
import com.smartkaya.model.KayaMetaModel;
import com.smartkaya.model.KayaModelMasterItem;
import com.smartkaya.user.User;
import com.smartkaya.user.User.UserType;
import com.smartkaya.utils.UtilTools;

import net.sf.json.JSONArray;

/**
 * KaYa
 * 
 * @author LiangChen 2018/4/30
 * @version 0.0.1
 */



@Controller
@RestController
public class HelloKayaInit {

	@RequestMapping(value = "/kayamenu", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> kayaMenu_html5(final HttpServletRequest request, final HttpServletResponse response) {
		List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
		menuList = AccessKayaModel.getMenuTree();
		RestHelper helper = new RestHelper();
		Map<String, Object> ret = helper.getSimpleSuccess();
		ret.put(Constant.MENUTREE, menuList);
		ret.put(Constant.LANGUAGE, AccessKayaModel.getLanguage());
		ret.put(APIConstant.ITEMS, true);
		return ret;
	}

	@RequestMapping(value = "/kayainit", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> HelloKaya(final HttpServletRequest request, final HttpServletResponse response) {

		// 获取前台参数
		String kayaModelId = request.getParameter("kayaModelId");

		// 表列信息列表
		List<GridColumn> columnsList = new ArrayList<GridColumn>();
		// 从元模型中取出表信息
		List<KayaMetaModel> kayamodelList = AccessKayaModel.getKayaModelByParentIdNoAction_UI(kayaModelId);

		KayaMetaModel kayamodelReq =  AccessKayaModel.getKayaModelId(kayaModelId);

		
		//Collections.sort(columnsList);
		// 如果绑定流程则添加流程信息
		if (!UtilTools.isEmpty(kayamodelReq.getWorkFlowId())) {
			// TODO:colspan
			AccessKayaModel.getKayaWorkFlowUI(kayamodelReq.getWorkFlowId());

			kayamodelList.add(AccessKayaModel.getKayaModelId(kayamodelReq.getWorkFlowId()));
			//Rowspan 最小值为2
			columnsList = editeGridColumn(kayamodelList,kayamodelReq.getRowspan()>2?kayamodelReq.getRowspan():2);
		} else {
			columnsList = editeGridColumn(kayamodelList,kayamodelReq.getRowspan());
		}
		//
		RestHelper helper = new RestHelper(null, null);
		Map<String, Object> ret = helper.getSimpleSuccess();
		ret.put(Constant.COLUMNSLIST, columnsList);
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			ret.put(Constant.BUSINESSKEYLIST, AccessKayaModel.getKayaModelId(kayaModelId).getBusinessKeys());
		} else {
			ret.put(Constant.BUSINESSKEYLIST, AccessKayaModel.getParentKayaModel(kayaModelId).getBusinessKeys());
			if (Constant.G_WORKFLOW.equals(kayamodelReq.getMetaModelType())) {
				List<String> businesssubKeyList = new ArrayList<String>();
				businesssubKeyList.add(Constant.WORKFLOW);
				ret.put(Constant.BUSINESSSUBKEYLIST, businesssubKeyList);
			} else {				
				ret.put(Constant.BUSINESSSUBKEYLIST, AccessKayaModel.getKayaModelId(kayaModelId).getBusinessKeys());
			}
		}

		ret.put(APIConstant.ITEMS, true);
		return ret;
	}

	@RequestMapping(value = "/kayainitwindow", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> KayaWindow(final HttpServletRequest request, final HttpServletResponse response) {
		// 获取前台参数
		String kayaModelId = request.getParameter("kayaModelId");
		
		// 表列信息列表
		List<GridColumn> columnsList = new ArrayList<GridColumn>();
		// 流程元素信息
		List<Map<String, Object>> workflowList = new ArrayList<Map<String, Object>>();
		// 从元模型中取出表信息
		List<KayaMetaModel> kayamodelList = AccessKayaModel.getKayaModelByParentIdNoAction_UI(kayaModelId);
		KayaMetaModel kayamodelReq =  AccessKayaModel.getKayaModelId(kayaModelId);
		columnsList = editeGridColumn(kayamodelList,kayamodelReq.getRowspan());

		// 监听业务流程处理
		if (!Constant.EMPTY.equals(AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId())) {
			// TODO:验证流程ID
			// 从元模型中取出表信息
			// 监听业务流程处理
			String workFlowId = AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId();
			
			
			if (!Constant.EMPTY.equals(workFlowId)) {
				// TODO:验证流程ID
				List<KayaMetaModel> kayaModelList = new ArrayList<KayaMetaModel>();
				// 流程类型
				String WFType = request.getParameter("wftype");
				String isEdit = request.getParameter("iseditflg");
				// 流程Pending状态的TaskId取得处理
				JSONArray jsonarray = JSONArray.fromObject(request.getParameter("kvParamaterList"));

				@SuppressWarnings("unchecked")
				List<Map<String, Object>> kvParamaterList = (List<Map<String, Object>>) JSONArray.toCollection(jsonarray,
						Map.class);

				// 申请者（Add）
				if(Constant.APPLY.equals(WFType) && "false".equals(isEdit)) {
					User user=new User();
					// 取得活性化Actions（开始Action和指向自己的Action）
					kayaModelList = AccessKayaModel.getWorkFlowItemByStartKayaModelId(workFlowId,user.initUserInfo(UserType.E1));
					// 申请者（Edit）
				}  else if (Constant.APPLY.equals(WFType) && "true".equals(isEdit)) {
					User user=new User();
					kayaModelList = AccessKayaModel.getWorkFlowItemByNextKayaModelId(workFlowId,user.initUserInfo(UserType.E1),kvParamaterList.get(0));
					// 审批者（ReView）
				} else if (Constant.APPROVAL.equals(WFType)) {
					User user=new User();
					kayaModelList = AccessKayaModel.chekPermission(workFlowId,user.initUserInfo(UserType.PEM),kvParamaterList.get(0));
				} else {
					// TODO： 例外处理
				}

				
				for (KayaMetaModel kayaModel : kayaModelList) {
					Map<String, Object> workflowItem = new HashMap<String, Object>();
					// System.out.println(kayaModel.getName());
					workflowItem.put(Constant.LABEL, kayaModel.getName());
					workflowItem.put(Constant.KAYAMODELID, kayaModel.getGmeId());
					workflowItem.put(Constant.EDITOR, kayaModel.getMetaModelType());
					workflowItem.put(Constant.FIELD, kayaModel.get(Constant.KINDKEY));
					workflowItem.put(Constant.INDEX, kayaModel.get(Constant.INDEXNO));
					workflowList.add(workflowItem);
				}
			}
		}

		Collections.sort(workflowList, new MapUtils.MapComparatorAsc());
		// Collections.sort(labelList);
		//
		//
		RestHelper helper = new RestHelper();
		Map<String, Object> ret = helper.getSimpleSuccess();
		ret.put(Constant.LABELLIST, columnsList);
		ret.put(Constant.BUSINESSKEYLIST, AccessKayaModel.getKayaModelId(kayaModelId).getBusinessKeys());
		ret.put(Constant.WORKFLOWLIST, workflowList);
		ret.put(APIConstant.ITEMS, true);
		return ret;
	}

	@RequestMapping(value = "/kayareset", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> KayaReset(final HttpServletRequest request, final HttpServletResponse response) {

		AccessKayaModel.ResetKayaModel();
		//
		//
		RestHelper helper = new RestHelper();
		Map<String, Object> ret = helper.getSimpleSuccess();
		ret.put(APIConstant.ITEMS, true);
		return ret;
	}
//	@RequestMapping(value = "/kayainitWFDetail", produces = "application/json;charset=utf-8")
//	@ResponseBody
//	public Map<String, Object> HelloKayaWFDetail(final HttpServletRequest request, final HttpServletResponse response) {
//
//		// 获取前台参数
//		String kayaModelId = request.getParameter("kayaModelId");
//		String orientationKey = request.getParameter("orientationKey");
//		String workflowId = request.getParameter("workflowId");
//		String flowcode = request.getParameter("flowcode");
//		// 表列信息列表
//		List<GridColumn> columnsList = new ArrayList<GridColumn>();
//		// 从元模型中取出表信息
//		List<KayaMetaModel> kayamodelList = AccessKayaModel.getKayaModelByParentIdNoAction(kayaModelId);
//
//		// 流程元素信息
//		List<Map<String, Object>> workflowItemList = new ArrayList<Map<String, Object>>();
//		// 新追加的项目
//
//		Paramater paramater = new Paramater();
//		paramater.setId(kayaModelId);
//		paramater.setMapping(new Propertys());
//		paramater.setOrientationKey(orientationKey);
//		List<String> targetTableList = new ArrayList<String>();
//		if(workflowId != null) {
//			targetTableList.add(workflowId);
//		}
//		paramater.setTargetTableList(targetTableList);
//		// 检索Map
//		Map<String, Object> propertys = new HashMap<String, Object>();
//
//		// 业务Map
//		//paramater.setBusinessKeyMap(reqParaToMap(request.getParameter("kvParamaterList")));
//		paramater.getMapping().setPropertys(propertys);
//		//paramater.setOrientationKey(request.getParameter("orientationKey"));
//		KayaWorkFlow dao = new KayaWorkFlow();
//
//		Map<String, Object> mapResult = dao.selectForWorkflowDetail(paramater);
//		@SuppressWarnings("unchecked")
//		List<Map<String, String>> resultList = (List<Map<String, String>>) mapResult.get("kayaEntityList");		
//		@SuppressWarnings("unchecked")
//		List<Map<String, String>> workflowList = (List<Map<String, String>>) mapResult.get("workflowList");
//		//Map<String, String> resultInit = resultList.get(0);
//
//
//		for (KayaMetaModel kayamodel : kayamodelList) {
//			GridColumn gridColumn = new GridColumn();
//			Editor editor = new Editor();
//			gridColumn.setEditor(editor);
//			gridColumn.setTitle(kayamodel.getName());
//
//			gridColumn.setWidth("140");
//
//			gridColumn.setField(kayamodel.get(Constant.KINDKEY));
//			gridColumn.setIndex(kayamodel.get(Constant.INDEXNO));
//			if (Constant.G_ROLE.equals(kayamodel.getMetaModelType())) {
//				editor.setType("textbox");
//				gridColumn.setHidden(true);
//				gridColumn.setRole(true);
//				gridColumn.setKayamodelid(kayamodel.getGmeId());
//			} else if (Constant.PROPERTYREF.equals(kayamodel.getMetaModelType())) {
//				gridColumn
//				.setDatatype(AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED))
//						.get(Constant.DATATYPE));
//				editor.setType(kayamodel.get(Constant.COMPONENTTYPE));
//				gridColumn.setRole(false);
//				gridColumn.setHidden(!Boolean.valueOf(kayamodel.get(Constant.DISPLAY)));
//				gridColumn.setSearchFlg(Boolean.valueOf(kayamodel.get(Constant.ISSEARCHKEY)));
//
//
//			} else if (Constant.MASTER_REFERNCE.equals(kayamodel.getMetaModelType())) {
//				Options options = new Options();
//				editor.setType(kayamodel.get(Constant.COMPONENTTYPE));
//				gridColumn.setRole(false);
//				gridColumn.setDatatype(Constant.MASTER);
//				gridColumn.setHidden(!Boolean.valueOf(kayamodel.get(Constant.DISPLAY)));
//				gridColumn.setSearchFlg(Boolean.valueOf(kayamodel.get(Constant.ISSEARCHKEY)));
//				List<KayaModelMasterItem> MasterItemList = AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED)).getMasterItems();
//				options.setData(MasterItemList);
//
//				editor.setOptions(options);
//			} else {
//
//				editor.setType(kayamodel.get(Constant.COMPONENTTYPE));
//				gridColumn.setRole(false);
//				gridColumn.setDatatype(kayamodel.get(Constant.DATATYPE));
//				gridColumn.setHidden(!Boolean.valueOf(kayamodel.get(Constant.DISPLAY)));
//				gridColumn.setSearchFlg(Boolean.valueOf(kayamodel.get(Constant.ISSEARCHKEY)));
//
//			}
//			columnsList.add(gridColumn);
//			// columnsList.add(column);
//		}
//		Collections.sort(columnsList);
//
//		// 监听业务流程处理
//		if (!Constant.EMPTY.equals(AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId())) {
//			// TODO:验证流程ID
//			// 从元模型中取出表信息
//			// List<KayaMetaModel> kayamodelList =
//			// AccessKayaModel.getKayaModelByParentIdNoAction(kayaModelId);
//			// 监听业务流程处理
//			String workFlowIdTmp = AccessKayaModel.getKayaModelId(kayaModelId).getWorkFlowId();
//			if (!Constant.EMPTY.equals(workFlowIdTmp)) {
//				KayaMetaModel kayaMetaWorkFlowModel = AccessKayaModel.getKayaModelId(workFlowIdTmp);
//				// TODO:验证流程ID
//				List<KayaMetaModel> kayaModelListWF = new ArrayList<>();
//				if(workflowId != null && workflowId.length()>3){
//					if("mg".equals(workflowId.substring(0,2))) {
//
//						kayaModelListWF = AccessKayaModel.getWorkFlowItemByKayaModelId(flowcode);
//
//					} else {
//						if(!flowcode.equals(kayaMetaWorkFlowModel.get(Constant.END))) {
//							String startUserTaskId = AccessKayaModel
//									.getWorkFlowConnectionDes(kayaMetaWorkFlowModel.get(Constant.START));
//							kayaModelListWF = AccessKayaModel.getWorkFlowItemByKayaModelId(startUserTaskId);
//						}
//					}
//				} 
//
//				for (KayaMetaModel kayaModel : kayaModelListWF) {
//					Map<String, Object> workflowItem = new HashMap<String, Object>();
//					// System.out.println(kayaModel.getName());
//					workflowItem.put(Constant.LABEL, kayaModel.getName());
//					workflowItem.put(Constant.KAYAMODELID, kayaModel.getGmeId());
//					workflowItem.put(Constant.EDITOR, kayaModel.getMetaModelType());
//					workflowItem.put(Constant.FIELD, kayaModel.get(Constant.KINDKEY));
//					workflowItem.put(Constant.INDEX, kayaModel.get(Constant.INDEXNO));
//					workflowItemList.add(workflowItem);
//				}
//			}
//		}
//
//		Collections.sort(workflowItemList, new MapUtils.MapComparatorAsc());
//		RestHelper helper = new RestHelper(null, null);
//		Map<String, Object> ret = helper.getSimpleSuccess();
//		ret.put(Constant.COLUMNSLIST, columnsList);
//
//		ret.put("resultList", resultList);
//		ret.put("workflowList", workflowList);
//		ret.put("actionItemList", workflowItemList);
//
//		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
//			ret.put(Constant.BUSINESSKEYLIST, AccessKayaModel.getKayaModelId(kayaModelId).getBusinessKeys());
//		} else {
//			ret.put(Constant.BUSINESSKEYLIST, AccessKayaModel.getParentKayaModel(kayaModelId).getBusinessKeys());			
//			ret.put(Constant.BUSINESSSUBKEYLIST, AccessKayaModel.getKayaModelId(kayaModelId).getBusinessKeys());
//		}
//
//		ret.put(APIConstant.ITEMS, true);
//		return ret;
//	}


	/*
	 * 获取Group嵌套结构
	 * @param kayaMetaModeList
	 * @param rowspan 
	 * @return
	 */
	private List<GridColumn> editeGridColumn(List<KayaMetaModel> kayaMetaModeList,int rowspan) {
		List<GridColumn> columnsList = new ArrayList<GridColumn>();
		for (KayaMetaModel kayamodel:kayaMetaModeList) {
			GridColumn gridColumn = new GridColumn();
			Editor editor = new Editor();
			Options options = new Options();
			gridColumn.setEditor(editor);
			gridColumn.setTitle(kayamodel.getName());

			// 字段长度
			if ("0".equals(kayamodel.get(Constant.WIDTH))) {
				gridColumn.setWidth("140");
			} else {
				gridColumn.setWidth(kayamodel.get(Constant.WIDTH));
				gridColumn.setWidth("140");
			}
			gridColumn.setModeltype(kayamodel.getMetaModelType());
			gridColumn.setDatalength(kayamodel.get(Constant.DATALENGTH));

			// 关键字
			gridColumn.setField(kayamodel.get(Constant.KINDKEY));
			// IndexNo
			gridColumn.setIndex(kayamodel.get(Constant.INDEXNO));

			switch (kayamodel.getMetaModelType()) {
			case Constant.G_ROLE:
				editor.setType("textbox");
				gridColumn.setHidden(true);
				gridColumn.setRole(true);
				gridColumn.setDatatype(Constant.G_ROLE);
				gridColumn.setKayamodelid(kayamodel.getGmeId());
				break;
			case Constant.PROPERTYREF:
				gridColumn.setDatatype(AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED)).get(Constant.DATATYPE));
				editor.setType(kayamodel.get(Constant.COMPONENTTYPE));
				gridColumn.setRowspan(rowspan);
				gridColumn.setRole(false);
				gridColumn.setUniquekey(kayamodel.isUniqueKey());
				gridColumn.setHidden(!Boolean.valueOf(kayamodel.get(Constant.DISPLAY)));
				gridColumn.setRequired(Boolean.valueOf(kayamodel.get(Constant.REQUIRED)));
				gridColumn.setSearchFlg(Boolean.valueOf(kayamodel.get(Constant.ISSEARCHKEY)));
				gridColumn.setDatatype(AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED))
						.get(Constant.DATATYPE));
				gridColumn.setValidation(AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED))
						.get(Constant.FORMAT));
				gridColumn.setDefaultvalue(AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED))
						.get(Constant.DEFAULTVALUE));
				gridColumn.setEncryption(AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED)).get(
						Constant.ISENCRYPTION)==null?false:Boolean.valueOf(kayamodel.get(Constant.ISENCRYPTION)));
				break;
			case Constant.MASTER_REFERNCE:
				//Options m_options = new Options();
				editor.setType(kayamodel.get(Constant.COMPONENTTYPE));
				gridColumn.setRowspan(rowspan);
				gridColumn.setRole(false);
				gridColumn.setUniquekey(kayamodel.isUniqueKey());
				gridColumn.setDatatype(Constant.MASTER);
				gridColumn.setHidden(!Boolean.valueOf(kayamodel.get(Constant.DISPLAY)));
				gridColumn.setRequired(Boolean.valueOf(kayamodel.get(Constant.REQUIRED)));
				gridColumn.setSearchFlg(Boolean.valueOf(kayamodel.get(Constant.ISSEARCHKEY)));
				gridColumn.setValidation(AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED))
						.get(Constant.FORMAT));
				gridColumn.setDefaultvalue(AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED)).get(Constant.DEFAULTVALUE));
				List<KayaModelMasterItem> MasterItemList = AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED)).getMasterItems();

				options.setData(MasterItemList);
				editor.setOptions(options);
				break;
			case Constant.G_GROUP:
				Options g_options = new Options();
				editor.setType(kayamodel.get(Constant.COMPONENTTYPE));
				gridColumn.setRole(false);
				gridColumn.setColspan(kayamodel.getColspan());
				gridColumn.setDatatype(Constant.G_GROUP);
				gridColumn.setHidden(!Boolean.valueOf(kayamodel.get(Constant.DISPLAY)));
				gridColumn.setSearchFlg(Boolean.valueOf(kayamodel.get(Constant.ISSEARCHKEY)));
				gridColumn.setRowspan(rowspan);
				// 递归处理Group结构，层数-1
				g_options.setGroup(editeGridColumn(kayamodel.getGroupItems(),rowspan-1));
				editor.setOptions(g_options);
				break;
			case Constant.G_WORKFLOW:
				//Options w_options = new Options();
				editor.setType(kayamodel.get(Constant.COMPONENTTYPE));
				gridColumn.setRole(false);
				gridColumn.setColspan(kayamodel.getColspan());
				gridColumn.setDatatype(Constant.G_GROUP);
				gridColumn.setHidden(false);
				gridColumn.setSearchFlg(Boolean.valueOf(kayamodel.get(Constant.ISSEARCHKEY)));
				gridColumn.setRowspan(rowspan);
				// 递归处理Group结构，层数-1
				options.setGroup(editeGridColumn(kayamodel.getGroupItems(),rowspan-1));
				editor.setOptions(options);
				break;
			case Constant.USERTASK:
				editor.setType(kayamodel.get(Constant.COMPONENTTYPE));
				gridColumn.setRowspan(rowspan);
				gridColumn.setRole(false);
				gridColumn.setUniquekey(kayamodel.isUniqueKey());
				gridColumn.setDatatype(kayamodel.get(Constant.DATATYPE));
				gridColumn.setHidden(!Boolean.valueOf(kayamodel.get(Constant.DISPLAY)));
				gridColumn.setRequired(Boolean.valueOf(kayamodel.get(Constant.REQUIRED)));
				gridColumn.setSearchFlg(Boolean.valueOf(kayamodel.get(Constant.ISSEARCHKEY)));
				gridColumn.setDefaultvalue(kayamodel.get(Constant.DEFAULTVALUE));
				gridColumn.setValidation(kayamodel.get(Constant.FORMAT));
				gridColumn.setEncryption(kayamodel.get(Constant.ISENCRYPTION)==null?false:Boolean.valueOf(kayamodel.get(Constant.ISENCRYPTION)));
				break;
			default :
				editor.setType(kayamodel.get(Constant.COMPONENTTYPE));
				gridColumn.setRowspan(rowspan);
				gridColumn.setRole(false);
				gridColumn.setUniquekey(kayamodel.isUniqueKey());
				gridColumn.setDatatype(kayamodel.get(Constant.DATATYPE));
				gridColumn.setHidden(!Boolean.valueOf(kayamodel.get(Constant.DISPLAY)));
				gridColumn.setRequired(Boolean.valueOf(kayamodel.get(Constant.REQUIRED)));
				gridColumn.setSearchFlg(Boolean.valueOf(kayamodel.get(Constant.ISSEARCHKEY)));
				gridColumn.setDefaultvalue(kayamodel.get(Constant.DEFAULTVALUE));
				gridColumn.setValidation(kayamodel.get(Constant.FORMAT));
				gridColumn.setEncryption(kayamodel.get(Constant.ISENCRYPTION)==null?false:Boolean.valueOf(kayamodel.get(Constant.ISENCRYPTION)));
				break;
			}

			columnsList.add(gridColumn);
		}

		Collections.sort(columnsList);
		return columnsList;
	}
}