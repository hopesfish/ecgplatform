package com.ainia.ecgApi.core.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	    String requestUri = request.getRequestURI();
	    if (log.isDebugEnabled()) {
	    	log.debug(" interceptor the url " + requestUri);
	    }
	    for (String url : excludes) {  
	      if (requestUri.endsWith(url)) {  
	        return true;  
	      }  
	    }  
	    String token = request.getHeader(AjaxResult.AUTH_HEADER);
	    if (token == null) {
	    	response.setStatus(HttpStatus.UNAUTHORIZED.value());
	    	return false;
	    }
	    //TODO 此处获取用户信息 只针对employee 用户
	    authenticateService.setCurrentUser(authenticateService.loadUserByToken(token));
		return true;
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
