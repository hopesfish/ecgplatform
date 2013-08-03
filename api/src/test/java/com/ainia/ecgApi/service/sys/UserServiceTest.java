package com.ainia.ecgApi.service.sys;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.dto.common.Message.Type;

/**
 * <p>用户测试</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * UserServiceTest.java
 * @author pq
 * @createdDate 2013-8-3
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class UserServiceTest {

	@Autowired
	private UserService userService;	 
	
	@Test
	public void testRetakePassword() {
	}
}
