package com.smartkaya.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartkaya.api.utils.MapUtils;
import com.smartkaya.bean.Editor;
import com.smartkaya.bean.GridColumn;
import com.smartkaya.bean.Options;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.model.KayaMetaModel;
import com.smartkaya.model.KayaModelMasterItem;
import com.smartkaya.user.SysUser;
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
//		HttpSession session = request.getSession();
//		SysUser user = (SysUser) session.getAttribute(Constant.G_USER);
		List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
		
		// TODO:menu的权限判断处理
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
//		HttpSession session = request.getSession();
//		SysUser user = (SysUser) session.getAttribute(Constant.G_USER);
		// 获取前台参数
		String kayaModelId = request.getParameter("kayaModelId");

		// TODO：表的处理的权限判断处理
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
		HttpSession session = request.getSession();
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
				SysUser user = (SysUser) session.getAttribute(Constant.G_USER);
				// 申请者（Add）
				if(Constant.APPLY.equals(WFType) && "false".equals(isEdit)) {
					// 取得活性化Actions（开始Action和指向自己的Action）
					kayaModelList = AccessKayaModel.getWorkFlowItemByStartKayaModelId(workFlowId,user);
					// 申请者（Edit）
				}  else if (Constant.APPLY.equals(WFType) && "true".equals(isEdit)) {
					kayaModelList = AccessKayaModel.getWorkFlowItemByNextKayaModelId(workFlowId,user,kvParamaterList.get(0));
					// 审批者（ReView）
				} else if (Constant.APPROVAL.equals(WFType)) {
					kayaModelList = AccessKayaModel.chekPermission(workFlowId,user,kvParamaterList.get(0));
				} else {
					// TODO： 例外处理
				}

				
				for (KayaMetaModel kayaModel : kayaModelList) {
					Map<String, Object> workflowItem = new HashMap<String, Object>();
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
//		HttpSession session = request.getSession();
//		SysUser user = (SysUser) session.getAttribute(Constant.G_USER);
		// TODO：admin权限才可以进行模型Load处理
		AccessKayaModel.ResetKayaModel();
		
		//
		//
		RestHelper helper = new RestHelper();
		Map<String, Object> ret = helper.getSimpleSuccess();
		ret.put(APIConstant.ITEMS, true);
		return ret;
	}

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