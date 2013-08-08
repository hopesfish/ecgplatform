package com.ainia.ecgApi.service.apk;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.dao.apk.ApkDao;
import com.ainia.ecgApi.domain.apk.Apk;
import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadService.Type;

/**
 * <p>Apk Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ApkServiceImpl.java
 * @author pq
 * @createdDate 2013-08-07
 * @version 0.1
 */
@Service
public class ApkServiceImpl extends BaseServiceImpl<Apk , Long> implements ApkService {
    
	
    @Autowired
    private ApkDao apkDao;
    @Autowired
    private UploadService uploadService;
    
    @Override
    public BaseDao<Apk , Long> getBaseDao() {
        return apkDao;
    }

	@Override
	public void upload(Apk apk, byte[] content) throws IOException {
		apkDao.save(apk);
		uploadService.save(Type.apk ,apk.getName() ,content);
	}
	
	public byte[] load(Apk apk) throws IOException {
		return uploadService.load(Type.apk ,apk.getName());
	}

	@Override
	public void delete(Long id) {
		super.delete(this.get(id));
	}

	@Override
	public void delete(Apk apk) {
		super.delete(apk);
		try {
			uploadService.remove(Type.apk ,apk.getName());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("exception.apk.delteFailed");
		}
	}
	
	

}
