package com.smartkaya.script;
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
 * 脚本执行引擎（默认GroovyScript）
 * @author LiangChen 2018.5.12
 *
 */
public class ScriptEXE {
	
	public static String Exe(String ExclusiveGagewayId,Map<String, Object> property) {
		ScriptEngineManager factory = new ScriptEngineManager();  
	    //每次生成一个engine实例  
		ScriptEngine engine = factory.getEngineByName(Constant.SCRIPT_ENGINE);
		String kayaModelId = "";
	    
	 // 从元模型中取出表信息
	 	List<KayaMetaModel> kayamodelList = AccessKayaModel.getExclusiveGatewayChildrenByKayaModelId(ExclusiveGagewayId);
	    
	    try {
		    assert engine != null; 
		    String scriptInit = "def sayHello(){";
		    String scriptString = "";
		    //javax.script.Bindings  
		    Bindings binding = engine.createBindings();  
		    binding.put("mapping", property);
	 		for (KayaMetaModel kayamodel: kayamodelList) {
	 			// 逻辑处理
	 			if (Constant.CONDITIONS.equals(kayamodel.getMetaModelType())) {
	 				scriptString = scriptString + "if(" +kayamodel.get(Constant.SCRIPTSTRING) + ") {return \"" + kayamodel.getGmeId() + "\"}else \n";
	 				// 变量初期处理
	 			} else if (Constant.PROPERTYREF.equals(kayamodel.getMetaModelType())) {
	 				KayaMetaModel kayamodelRef = AccessKayaModel.getKayaModelId(kayamodel.get(Constant.REFERRED));
	 				
	 				switch (kayamodelRef.get(Constant.DATATYPE)) {
					case "String":
					case "char":
						scriptInit = scriptInit + kayamodelRef.get(Constant.DATATYPE) + " " + kayamodelRef.get(Constant.KINDKEY) + " = \"" + property.get(kayamodel.get(Constant.KINDKEY)) + "\";\n";
						break;
					default :
						scriptInit = scriptInit + kayamodelRef.get(Constant.DATATYPE) + " " + kayamodelRef.get(Constant.KINDKEY) + " = " + property.get(kayamodel.get(Constant.KINDKEY)) + ";\n";
						break;
					}

	 				
	 			} else if (Constant.MASTER_REFERNCE.equals(kayamodel.getMetaModelType())) {
	 				scriptInit = scriptInit + "String " + kayamodel.get(Constant.KINDKEY) + " = \"" + property.get(kayamodel.get(Constant.KINDKEY)) + "\";\n";
	 			} else {
	 				
	 			}
	 		}
	 		
	 		scriptInit = scriptInit + scriptString + " {return '" + ExclusiveGagewayId + "'}}";

	 		engine.eval(scriptInit,binding);
	 	
		    kayaModelId = (String)((Invocable)engine).invokeFunction("sayHello"); 
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return kayaModelId;
	}
	
}
