package com.ainia.ecgApi.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.sys.EmployeeDao;
import com.ainia.ecgApi.domain.sys.Employee;

/**
 * <p>Employee Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * EmployeeServiceImpl.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.1
 */
@Service
public class EmployeeServiceImpl extends BaseServiceImpl<Employee , Long> implements EmployeeService {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Override
	public BaseDao<Employee, Long> getBaseDao() {
		return employeeDao;
	}

	public void setEmployeeDao(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}

	public Employee findByUsername(String username) {
		return employeeDao.findByUsername(username);
	}

	public boolean checkPassword(String target, String source) {
		return target.equals(source);
	}

	public String generateToken(String username) {
		return username;
	}

	
	
}
