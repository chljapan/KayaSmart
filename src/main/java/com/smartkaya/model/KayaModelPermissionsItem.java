package com.smartkaya.model;

import java.util.ArrayList;
import java.util.List;

/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public class KayaModelPermissionsItem {
	private String id;
	private List<String> text = new ArrayList<String>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getText() {
		return text;
	}
	public void setText(List<String> text) {
		this.text = text;
	}


	
}
