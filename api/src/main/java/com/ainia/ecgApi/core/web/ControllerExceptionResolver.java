package com.ainia.ecgApi.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>controler exception handler</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ControllerExceptionResolver.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
public class ControllerExceptionResolver implements HandlerExceptionResolver {

	public static final String EXCPRTION_KEY = "_exception_";

	
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		System.out.println("-----------------------------");
		request.setAttribute(EXCPRTION_KEY , ex);
		return new ModelAndView("forward:/exception/");
	}

}
