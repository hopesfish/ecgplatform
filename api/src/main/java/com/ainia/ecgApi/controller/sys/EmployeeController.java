package com.ainia.ecgApi.controller.sys;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.service.sys.EmployeeService;

/**
 * <p>employee controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * EmployeeController.java
 * @author pq
 * @createdDate 2013-6-21
 * @version 0.1
 */
@Controller
@RequestMapping("api/employee")
public class EmployeeController extends BaseController<Employee , Long> {

	@Autowired
	private EmployeeService employeeService;


	@Override
	public BaseService<Employee, Long> getBaseService() {
		return employeeService;
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	/**
	 * <p>根据用户名查询</p>
	 * ResponseEntity<Employee>
	 * @param username
	 * @return
	 */
	@RequestMapping("byName/{username}")
	public ResponseEntity<Employee> getByUsername(String username) {
		if (true) {
			throw new ServiceException("test");
		}
		Employee employee = employeeService.findByUsername(username);
		if (employee == null) {
			return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Employee>(employee , HttpStatus.OK);
	}
	
	/**
	 * <p>修改密码</p>
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * ResponseEntity
	 */
	@RequestMapping(value = "{id}/password" ,method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity changePassword(@PathVariable("id") Long id , 
										 @RequestParam(value = "oldPassword" , required = false) String oldPassword ,
										 @RequestParam(value = "newPassword" , required = false) String newPassword) {
		if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
			employeeService.resetPassword(id);
		}
		else {
			employeeService.changePassword(id, oldPassword, newPassword);
		}
		return new ResponseEntity(HttpStatus.OK);
	}
	
}
