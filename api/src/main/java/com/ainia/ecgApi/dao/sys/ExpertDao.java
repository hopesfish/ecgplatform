package com.ainia.ecgApi.dao.sys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.sys.Expert;

/**
 * <p>Expert Dao </p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ExpertDao.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.1
 */
public interface ExpertDao extends JpaRepository<Expert , Long> , BaseDao<Expert , Long>{

}
