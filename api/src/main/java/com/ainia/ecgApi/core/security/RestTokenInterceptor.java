package com.ainia.ecgApi.core.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	public static final String SECURE_KEY = "secure";
	public static final String SPLIT_KEY = ":";
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private List<String> excludes;
	private boolean enable;
	@Autowired
	private AuthenticateService authenticateService;

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
		if (!enable) {
			return true;
		}
	    String uri = request.getRequestURI();
	    String requestUri = uri.substring(uri.indexOf(request.getContextPath()) + request.getContextPath().length());
	    if (log.isDebugEnabled()) {
	    	log.debug(" interceptor the url " + requestUri);
	    }
	    if ("/".equals(requestUri)) {
	    	return true;
	    }
	    boolean isExclude =false;
	    for (String key : excludes) {
	      String url = key;
	      String method = null;
	      if (key.indexOf(SPLIT_KEY) != -1) {
	    	  String[] keyValues = StringUtils.split(key , SPLIT_KEY);
	    	  url = keyValues[0];
	    	  method = keyValues[1];
	      }
	      //TODO 将来需要支持ant 表达式
	      if (requestUri.endsWith(url) && (method == null || request.getMethod().equalsIgnoreCase(method))) {  
	        isExclude = true;  
	      }  
	    }  
	    String token = request.getHeader(AjaxResult.AUTH_HEADER);
	    if (StringUtils.isBlank(token)) {
	    	if (isExclude) {
	    		return true;
	    	}
	    	else {
		    	response.setStatus(HttpStatus.UNAUTHORIZED.value());
		    	return false;	
	    	}
	    }        	
	    else {
	    	authenticateService.setCurrentUser(authenticateService.loadUserByToken(token));
	    	return true;
	    }
	}

	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}
	
}
