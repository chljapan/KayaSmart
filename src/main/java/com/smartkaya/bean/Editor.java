package com.smartkaya.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//@SuppressWarnings("deprecation")
//@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Editor implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String type;
	private Options options;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Options getOptions() {
		return options;
	}
	public void setOptions(Options options) {
		this.options = options;
	}
	
	@Override
	public String toString() {

		return 	"\n" + "type:[" + this.type + "],"
				+ "Options:[" + this.options + "]";
	}
	
}
