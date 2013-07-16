package com.ainia.ecgApi.service.sys;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.utils.PropertyUtil;
import com.ainia.ecgApi.dao.sys.UserDao;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.domain.sys.User;

/**
 * <p>User Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * UserServiceImpl.java
 * @author pq
 * @createdDate 2013-06-22
 * @version 0.1
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User , Long> implements UserService {
    
    @Autowired
    private UserDao userDao;
    public BaseDao<User , Long> getBaseDao() {
        return userDao;
    }

	@Override
	public User update(User user) {
		//not allow change the password
		User old =  userDao.findOne(user.getId());
		List<String> excludes = new ArrayList<String>(1);
		excludes.add(Employee.PASSWORD);
		PropertyUtil.copyProperties(old , user , excludes);
		return userDao.save(old);
	}

	@Override
	public User patch(User user) {
		//not allow change the password
		User old = userDao.findOne(user.getId());
		user.setPassword(old.getPassword());
		return userDao.save(user);
	}

	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

    
}
