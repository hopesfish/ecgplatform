package com.ainia.ecgApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.domain.Employee;
import com.ainia.ecgApi.service.EmployeeService;

/**
 * <p>employee controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * EmployeeController.java
 * @author Administrator
 * @createdDate 2013-6-21
 * @version 0.1
 */
@Controller
@RequestMapping("employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping(value="" , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Employee> index() {
		return employeeService.findAll();
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	
}
