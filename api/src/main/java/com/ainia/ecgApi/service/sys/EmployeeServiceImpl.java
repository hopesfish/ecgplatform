package com.ainia.ecgApi.service.sys;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.core.utils.PropertyUtil;
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
	public Employee create(Employee employee) {
		employee.setPassword(authenticateService.encodePassword(employee.getPassword() , null));
		return super.create(employee);
	}
	

	@Override
	public Employee update(Employee employee) {
		//not allowed change the password
		Employee old = employeeDao.findOne(employee.getId());
		List<String> excludes = new ArrayList<String>(1);
		excludes.add(Employee.PASSWORD);
		PropertyUtil.copyProperties(old , employee , excludes);
		return super.update(employee);
	}

	@Override
	public Employee patch(Employee employee) {
		//not allowed change the password
		Employee old = employeeDao.findOne(employee.getId());
		employee.setPassword(old.getPassword());
		return super.patch(employee);
	}

	public void changePassword(Long id, String oldPassword,
			String newPassword) {
		Employee employee = this.get(id);
		AuthUser currentUser = authenticateService.getCurrentUser();
		if (employee == null) {
			throw new ServiceException("exception.notFound");
		}
		if (!currentUser.isSuperAdmin() && !employee.getUsername().equals(currentUser.getUsername())) {
			throw new ServiceException("exception.password.cannotChange");
		}
		
		if (!authenticateService.checkPassword(employee.getPassword() , oldPassword , null)) {
			throw new ServiceException("exception.oldPassword.notEquals");
		}
		employee.setPassword(authenticateService.encodePassword(newPassword , null));
		this.employeeDao.save(employee);
	}
	
	public void resetPassword(Long id) {
		Employee employee = this.get(id);
		AuthUser currentUser = authenticateService.getCurrentUser();
		if (employee == null) {
			throw new ServiceException("exception.notFound");
		}
		if (!currentUser.isSuperAdmin() && !employee.getUsername().equals(currentUser.getUsername())) {
			throw new ServiceException("exception.password.cannotChange");
		}
		employee.setPassword(authenticateService.encodePassword(employee.getUsername() , null));
		this.employeeDao.save(employee);
	}

	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}

}
