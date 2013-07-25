package com.ainia.ecgApi.core.exception;

/**
 * <p>权限异常</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * PermissionException.java
 * @author pq
 * @createdDate 2013-7-25
 * @version
 */
public class PermissionException extends RuntimeException {

	private static final long serialVersionUID = -5679470848335540739L;
	
	private String errorMessage;
	private Object[] arguments;
	
	public PermissionException(Exception e) {
		super(e);
	}
	
	public PermissionException(String message , Object... objects) {
		super(message);
		this.errorMessage = message;
		this.arguments = objects;
	}

	public Object[] getArguments() {
		return arguments;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
