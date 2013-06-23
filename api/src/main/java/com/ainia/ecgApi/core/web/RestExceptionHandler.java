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

	    @ExceptionHandler(value = { ServiceException.class })
	    public final ResponseEntity<?> handleException(ServiceException ex, WebRequest request) {
	    	return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	   } 
	    
	    @ExceptionHandler(value = { ConstraintViolationException.class })
	    public final ResponseEntity<Map<String , String>> handleException(ConstraintViolationException ex, WebRequest request) {
	    	Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
	    	Map<String , String> errors = new HashMap<String , String>(violations.size());
	    	for (ConstraintViolation<?> violation  : violations) {
	    		errors.put(violation.getPropertyPath().toString() ,  violation.getMessage());
	    	}
	    	System.out.println("============  "+ errors);
	        return new ResponseEntity<Map<String , String>>(errors , HttpStatus.BAD_REQUEST);
	    } 
}
