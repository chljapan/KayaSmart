package com.smartkaya.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GridColumn implements java.io.Serializable,Comparable<Object>{

	private static final long serialVersionUID = 1L;
	private String field;// 关键字
	private String title;// 名称
	private String width;// 前台UI宽度
	private String modeltype;// 元素类型
	private String datatype;// 数据类型
	private String datalength;// 数据长度（DB）
	private boolean hidden;// 隐藏
	private String index;// 排序
	private String validation;// 验证(正则表达式)
	private boolean isrole;
	private int rowspan;// Rowspan
	private int colspan;// Colspan
	private boolean searchFlg;// 检索字段
	private String kayamodelid;	// 模型Id
	private Editor editor;// 子内容
	private boolean uniquekey;// 主键
	private boolean required;// 必须入力
	private String defaultvalue;// 默认值
	private boolean encryption;// 加密项
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}

	public String getDatalength() {
		return datalength;
	}
	public void setDatalength(String datalength) {
		this.datalength = datalength;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getModeltype() {
		return modeltype;
	}
	public void setModeltype(String modeltype) {
		this.modeltype = modeltype;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}

	public String getValidation() {
		return validation;
	}
	public void setValidation(String validation) {
		this.validation = validation;
	}

	public boolean isSearchFlg() {
		return searchFlg;
	}
	public void setSearchFlg(boolean searchFlg) {
		this.searchFlg = searchFlg;
	}
	public String getKayamodelid() {
		return kayamodelid;
	}
	public void setKayamodelid(String kayamodelid) {
		this.kayamodelid = kayamodelid;
	}
	public Editor getEditor() {
		return editor;
	}
	public void setEditor(Editor editor) {
		this.editor = editor;
	}
	public boolean isRole() {
		return isrole;
	}
	public void setRole(boolean isrole) {
		this.isrole = isrole;
	}
	public int getRowspan() {
		return rowspan;
	}
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}
	public int getColspan() {
		return colspan;
	}
	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public boolean isUniquekey() {
		return uniquekey;
	}
	public void setUniquekey(boolean uniquekey) {
		this.uniquekey = uniquekey;
	}

	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}
	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	public boolean isEncryption() {
		return encryption;
	}
	public void setEncryption(boolean encryption) {
		this.encryption = encryption;
	}

	@Override
	public String toString() {

		return 	"\n" + "field:[" + this.field + "],"
				+ "title:[" + this.title + "],"
				+ "width:[" + this.width + "],"
				+ "modeltype:[" + this.modeltype + "],"
				+ "datatype:[" + this.datatype + "],"
				+ "datalength:[" + this.datalength + "],"
				+ "hidden:[" + this.hidden + "],"
				+ "index:[" + this.index + "],"
				+ "validation:[" + this.validation + "],"
				+ "isrole:[" + this.isrole + "],"
				+ "rowspan:[" + this.rowspan + "],"
				+ "colspan:[" + this.colspan + "],"
				+ "searchFlg:[" + this.searchFlg + "],"
				+ "kayamodelid:[" + this.kayamodelid + "],"
				+ "editor:[" + this.editor.toString() + "],"
				+ "uniquekey:[" + this.uniquekey + "],"
				+ "required:[" + this.required + "],"
				+ "defaultvalue:[" + this.defaultvalue + "],"
				+ "encryption:[" + this.encryption + "]";
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		GridColumn column = (GridColumn) o;  
		Integer v1 = Integer.valueOf(column.getIndex());

		Integer v2 = Integer.valueOf(this.getIndex());
		if(v1 != null){
			return v2.compareTo(v1);
		} 
		return 0; // Index比较大小，用于默认排序  
	}  
}
