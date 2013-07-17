package com.ainia.ecgApi.service.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.service.common.UploadServiceImpl.Type;

/**
 * <p>文件上传服务</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * UploadService.java
 * @author pq
 * @createdDate 2013-7-12
 * @version
 */
public interface UploadService {

	/**
	 * <p>保存上传文件</p>
	 * @param type
	 * @param key
	 * @param content
	 * @return
	 * String
	 */
	public String save(Type type , String key , byte[] content) throws IOException;
	/**
	 * <p>读取上传文件</p>
	 * @param type
	 * @param relativePath
	 * @return
	 * @throws IOException
	 * byte[]
	 */
	public byte[] load(Type type , String relativePath) throws IOException;
	/**
	 * <p>读取心跳数据上传文件</p>
	 * @param type
	 * @param relativePath
	 * @return
	 * @throws IOException
	 * byte[]
	 */
	public byte[] loadHeartImg(Type  type , String relativePath) throws IOException;
}
