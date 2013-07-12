package com.ainia.ecgApi.service.common;

import java.io.IOException;

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
}
