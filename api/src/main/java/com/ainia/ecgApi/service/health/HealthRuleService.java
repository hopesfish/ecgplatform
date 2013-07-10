package com.ainia.ecgApi.service.health;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.health.HealthRule;

/**
 * <p>HealthRule Service interface </p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthRuleService.java
 * @author pq
 * @createdDate 2013-07-04
 * @version 0.1
 */
public interface HealthRuleService extends BaseService<HealthRule , Long> {
    
	/**
	 * <p>根据code 获得一般健康规则（不包含特定用户规则）</p>
	 * @param code
	 * @return
	 * HealthRule
	 */
	public HealthRule findSimpleByCode(String code);
	
	/**
	 * <p>将规则绑定至用户</p>
	 * void
	 */
	public void addUser(Long ruleId , Long userId);
	/**
	 * <p>解除规则与用户绑定关系</p>
	 * @param ruleId
	 * @param userId
	 * void
	 */
	public void removeUser(Long ruleId , Long userId);
}
