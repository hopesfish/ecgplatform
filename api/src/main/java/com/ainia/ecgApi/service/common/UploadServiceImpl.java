package com.ainia.ecgApi.service.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.service.sys.SystemConfigService;

/**
 * <p>文件保存服务</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * UploadService.java
 * @author pq
 * @createdDate 2013-7-12
 * @version
 */
@Service
public class UploadServiceImpl implements UploadService {

	@Autowired
	private SystemConfigService systemConfigService;
	
	//TODO 暂时固定
	//public static final String rootPath = "d:/upload/";
	public static final String ROOT_PATH_KEY = "upload.rootPath";
	
	public String getPath(Type type , String relativePath) {
		String rootPath     = systemConfigService.findByKey(ROOT_PATH_KEY);
		String path = rootPath + "/"  + type.name()  + "/" + relativePath;
		return path;
	}
	
	public String save(Type type , String relativePath , byte[] content) throws IOException{
		if (Type.heart_img.equals(type)) {
			return saveHeartImg(type , relativePath , content);
		}
		throw new ServiceException("upload.error.unknown");
	}
	
	public String saveHeartImg(Type type , String relativePath , byte[] content) throws IOException {
		String path = getPath(type , relativePath);
		FileUtils.writeByteArrayToFile(new File(path), content);
		return UPLOAD_URI + type.name() + "/" + relativePath;
	}
	
	public byte[] load(Type type , String relativePath) throws IOException {
		if (Type.heart_img.equals(type)) {
			return loadHeartImg(type ,  relativePath);
		}
		throw new ServiceException("upload.error.unknown");		
	}
	
	public byte[] loadHeartImg(Type  type , String relativePath) throws IOException {
		String path = getPath(type , relativePath);
		return FileUtils.readFileToByteArray(new File(path));
	}
			
	
}
