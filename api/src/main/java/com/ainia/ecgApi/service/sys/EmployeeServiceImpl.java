package com.ainia.ecgApi.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.security.AuthenticateService;
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
	@Autowired
	private AuthenticateService authenticateService;
	
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
	
	@Override
	public Employee save(Employee employee) {
		employee.setPassword(authenticateService.encodePassword(employee.getPassword() , null));
		return super.save(employee);
	}

	public void changePassword(Long id, String oldPassword,
			String newPassword) {
		Employee employee = this.get(id);
		if (employee == null) {
			throw new ServiceException("exception.notFound");
		}
		if (!employee.getUsername().equals(authenticateService.getCurrentUser().getUsername())) {
			throw new ServiceException("exception.password.changeByOwner");
		}
		
		if (!authenticateService.checkPassword(oldPassword , employee.getPassword() , null)) {
			throw new ServiceException("exception.oldPassword.notEquals");
		}
		employee.setPassword(newPassword);
		this.employeeDao.save(employee);
	}

	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}

}
