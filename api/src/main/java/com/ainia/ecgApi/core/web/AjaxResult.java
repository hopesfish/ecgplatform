package com.ainia.ecgApi.core.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @File Name     : AjaxResult.java
 * @Author        ： pp
 * @Create Date   : 2012-10-6 下午4:38:13
 * @Modified By   :   
 * @Modified Date : 
 * @Description   :
 * @Version       :
 */
public class AjaxResult implements Serializable {

	public static final String AUTH_HEADER = "Authorization";
	public static final String AUTH_TOKEN  = "token";
	public static final String AUTH_ID = "userId";
	
	private static final long serialVersionUID = 1L;
	
	
	private int status;
	private String message;
	private String forwardUrl;
	private String confirmMsg;
    private Map<String , Object> params = new HashMap<String , Object>();
    private Map<String , String> fieldErrors = new HashMap<String , String>();
    private Map<String , String> headers = new HashMap<String , String>(6);
    private String token;
	

    public AjaxResult() {
    	
    }
    
    public AjaxResult(int status) {
    	this(status , null , null , null);
    }
    
    public AjaxResult(int status , String message) {
    	this(status , message , null , null);
    }

	public AjaxResult(int status, String message, String forwardUrl,
			String confirmMsg) {
		super();
		this.status = status;
		this.message = message;
		this.forwardUrl = forwardUrl;
		this.confirmMsg = confirmMsg;
	}
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}

	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}

	public String getConfirmMsg() {
		return confirmMsg;
	}

	public void setConfirmMsg(String confirmMsg) {
		this.confirmMsg = confirmMsg;
	}

	public Map<String , Object> getParams() {
		return params;
	}

	public void addParam(String key , String value) {
		this.params.put(key , value);
	}
	
	public void setParams(Map<String , Object> params) {
		this.params = params;
	}
	
	public void addHeader(String name , String value) {
		this.headers.put(name , value);
	}
	@JsonIgnore
	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> header) {
		this.headers = header;
	}
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void addFieldError(String field , String message) {
		this.fieldErrors.put(field , message);
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(Map<String, String> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}
