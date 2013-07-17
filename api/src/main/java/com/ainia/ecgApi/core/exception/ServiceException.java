package com.ainia.ecgApi.core.exception;


/**
 * <p>service layer exception</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ServiceException.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
public class ServiceException extends RuntimeException {

	private String errorMessage;
	private Object[] arguments;
	
	public ServiceException(Exception e) {
		super(e);
	}
	
	public ServiceException(String message , Object... objects) {
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
