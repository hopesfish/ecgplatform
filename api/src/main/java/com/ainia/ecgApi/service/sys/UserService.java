package com.ainia.ecgApi.service.sys;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.dto.common.Message.Type;

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
    
	public static final int SALT_NUM = 6;
	/**
	 * <p>根据用户名获得用户</p>
	 * @param username
	 * @return
	 * User
	 */
	public User findByUsername(String username);
	
	/**
	 * <p>根据电话获得用户</p>
	 * @param username
	 * @return
	 * User
	 */
	public User findByMobile(String mobile);
	/**
	 * <p>根据邮箱获得用户</p>
	 * @param username
	 * @return
	 * User
	 */
	public User findByEmail(String email);
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
	
	/**
	 * <p>找回密码</p>
	 * @param email
	 * void
	 */
	public void retakePassword(User user , Type messageType);
	
	/**
	 * <p>通过找回码修改密码</p>
	 * @param userId
	 * @param code
	 * @param newPassword
	 * void
	 */
	public void retakePassword(User user , String code , String newPassword);
}
