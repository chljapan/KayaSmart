package com.smartkaya.api;

import java.util.HashMap;
import java.util.Map;

import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.exception.ExceptionEnum;

/**
 * KaYa
 * 
 * @author LiangChen 2018/4/30
 * @version 1.0.0
 */
public class RestHelper {

	private Map<String, Object> props = new HashMap<String, Object>();

	public RestHelper() {
	}
	public RestHelper(Object msg, Object resultDetails) {
		if (msg != null) {
			if (msg instanceof Paramater) {
				props.put(APIConstant.PROP_RETURN_MESSAGE, ((Paramater) msg).messagesToString());

			} else if (msg instanceof Paramaters) {
				props.put(APIConstant.PROP_RETURN_MESSAGE, ((Paramaters) msg).messagesToString());

			} else {
				props.put(APIConstant.PROP_RETURN_MESSAGE, msg);
			}
		}
		props.put(APIConstant.RESULT_DETAILS, resultDetails);
	}

	public Map<String, Object> setMessage(Object msg) {
		props.put(APIConstant.PROP_RETURN_MESSAGE, msg);
		return props;

	}

	public Map<String, Object> getSimpleSuccess() {

		props.put(APIConstant.PROP_RETURN_CODE, ExceptionEnum.SUCCESS.getCode());
		return props;

	}

	public Map<String, Object> getFailed(int code) {

		props.put(APIConstant.PROP_RETURN_CODE, code);
		return props;

	}
}