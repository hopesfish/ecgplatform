package com.ainia.ecgApi.core.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>http utils</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HttpUtils.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.1
 */
public class HttpUtils {
	
	public static boolean isAjax(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equals("XMLHttpRequest")) {
			return true;
		} else {
			return false;
		}

	}

}
