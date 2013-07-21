package com.ainia.ecgApi;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.ainia.ecgApi.core.utils.ServiceUtils;
import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadService.Type;

/**
 * <p>加载上传文件servlet</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * UploadServlet.java
 * @author pq
 * @createdDate 2013-7-18
 * @version
 */
public class UploadServlet implements Servlet {

	public void init(ServletConfig config) throws ServletException {

	}

	public ServletConfig getServletConfig() {
		return null;
	}

	public void service(ServletRequest req, ServletResponse res)
			throws ServletException {
		HttpServletRequest request =(HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		try {
			String accessUri = request.getRequestURI();
			int index = accessUri.indexOf(UploadService.UPLOAD_URI) + UploadService.UPLOAD_URI.length();
			String typeStr =  accessUri.substring(index , accessUri.indexOf("/" , index + 1));
			
			Type type = Type.valueOf(typeStr);
			String relativePath = accessUri.substring(accessUri.indexOf(typeStr) + typeStr.length() + 1);
			UploadService uploadService =(UploadService) ServiceUtils.getService(UploadService.class);
			response.getOutputStream().write(uploadService.load(type , relativePath));
			response.getOutputStream().flush();
		}
		catch(Exception e) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}
	}

	public String getServletInfo() {
		return null;
	}

	public void destroy() {

	}

}
