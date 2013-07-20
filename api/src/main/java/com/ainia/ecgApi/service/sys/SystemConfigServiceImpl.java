package com.ainia.ecgApi.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.sys.SystemConfigDao;
import com.ainia.ecgApi.domain.sys.SystemConfig;
import com.ainia.ecgApi.domain.sys.SystemConfig.Type;

/**
 * <p>SystemConfig Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * SystemConfigServiceImpl.java
 * @author pq
 * @createdDate 2013-07-17
 * @version 0.1
 */
@Service
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig , Long> implements SystemConfigService {
    
    @Autowired
    private SystemConfigDao systemConfigDao;
    
    @Override
    public BaseDao<SystemConfig , Long> getBaseDao() {
        return systemConfigDao;
    }

	public String findByKey(String key) {
		SystemConfig sysConfig = systemConfigDao.findByKeyAndType(key , Type.basic);
		return sysConfig == null?null : sysConfig.getValue();
	}

}
