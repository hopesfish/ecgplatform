package com.ainia.ecgApi.core.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ainia.ecgApi.core.exception.PermissionException;
import com.ainia.ecgApi.core.exception.ServiceException;

/**
 * <p>Controller exception handler</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * RestExceptionHandler.java
 * @author pq
 * @createdDate 2013-6-23
 * @version 0.1
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	    @ExceptionHandler(value = { PermissionException.class })
	    public final ResponseEntity<AjaxResult> handleException(PermissionException ex, WebRequest request) {
	    	AjaxResult ajaxResult = new AjaxResult(HttpStatus.FORBIDDEN.value());
	    	ajaxResult.setMessage(ex.getErrorMessage());
	    	ex.printStackTrace();
	    	return new ResponseEntity<AjaxResult>(ajaxResult , HttpStatus.FORBIDDEN);
	    } 
	
	    @ExceptionHandler(value = { ServiceException.class })
	    public final ResponseEntity<AjaxResult> handleException(ServiceException ex, WebRequest request) {
	    	AjaxResult ajaxResult = new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    	ajaxResult.setMessage(ex.getErrorMessage());
	    	ex.printStackTrace();
	    	return new ResponseEntity<AjaxResult>(ajaxResult , HttpStatus.INTERNAL_SERVER_ERROR);
	    } 
	    
	    @ExceptionHandler(value = { ConstraintViolationException.class })
	    public final ResponseEntity<AjaxResult> handleException(ConstraintViolationException ex, WebRequest request) {
	    	ex.printStackTrace();
	    	Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
	    	Map<String , String> errors = new HashMap<String , String>(violations.size());
	    	for (ConstraintViolation<?> violation  : violations) {
	    		errors.put(violation.getPropertyPath().toString() ,  violation.getMessage());
	    	}
	    	AjaxResult ajaxResult = new AjaxResult(HttpStatus.BAD_REQUEST.value());
	    	ajaxResult.setFieldErrors(errors);
	        return new ResponseEntity<AjaxResult>(ajaxResult , HttpStatus.BAD_REQUEST);
	    } 
	    
	    
	    @ExceptionHandler(value = { RuntimeException.class })
	    public final ResponseEntity<AjaxResult> handleException(RuntimeException ex, WebRequest request) {
	    	ex.printStackTrace();
	    	AjaxResult ajaxResult = new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    	ajaxResult.setMessage("UNKONW");
	    	return new ResponseEntity<AjaxResult>(ajaxResult , HttpStatus.INTERNAL_SERVER_ERROR);
	    } 
}
