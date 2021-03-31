package com.smartkaya.script;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.smartkaya.constant.Constant;
import com.smartkaya.core.AccessKayaModel;
import com.smartkaya.model.KayaMetaModel;

/**
 * 规则引擎（默认GroovyScript）
 * @author LiangChen 2021.3.27
 *
 */
public class ScriptEXERules {

	public static String Exe(String kayaModelId,Map<String, Object> property) {
		ScriptEngineManager factory = new ScriptEngineManager();  
		//每次生成一个engine实例  
		ScriptEngine engine = factory.getEngineByName(Constant.SCRIPT_ENGINE);

		// 从元模型中取出表信息
		KayaMetaModel kayaMetaModel = AccessKayaModel.getKayaModelId(kayaModelId);
		List<KayaMetaModel> kayamodelList = AccessKayaModel.getKayaModelByParentIdNoAction(kayaModelId);

		try {
			assert engine != null; 
			String scriptInit = "def exeRules(){";
			String scriptString = "";
			//javax.script.Bindings  
			Bindings binding = engine.createBindings();  
			binding.put("mapping", property);
			//			EmployeeInfo(HashMap<String,Object> property) {
			//				this.property = property;
			//			}
			//			public String 用户状态 = (String)this.property.get("UserStatus");
			//			public String 性别 = (String)this.property.get("Sex");
			//			public String 市 = (String)this.property.get("AddressCity");
			//			public String 区 = (String)this.property.get("AddressDistrict");
			// import
			String inmportString = "import java.util.HashMap;\n";

			// class
			StringBuilder classString = new StringBuilder("class " + kayaMetaModel.get(Constant.KINDKEY) + " {\n" +// Class 定义
					"	private  HashMap<String,Object> property; \n" + // 构造函数
					"	" + kayaMetaModel.get(Constant.KINDKEY) + "(HashMap<String,Object> property) { \n" +
					"		this.property = property;\n" +
					"	}");
			// 变量初期化
			kayamodelList.forEach(item -> {
				switch (item.getMetaModelType()) { 
				case Constant.G_PROPERTY:
				case Constant.UNIQUEPROPERTY:
					System.out.println("	public " + item.get(Constant.DATATYPE) + " "
							+ item.getName() 
							+ " = (" + item.get(Constant.DATATYPE) + ")this.property.get(\"" 
							+ item.get(Constant.KINDKEY) + "\");");
					break;
				case Constant.MASTER_REFERNCE:
					System.out.println("	public String "
							+ item.getName() 
							+ " = (String)this.property.get(\"" 
							+ item.get(Constant.KINDKEY) + "\");");
					break;
				case Constant.PROPERTYREF:
					System.out.println("	public " + AccessKayaModel.getKayaModelId(item.get(Constant.REFERRED)).get(Constant.DATATYPE) + " "
							+ item.getName() 
							+ " = (" +  AccessKayaModel.getKayaModelId(item.get(Constant.REFERRED)).get(Constant.DATATYPE) + ")this.property.get(\"" 
							+ item.get(Constant.KINDKEY) + "\");");
					break;
				default:
					break;

				}
			});

			// 调用业务逻辑

			// 返回数据

		engine.eval(scriptInit,binding);

		kayaModelId = (String)((Invocable)engine).invokeFunction("exeRules"); 
	} catch (Exception e) {
		e.printStackTrace();
	} 

	return kayaModelId;
}

}
