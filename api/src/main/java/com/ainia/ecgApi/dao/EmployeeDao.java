package com.ainia.ecgApi.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ainia.ecgApi.domain.Employee;

/**
 * <p>Employee Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * EmployeeDao.java
 * @author pq
 * @createdDate 2013-6-21
 * @version 0.1
 */
public interface EmployeeDao extends JpaRepository<Employee , Long> {


}
