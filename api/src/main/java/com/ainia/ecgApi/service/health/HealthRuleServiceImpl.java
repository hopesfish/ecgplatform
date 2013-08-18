package com.ainia.ecgApi.service.health;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.dao.health.HealthRuleDao;
import com.ainia.ecgApi.domain.health.HealthRule;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.service.sys.UserService;

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
    
    @Autowired
    private UserService userService;
    
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
	
	/**
	 * 返回通用的filter以及定制的fulter
	 */
	public List<HealthRule> findAllFiltersByUser(Long userId) {
		List<HealthRule> filters = new ArrayList <HealthRule> ();
		
		Query ruleQuery = new Query();
		ruleQuery.eq(HealthRule.USAGE, HealthRule.Usage.group);
		ruleQuery.isNull(HealthRule.EMPLOYEE_ID);
		ruleQuery.isNull(HealthRule.GROUP_ID);
		List<HealthRule> sysRules = healthRuleDao.findAll(ruleQuery);
		
		User user = userService.get(userId);
		List<HealthRule> userRules = new ArrayList <HealthRule> ();
		userRules.addAll(user.getRules());
		
		for (HealthRule sysRule : sysRules) {
			Query filterQuery = new Query();
			filterQuery.eq(HealthRule.USAGE, HealthRule.Usage.filter);
			boolean found = false;
			HealthRule foundUserRule = null;
			
			for (HealthRule userRule : userRules) {
				if (userRule.getCode().equals(sysRule.getCode())) {
					found = true;
					foundUserRule = userRule;
					break;
				}
			}
			
			if (found) {
				filterQuery.eq(HealthRule.GROUP_ID, foundUserRule.getId());
			} else {
				filterQuery.eq(HealthRule.GROUP_ID, sysRule.getId());
			}
			filters.addAll(healthRuleDao.findAll(filterQuery));
		}
		
		return filters;
	};
}
