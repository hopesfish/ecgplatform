package com.ainia.ecgApi.service.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.exception.ServiceException;
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
    	return healthRuleDao.findByCode(code);
    }

	public void addUser(Long ruleId, Long userId) {
		HealthRule rule = this.get(ruleId);
		if (!HealthRule.USAGE_GROUP.equals(rule.getUsage())) {
			throw new ServiceException("healthRule.error.create.notGroup");
		}
		healthRuleDao.addUser(ruleId, userId);
	}

	public void removeUser(Long ruleId, Long userId) {
		healthRuleDao.removeUser(ruleId, userId);
	}

	public void deleteByGroup(Long groupId) {
		healthRuleDao.deleteByGroup(groupId);
	}
}
