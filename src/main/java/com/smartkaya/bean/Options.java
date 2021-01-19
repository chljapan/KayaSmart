package com.smartkaya.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smartkaya.model.KayaModelMasterItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Options implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private List<KayaModelMasterItem> data;
	private List<GridColumn> group;
	private String textField = "text";
	private String valueField = "id";
    private String panelHeight;
    
	public List<KayaModelMasterItem> getData() {
		return data;
	}
	public void setData(List<KayaModelMasterItem> data) {
		this.data = data;
	}
	
	public List<GridColumn> getGroup() {
		return group;
	}
	public void setGroup(List<GridColumn> group) {
		this.group = group;
	}
	public String getTextField() {
		return textField;
	}

	public String getValueField() {
		return valueField;
	}

	public String getPanelHeight() {
		return panelHeight;
	}
	public void setPanelHeight(String panelHeight) {
		this.panelHeight = panelHeight;
	}
	@Override
	public String toString() {

		return 	"\n" + "type:[" + this.data + "],"
				+ "type:[" + this.group+ "],"
				+ "textField:[" + this.textField + "],"
				+ "valueField:[" + this.valueField + "],"
				+ "panelHeight:[" + this.panelHeight + "]";
	}
}
