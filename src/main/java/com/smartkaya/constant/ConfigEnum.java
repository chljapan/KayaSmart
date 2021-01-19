package com.smartkaya.constant;

/**
 * 配置文件类型
 * 
 * @author Wuzy　2020/11/10
 * @version 1.0.0
 *
 */
public enum ConfigEnum {
    YAML("yaml"),
    PROPERTIES("properties");
    private String code;
    /**
     * 配置文件类型
     * 
     * @param code 配置文件类型
     */
    private ConfigEnum(String code) {
        this.code = code;
    }
    /**
     * 配置文件类型を取得
     * 
     * @return code 配置文件类型
     */
    public String getCode() {
        return code;
    }
    /**
     * 配置文件类型を取得
     * 
     * @param code 配置文件类型
     * @return ConfigEnum Enum
     */
    public static ConfigEnum toEnum(String code) {
        for (ConfigEnum enm : ConfigEnum.values()) {
            if (enm.getCode().equals(code)) {
                return enm;
            }
        }
        return null;
    }
}
