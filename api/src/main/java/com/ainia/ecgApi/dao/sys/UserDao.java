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
    
    
}
