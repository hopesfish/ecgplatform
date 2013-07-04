package com.ainia.ecgApi.dao.health;

import org.springframework.data.jpa.repository.JpaRepository;
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
	public HealthRule findByCodeAndUserIdIsNull(String code);
}
