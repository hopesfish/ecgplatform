package com.ainia.ecgApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.domain.User;
import com.ainia.ecgApi.service.UserService;

/**
 * <p>User Controller</p>
 * UserController.java
 * @author Administrator
 * @createdDate 2013-6-21
 * @version
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value="" , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> index() {
		return userService.findAll();
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
}
