package com.ainia.ecgApi.dao.health;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.health.HealthReply;


/**
 * <p>HealthReply Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthReplyDao.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
public interface HealthReplyDao extends JpaRepository<HealthReply , Long>, BaseDao<HealthReply , Long> { 
    
    
}
