package com.ainia.ecgApi.service.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.health.HealthRuleDao;
import com.ainia.ecgApi.domain.health.HealthRule;

/**
 * <p>HealthRule Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthRuleServiceImpl.java
 * @author pq
 * @createdDate 2013-07-04
 * @version 0.1
 */
@Service
public class HealthRuleServiceImpl extends BaseServiceImpl<HealthRule , Long> implements HealthRuleService {
    
    @Autowired
    private HealthRuleDao healthRuleDao;
    
    @Override
    public BaseDao<HealthRule , Long> getBaseDao() {
        return healthRuleDao;
    }

    public HealthRule findSimpleByCode(String code) {
    	return healthRuleDao.findByCodeAndUserIdIsNull(code);
    }
}
