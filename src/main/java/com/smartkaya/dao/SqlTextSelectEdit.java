package com.smartkaya.dao;

import java.util.List;

import com.smartkaya.bean.Paramater;
import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.log.KayaLogManager;
import com.smartkaya.model.KayaMetaModel;

public class SqlTextSelectEdit {
	// KayaLogManager
	private KayaLogManager kayaLoger = KayaLogManager.getInstance();
	
	protected SqlTextSelectEdit() {
	}

	
	/**
	 * 全文检索
	 * @param paramater
	 * @return
	 */
	protected String selectByFullText(Paramater paramater) {
		String kayaModelId= paramater.getId();
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName + " WHERE orientationkey IN (SELECT orientationkey FROM " + tableName 
				+ " WHERE kindvalue like '%" + paramater.getText() + "%' AND parentid='" + kayaModelId + "') ORDER BY orientationkey DESC;");
		kayaLoger.info(selectSQL);
		return selectSQL.toString();
	}

	
	/**
	 * 任意键检索
	 * @param paramater
	 * @return
	 */
	protected String selectByFreeKind(Paramater paramater) {
		String kayaModelId = paramater.getId();
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
		// 更新全对象取得（包含子）
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);

		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
		selectSQL.append(" WHERE orientationkey IN (");
		boolean flg = true;
		StringBuilder selectEmptSQL = new StringBuilder("");

		// 主次表处理
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())){
			for (KayaMetaModel kayaModel: kayaModelList) {
				if (paramater.getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))){
					selectEmptSQL.append("SELECT orientationkey FROM " + tableName 
							+ " WHERE (kind = '" + kayaModel.get(Constant.KINDKEY) + "' AND kindvalue LIKE '"
							+ paramater.getPropertys().get(kayaModel.get(Constant.KINDKEY))
							+ "%')");
				}
			}
			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
		} else {
			// orientationkey
			for (KayaMetaModel kayaModel: kayaModelList) {
				if (paramater.getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))){
					if(flg) {

						selectEmptSQL.append("SELECT orientationkey FROM " + tableName 
								+ " WHERE (kind = '" + kayaModel.get(Constant.KINDKEY) + "' AND kindvalue LIKE '"
								+ paramater.getPropertys().get(kayaModel.get(Constant.KINDKEY))
								+ "%' AND businessid = '" +  paramater.getOrientationKey()  + "')");

						flg = false;
					}else {
						selectEmptSQL.insert(0, " SELECT orientationkey FROM " + tableName + " WHERE (orientationkey IN (");
						selectEmptSQL.append(")) AND (kind='" + kayaModel.get(Constant.KINDKEY) 
								+ "' AND kindvalue LIKE '" + paramater.getPropertys().get(kayaModel.get(Constant.KINDKEY)) + "%' AND businessid = '" +  paramater.getOrientationKey()  + "')");
					}
				}
			}
		}

		selectSQL.append(selectEmptSQL.toString() + ") ORDER BY orientationkey DESC;");

		kayaLoger.info(selectSQL);


		return selectSQL.toString();
	}

	/**
	 * 任意多键检索(And)
	 * @param paramater
	 * @return
	 */
	protected String selectMuiltKind(Paramater paramater) {

		String kayaModelId = paramater.getId();
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
		// 更新全对象取得（包含子）
		List<KayaMetaModel> kayaModelList = AccessKayaModel.getKayaModelByParentIdNotRole(kayaModelId);

		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
		selectSQL.append(" WHERE orientationkey IN (SELECT orientationkey FROM " + tableName + " WHERE ");
		StringBuilder selectEmptSQL = new StringBuilder("");

		// 检索条件个数
		int selectCount = 0;
		// 复数检索条件（OR）
		String orString = "";
		
		// 主次表处理
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())){
			for (KayaMetaModel kayaModel: kayaModelList) {
				if (paramater.getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))){
					// 检索条件个数
					selectCount = selectCount + 1;
					selectEmptSQL.append(orString);
					selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY) + "' AND kindvalue LIKE '"
							+ paramater.getPropertys().get(kayaModel.get(Constant.KINDKEY))
							+ "%' AND parentid='" + kayaModelId + "')");
					orString = " OR ";
				}
			}
			// 否则判定为子表(更新子表的时候需要BusinessID作为主键更新)
		} else {
			// orientationkey
			for (KayaMetaModel kayaModel: kayaModelList) {
				if (paramater.getPropertys().containsKey(kayaModel.get(Constant.KINDKEY))){
					// 检索条件个数
					selectCount = selectCount + 1;
					selectEmptSQL.append(orString);
					selectEmptSQL.append("(kind = '" + kayaModel.get(Constant.KINDKEY) + "' AND kindvalue LIKE '"
								+ paramater.getPropertys().get(kayaModel.get(Constant.KINDKEY))
							//	+ "%' AND businessid = '" +  businessId  + "')");
								+ "%' AND businessid = '" +  paramater.getOrientationKey()  + "' AND parentid='" + kayaModelId + "')");
					orString = " OR ";
				}
			}
		}

		selectSQL.append(selectEmptSQL.toString() + " group by orientationkey having count(1)=" + selectCount + ") ORDER BY orientationkey DESC;");

		kayaLoger.info(selectSQL);
		return selectSQL.toString();
	}

	
	/**
	 * 主键检索
	 * @param paramater
	 * @return
	 */
	protected String selectByBusinessKeys(Paramater paramater) {

		String kayaModelId= paramater.getId();
		String tableName = AccessKayaModel.getKayaModelId(kayaModelId).getTableId().replace('-','_');
		String businessId = KayaModelUtils.getBusinessKey(AccessKayaModel.getKayaModelId(kayaModelId),paramater.getPropertys());
		StringBuilder selectSQL = new StringBuilder(KayaModelUtils.selectString + tableName);
		if (Constant.G_PRODUCT.equals(AccessKayaModel.getParentKayaModel(kayaModelId).getMetaModelType())) {
			selectSQL.append(" WHERE parentid = '" + kayaModelId + "' AND businessid = '" + businessId + "'");

		} else {
			selectSQL.append(" WHERE parentid = '" + kayaModelId
					+ "' AND businessid = '" + paramater.getOrientationKey() 
					+ "' AND businesssubid = '" +  businessId + "'");
		}
		selectSQL.append(" ORDER BY orientationkey DESC;");

		return selectSQL.toString();
	}

}
