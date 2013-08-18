package com.ainia.ecgApi.service.apk;

import java.io.IOException;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.apk.Apk;

/**
 * <p>Apk Service interface </p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ApkService.java
 * @author pq
 * @createdDate 2013-08-07
 * @version 0.1
 */
public interface ApkService extends BaseService<Apk , Long> {
    
	/**
	 * <p>上传apk</p>
	 * @param apk
	 * @param content
	 * void
	 */
	public void upload(Apk apk , byte[] content) throws Exception ;
	
	/**
	 * <p>获取apk文件</p>
	 * @param apk
	 * @return
	 * @throws IOException
	 * byte[]
	 */
	public byte[] load(Apk apk) throws IOException;
}
