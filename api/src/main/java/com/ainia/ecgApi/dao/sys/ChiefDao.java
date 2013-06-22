package com.ainia.ecgApi.dao.sys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.sys.Chief;

/**
 * <p>Chief Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ChiefDao.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.1
 */
public interface ChiefDao extends JpaRepository<Chief , Long> , BaseDao<Chief , Long> {

}
