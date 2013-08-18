package com.ainia.ecgApi.dao.sys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.sys.Operator;

/**
 * <p>operator Dao</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * OperatorDao.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.1
 */
public interface OperatorDao extends JpaRepository<Operator, Long>, BaseDao<Operator , Long> {

}
