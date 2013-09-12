package com.ainia.ecgApi.core.exception;

/**
 * <p></p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * InfoException.java
 * @author pq
 * @createdDate 2013-9-9
 * @version
 */
public class InfoException extends Exception {

	private String errorMessage;
	private Object[] arguments;
	
	public InfoException(Exception e) {
		super(e);
	}
	
	public InfoException(Exception e , String message) {
		super(e);
		this.errorMessage = message;
	}
	
	public InfoException(String message , Object... objects) {
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
