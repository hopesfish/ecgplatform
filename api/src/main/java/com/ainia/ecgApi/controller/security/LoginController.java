package com.ainia.ecgApi.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.core.web.AjaxResult;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.service.sys.EmployeeService;

/**
 * <p>login controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * LoginController.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
@Controller
@RequestMapping("api/login")
public class LoginController {
	
	@Autowired
	private EmployeeService employeeService;

	/**
	 * <p>check the user login</p>
	 * void
	 */
	@RequestMapping(method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AjaxResult> login(@RequestParam("username") String username , 
					  @RequestParam("password") String password) {
		Employee employee = employeeService.findByUsername(username);
		AjaxResult ajaxResult = new AjaxResult();
		HttpHeaders headers = new HttpHeaders();
		if (employee == null) {
			ajaxResult.setStatus(HttpStatus.NOT_FOUND.value());
		}
		if (!employeeService.checkPassword(employee.getPassword() , password)) {
			ajaxResult.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
		else {
			ajaxResult.setStatus(HttpStatus.OK.value());
			headers.add(AjaxResult.AUTH_HEADER , employeeService.generateToken(employee.getUsername()));
		}
		return new ResponseEntity<AjaxResult>(ajaxResult , headers , HttpStatus.valueOf(ajaxResult.getStatus()));
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	
}
