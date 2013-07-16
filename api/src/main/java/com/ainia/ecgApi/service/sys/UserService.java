package com.ainia.ecgApi.service.sys;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.domain.sys.User;

/**
 * <p>User Service interface </p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * UserService.java
 * @author pq
 * @createdDate 2013-06-22
 * @version 0.1
 */
public interface UserService extends BaseService<User , Long> {
    

	/**
	 * <p>根据用户名获得用户</p>
	 * @param username
	 * @return
	 * User
	 */
	public User findByUsername(String username);
	
	/**
	 * <p>修改密码</p>
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 * void
	 */
	public void changePassword(Long id, String oldPassword,
			String newPassword);
	
	/**
	 * <p>重置密码</p>
	 * @param id
	 * void
	 */
	public void resetPassword(Long id);
}
