package com.ainia.ecgApi.dao.health;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.health.HealthExamination;


/**
 * <p>HealthExamination Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthExaminationDao.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
public interface HealthExaminationDao extends JpaRepository<HealthExamination , Long>, BaseDao<HealthExamination , Long> { 
    
    
}
