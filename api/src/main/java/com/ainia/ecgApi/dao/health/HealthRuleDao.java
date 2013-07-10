package com.ainia.ecgApi.dao.health;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.health.HealthRule;


/**
 * <p>HealthRule Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthRuleDao.java
 * @author pq
 * @createdDate 2013-07-04
 * @version 0.1
 */
public interface HealthRuleDao extends JpaRepository<HealthRule , Long>, BaseDao<HealthRule , Long> { 
    
    /**
     * <p>根据code 获得健康规则</p>
     * @param code
     * @return
     * HealthRule
     */
	public HealthRule findByCode(String code);
	
	
	/**
	 * <p>将规则绑定至用户</p>
	 * void
	 */
	@Query(nativeQuery = true , value = "insert into health_rule_user (rule_id , user_id ) values (? , ?)")
	@Modifying
	public void addUser(Long ruleId , Long userId);
	/**
	 * <p>解除规则与用户绑定关系</p>
	 * @param ruleId
	 * @param userId
	 * void
	 */
	@Query(nativeQuery = true , value = "delete from health_rule_user where rule_id = ? and user_id = ? ")
	@Modifying
	public void removeUser(Long ruleId , Long userId);
}
