package com.ainia.ecgApi.dao.sys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.sys.Employee;

/**
 * <p>Employee Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * EmployeeDao.java
 * @author pq
 * @createdDate 2013-6-21
 * @version 0.1
 */
public interface EmployeeDao extends JpaRepository<Employee , Long> , BaseDao<Employee , Long>{


	/**
	 * <p>find by username</p>
	 * Employee
	 * @param username
	 * @return
	 */
	public Employee findByUsername(String username);
}
