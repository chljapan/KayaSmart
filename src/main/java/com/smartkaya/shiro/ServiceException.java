package com.smartkaya.shiro;

public class ServiceException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super("次数用完");
		System.out.println("次数用完!");
	}
}
