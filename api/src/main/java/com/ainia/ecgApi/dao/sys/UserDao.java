package com.ainia.ecgApi.dao.sys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.sys.User;


/**
 * <p>User Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * UserDao.java
 * @author pq
 * @createdDate 2013-06-22
 * @version 0.1
 */
public interface UserDao extends JpaRepository<User , Long>, BaseDao<User , Long> { 
    
    /**
     * <p>根据用户名获得用户</p>
     * @param username
     * @return
     * User
     */
	public User findByUsername(String username);
	/**
	 * <p>通过电话获得用户</p>
	 * @param mobile
	 * @return
	 * User
	 */
	public User findByMobile(String mobile);
	/**
	 * <p>通过email 获得用户</p>
	 * @return
	 * User
	 */
	public User findByEmail(String email);
}
