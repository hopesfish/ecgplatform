package com.ainia.ecgApi.core.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ainia.ecgApi.core.web.AjaxResult;

/**
 * <p>rest security with token</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * RestTokenInterceptor.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
public class RestTokenInterceptor implements HandlerInterceptor {
	
	private List<String> excludes;

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response , Object handler, Exception e)
			throws Exception {

	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {

	}
	/**
	 * handler controler check the token
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
	    String requestUri = request.getRequestURI();

	    for (String url : excludes) {  
	      if (requestUri.endsWith(url)) {  
	        return true;  
	      }  
	    }  
//	    String token = request.getHeader(AjaxResult.AUTH_HEADER);
//	    if (token == null) {
//	    	response.setStatus(HttpStatus.UNAUTHORIZED.value());
//	    	return false;
//	    }
		return true;
	}

	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}
	
}
