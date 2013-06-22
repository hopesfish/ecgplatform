package com.ainia.ecgApi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.dao.UserDao;
import com.ainia.ecgApi.domain.User;

/**
 * <p>User Service</p>
 * UserService.java
 * @author Administrator
 * @createdDate 2013-6-21
 * @version
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public List<User> findAll() {
		return userDao.findAll();
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
}
