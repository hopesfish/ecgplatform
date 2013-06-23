package com.ainia.ecgApi.controller.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("api")
public class LoginController {
	
	@Autowired
	private EmployeeService employeeService;

	/**
	 * <p>check the user login</p>
	 * void
	 */
	@RequestMapping(value = "auth" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Map<String , String>> login(@RequestParam("username") String username , 
					  @RequestParam("password") String password) {
		Employee employee = employeeService.findByUsername(username);
		AjaxResult ajaxResult = new AjaxResult();
		Map<String , String> result = new HashMap(1);
		if (employee == null) {
			ajaxResult.setStatus(HttpStatus.NOT_FOUND.value());
		}
		if (!employeeService.checkPassword(employee.getPassword() , password)) {
			ajaxResult.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
		else {
			ajaxResult.setStatus(HttpStatus.OK.value());
			result.put(AjaxResult.AUTH_TOKEN , employeeService.generateToken(username));
		}
		return new ResponseEntity<Map<String , String>>(result, HttpStatus.valueOf(ajaxResult.getStatus()));
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	
}
