package com.smartkaya.api;
/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public enum APIStatus {

    ERR_INVALID_DATA(100, "Input data is not valid."),
    ERR_USER_NOT_EXIST(110, "User does not exist"),
    ERR_USER_NOT_VALID(111, "User name or password is not correct"),
    USER_ALREADY_EXIST(112, "User name already exist"),
    USER_PENDING_STATUS(113, "User have not activated"),
    USER_NO_ROLE(401, "user have not role to access"),
    USER_LOCKED_OR_EXPIRTED(402, "user was locked or expirted"),
    INVALID_PARAMETER(201, "Invalid request parameter"),
    TOKEN_EXPIRIED(202, "Token expiried"),
    REQUIRED_LOGIN(203, "请重新登录！"),
    INVALID_TOKEN(204, "Invalid token"),
    // Common status
    OK(200, "success"),
	//Cart status
	QUANTITY_NOT_ENOUGH(301, "XXXXX"),
	//product status 
	PRODUCT_NOT_ACTIVE(501,"XXXXX");
    private final int code;
    private final String description;

    private APIStatus(int s, String v) {
        code = s;
        description = v;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
