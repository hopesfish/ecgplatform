package com.ainia.ecgApi.controller.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadService.Type;

/**
 * <p>上传附件controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * UploadController.java
 * @author pq
 * @createdDate 2013-7-18
 * @version
 */
public class UploadController {
	
	
	public static final String UPLOAD_URI = "/upload/";
	@Autowired
	private UploadService uploadService;
	
	@RequestMapping(value = "/{type}/**" , method = RequestMethod.GET)
	public void accessFile(@PathVariable("type") Type type , HttpServletRequest request ,
								HttpServletResponse response) throws IOException {
		String accessUri = request.getRequestURI();
		response.setStatus(HttpStatus.OK.value());
//		String accessUri = request.getRequestURI();
//		String relativePath = accessUri.substring((UPLOAD_URI + type.name()).length());
//		return uploadService.load(type , relativePath );
	}
}
