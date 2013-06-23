package com.ainia.ecgApi.controller.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.web.ControllerExceptionResolver;

/**
 * <p>exception handler controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ExceptionController.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
@Controller
@RequestMapping("exception")
public class ExceptionController {

	/**
	 * <p>handler exception </p>
	 * ResponseEntity<AjaxResult>
	 * @return
	 */
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public final ResponseEntity<Map<String , String>> handleException(HttpServletRequest request) {
		Exception e = (Exception)request.getAttribute(ControllerExceptionResolver.EXCPRTION_KEY);
		Map<String , String> errors = new HashMap<String , String>();
		if (e instanceof ConstraintViolationException) {
			ConstraintViolationException ex = (ConstraintViolationException)e;
	    	Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
	    	
	    	for (ConstraintViolation<?> violation  : violations) {
	    		errors.put(violation.getPropertyPath().toString() ,  violation.getMessage());
	    	}
		}
		else if (e instanceof ServiceException) {
			ServiceException ex = (ServiceException)e;
			errors.put("message" , ex.getErrorMessage());
		}
		else {
			errors.put("message" , "unknown");
		}
		return new ResponseEntity<Map<String , String>>(errors , HttpStatus.BAD_REQUEST);
    } 
}
