package com.smartkaya.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;



public class UtilTools {
	
	
	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Object obj) {
		boolean ret = false;
		if (isNull(obj)) {
			ret = true;
		}
		if (obj instanceof String) {
			if (((String) obj).isEmpty()) {
				ret = true;
			}
		}
		return ret;
	}

	public static boolean isValidPicture(int width, int height) {
		boolean ret = true;

		if (width < 800 || height < 800 || width != height) {
			ret = false;
		}
		return ret;

	}

	public static void createDirs(String dir){
		File f = new File(dir);
		if(!f.exists()){
			f.mkdirs();
		}
	}
	
	private static long orderNum = 0l;
	private static String date;
	
	/**
	 * 生成编号
	 * 
	 * @return
	 */
	public static synchronized String getOrderNo() {
		String str = new SimpleDateFormat("yyMMddHHmmsss").format(new Date());
		if (date == null || !date.equals(str)) {
			date = str;
			orderNum = 0l;
		}
		orderNum++;
		long orderNo = Long.parseLong((date)) * 10000;
		orderNo += orderNum;
		;
		return orderNo + "";
	}
}
