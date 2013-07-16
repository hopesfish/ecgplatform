package com.ainia.ecgApi.dao.sys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.sys.SystemConfig;
import com.ainia.ecgApi.domain.sys.SystemConfig.Type;


/**
 * <p>SystemConfig Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * SystemConfigDao.java
 * @author pq
 * @createdDate 2013-07-17
 * @version 0.1
 */
public interface SystemConfigDao extends JpaRepository<SystemConfig , Long>, BaseDao<SystemConfig , Long> { 
    
	/**
	 * <p>根据键值和类型获取配置</p>
	 * @param key
	 * @param type
	 * @return
	 * SystemConfig
	 */
    public SystemConfig findByKeyAndType(String key , Type type);
}
