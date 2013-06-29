package com.ainia.ecgApi.controller.security;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.core.security.AuthenticateService;
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
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AuthenticateService authenticateService;

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
		if (!authenticateService.checkPassword(employee.getPassword() , password , null)) {
			ajaxResult.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
		else {
			ajaxResult.setStatus(HttpStatus.OK.value());
			//TODO 用户类型暂时硬编码为 员工
			result.put(AjaxResult.AUTH_TOKEN , authenticateService.generateToken(username , Employee.class.getSimpleName()));
		}
		return new ResponseEntity<Map<String , String>>(result, HttpStatus.valueOf(ajaxResult.getStatus()));
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}
	
}
