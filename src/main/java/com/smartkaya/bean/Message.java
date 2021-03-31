
package com.smartkaya.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message implements java.io.Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum Lever{TRACE,DEBUG,INFO,WARN,ERROR,FATAL};
	
	private String code;

	private String mesage;
	
	private Lever lever;
	
	public Lever getLever() {
		return lever;
	}
	public void setLever(Lever lever) {
		this.lever = lever;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMesage() {
		return mesage;
	}
	public void setMesage(String mesage) {
		this.mesage = mesage;
	}
}
