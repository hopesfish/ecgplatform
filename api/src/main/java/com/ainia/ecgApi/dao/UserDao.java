package com.ainia.ecgApi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ainia.ecgApi.domain.User;

/**
 * <p>User data access object</P>  
 * @author  pq
 * @createDate 2013-6-21
 * @version
 */
public interface UserDao extends JpaRepository<User , Long> {

}
