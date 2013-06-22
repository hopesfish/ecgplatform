package com.ainia.ecgApi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.dao.EmployeeDao;
import com.ainia.ecgApi.domain.Employee;

/**
 * <p>Employee Service</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * EmployeeService.java
 * @author Administrator
 * @createdDate 2013-6-21
 * @version
 */
@Service
public class EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;
	
	public List<Employee> findAll() {
		return employeeDao.findAll();
	}

	public void setEmployeeDao(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}
	
}
