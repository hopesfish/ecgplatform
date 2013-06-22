package com.ainia.ecgApi.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * <p>controler exception handler</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ControllerExceptionResolver.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
public class ControllerExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		return new ModelAndView("forward:/exception/");
	}
	
	

}
