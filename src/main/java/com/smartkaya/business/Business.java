package com.smartkaya.business;

import java.lang.reflect.Method;


@BusinessAnnotation(maxMoney = 15000,objectId = "")
public class Business {

	public Business() {
		// TODO Auto-generated constructor stub
	}
	   /**
     * @param money 转账金额
     */

    public static void TransferMoney(double money){
        System.out.println(processAnnotationMoney(money));

    }
    
    private static String processAnnotationMoney(double money) {
        try {
            Method transferMoney = BusinessAnnotation.class.getDeclaredMethod("TransferMoney",double.class);
            boolean annotationPresent = transferMoney.isAnnotationPresent(BusinessAnnotation.class);
            if(annotationPresent){
            	BusinessAnnotation annotation = transferMoney.getAnnotation(BusinessAnnotation.class);
                double l = annotation.maxMoney();
                String s = annotation.objectId();
                if(money>l){
                   return  s + "转账金额大于限额，转账失败";
                }else {
                    return"转账金额为:"+money+"，转账成功";
                }
            }
        } catch ( NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "转账处理失败";
    }
}
