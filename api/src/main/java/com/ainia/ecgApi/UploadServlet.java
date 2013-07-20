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
		String accessUri = request.getRequestURI();
		String typeStr =  accessUri.substring(UploadService.UPLOAD_URI.length() , accessUri.indexOf("/" , 
											UploadService.UPLOAD_URI.length()));
		try {
			Type type = Type.valueOf(typeStr);
			String relativePath = accessUri.substring((UploadService.UPLOAD_URI + typeStr).length());
			UploadService uploadService =(UploadService) ServiceUtils.getService(UploadService.class);
			response.getOutputStream().write(uploadService.load(type , relativePath));
			response.getOutputStream().flush();
		}
		catch(IOException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	public String getServletInfo() {
		return null;
	}

	public void destroy() {

	}

}
