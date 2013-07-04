package com.ainia.ecgApi.dao.health;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.health.HealthRuleReply;


/**
 * <p>HealthRuleReply Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthRuleReplyDao.java
 * @author pq
 * @createdDate 2013-07-04
 * @version 0.1
 */
public interface HealthRuleReplyDao extends JpaRepository<HealthRuleReply , Long>, BaseDao<HealthRuleReply , Long> { 
    
    
}
