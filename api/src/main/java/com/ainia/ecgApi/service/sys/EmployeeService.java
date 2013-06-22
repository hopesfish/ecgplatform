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
	 * <p>check the password</p>
	 * boolean
	 * @param target
	 * @param source
	 * @return
	 */
	public boolean checkPassword(String target , String source);
	
	/**
	 * <p>generate rest token</p>
	 * boolean
	 * @param username
	 * @return
	 */
	public String generateToken(String username);
}
