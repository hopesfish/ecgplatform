package com.ainia.ecgApi.service.sys;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.sys.SystemConfig;

/**
 * <p>SystemConfig Service interface </p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * SystemConfigService.java
 * @author pq
 * @createdDate 2013-07-17
 * @version 0.1
 */
public interface SystemConfigService extends BaseService<SystemConfig , Long> {
    
	/**
	 * <p>根据键值获取参数值</p>
	 * @param key
	 * @return
	 * String
	 */
	public String findByKey(String key);
}
