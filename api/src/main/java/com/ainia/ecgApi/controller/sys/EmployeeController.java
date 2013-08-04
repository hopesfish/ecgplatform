package com.ainia.ecgApi.controller.sys;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.core.utils.EncodeUtils;
import com.ainia.ecgApi.core.web.AjaxResult;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.domain.sys.User;
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
	@Autowired
	private AuthenticateService authenticateService;


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
		Employee employee = employeeService.get(id);
		if (employee == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
			employeeService.resetPassword(id);
		}
		else {
			employeeService.changePassword(id, oldPassword, newPassword);
		}
		employee.setSalt(EncodeUtils.encodeHex(EncodeUtils.asciiSum(employee.getPassword()).getBytes()));
		employeeService.update(employee);
		
		Map result = new HashMap(1);
		result.put(AjaxResult.AUTH_TOKEN , authenticateService.generateToken(employee.getUsername() , 
												Employee.class.getSimpleName(), employee.getSalt()));
		return new ResponseEntity(result , HttpStatus.OK);
	}
	
	/**
	 * <p>员工在线刷新下信息</p>
	 * @param employeeId
	 * @return
	 * ResponseEntity
	 */
	@RequestMapping(value = "{id}/live" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity live(@PathVariable("id") Long id) {
		Employee employee = employeeService.get(id);
		if (employee == null) {
			return new ResponseEntity(HttpStatus.OK);
		}
		AuthUser currentUser = authenticateService.getCurrentUser();
		if (!currentUser.isEmployee() || !employee.getId().equals(currentUser.getId())) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		employee.live();
		employeeService.update(employee);
		return new ResponseEntity(HttpStatus.OK);
	}

	
}
