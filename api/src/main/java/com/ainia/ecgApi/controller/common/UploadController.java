package com.ainia.ecgApi.controller.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadServiceImpl.Type;

@Controller
@RequestMapping("upload")
public class UploadController {
	
	
	public static final String UPLOAD_URI = "/upload/";
	@Autowired
	private UploadService uploadService;
	
	@RequestMapping(value = "/{type}/**/*.*" , method = RequestMethod.GET)
	@ResponseBody
	public byte[] accessFile(@PathVariable("type") Type type , HttpServletRequest request) throws IOException {
		String accessUri = request.getRequestURI();
		System.out.println("=============== " + accessUri);
		String relativePath = accessUri.substring((UPLOAD_URI + type.name()).length());
		System.out.println("=============== " + relativePath);
		return uploadService.load(type , relativePath );
	}
}
