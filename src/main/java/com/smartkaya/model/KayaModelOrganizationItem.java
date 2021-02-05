package com.smartkaya.model;

public class KayaModelOrganizationItem {

	private boolean isRef;
	private String text;
	private String refSrc;
	private String refDst;
	public boolean isRef() {
		return isRef;
	}
	public void setRef(boolean isRef) {
		this.isRef = isRef;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getRefSrc() {
		return refSrc;
	}
	public void setRefSrc(String refSource) {
		this.refSrc = refSource;
	}
	public String getRefDst() {
		return refDst;
	}
	public void setRefDst(String refDesiination) {
		this.refDst = refDesiination;
	}

	
}
