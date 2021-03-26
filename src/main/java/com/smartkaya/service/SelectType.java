package com.smartkaya.service;

public enum SelectType {

	FULLTEXT("全文",1),FREEKEY("任意键",2),BUSINESSKEY("主键",3),MUILTKEYBYORI("用orientationkey多键",4),MUILTKEYBYBUS("用businessid多键单表",5),MUILTKEYINSUB("多键包含子表",6),CUSTOM("自定义",7);
	
    // 成员变量  
    private String name;  
    private int index;  
    // 构造方法  
    private SelectType(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    // 普通方法  
    public static String getName(int index) {  
        for (SelectType type : SelectType.values()) {  
            if (type.getIndex() == index) {  
                return type.name;  
            }  
        }  
        return null;  
    }  
    // get set 方法  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public int getIndex() {  
        return index;  
    }  
    public void setIndex(int index) {  
        this.index = index;  
    } 
    //覆盖方法 
    @Override 
    public String toString() { 
    return this.index+"_"+this.name; 
    } 
    
    /**
     * Typeを取得
     * 
     * @param index
     * @return SelectType
     */
    public static SelectType getValue(int index) {
        for (SelectType value : SelectType.values()) {
            if (value.getIndex()==index) {
                return value;
            }
        }
        return null;
    }
}
