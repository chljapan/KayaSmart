package com.smartkaya.core;

/**
 * KaYa WorkFlow中的链接类
 * 业务流程里面实际分为正常的顺流和逆流
 * 我们需要严格区分顺逆关系后来确定Action的可否
 * @author LiangChen
 * @version 0.0.1
 */
public class WorkFlowConnection {
	// 是否是撤销，拒绝等返回操作
	private boolean reverse;
	// 目标（指向目标KaYaModel）
	private String KayaModelId;

	public boolean isReverse() {
		return reverse;
	}
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
	public String getKayaModelId() {
		return KayaModelId;
	}
	public void setKayaModelId(String kayaModelId) {
		KayaModelId = kayaModelId;
	}
	
}
