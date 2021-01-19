package com.smartkaya.constant;

/**
 *
 * 检索条件
 * @author Wuzy　2020/08/26
 * @version 1.0.0
 *
 */
public enum ConditionEnum {
    IN("IN"),
    LIKE("LIKE"),
    DATE("DATE");
    private String code;
    /**
     * 条件
     * 
     * @param code 条件
     */
    private ConditionEnum(String code) {
        this.code = code;
    }
    /**
     * 条件を取得
     * 
     * @return code 条件
     */
    public String getCode() {
        return code;
    }
    /**
     * 条件を取得
     * 
     * @param code 条件
     * @return ConditionEnum Enum
     */
    public static ConditionEnum toEnum(String code) {
        for (ConditionEnum enm : ConditionEnum.values()) {
            if (enm.getCode().equals(code)) {
                return enm;
            }
        }
        return null;
    }
}
