package com.ainia.ecgApi.service.sys;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.sys.Employee;

/**
 * <p>Employee Service interface </p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * EmployeeService.java
 * @author pq
 * @createdDate 2013-6-21
 * @version
 */
public interface EmployeeService  extends BaseService<Employee , Long> {

	/**
	 * <p>get unique employee by username</p>
	 * Employee
	 * @param username
	 * @return
	 */
	public Employee findByUsername(String username);
	/**
	 * <p>修改密码</p>
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	public void changePassword(Long id , String oldPassword , String newPassword);
	
}
