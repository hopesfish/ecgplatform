package com.ainia.ecgApi.service.sys;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.dto.common.Message;
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
@TransactionConfiguration(defaultRollback = false)
public class UserServiceTest {

	@Autowired
	private UserService userService;	
    @Autowired
    private AuthenticateService authenticateService;
	
    /**
     * 测试执行3次后,retake相关信息会被清空
     */
	@Test
	public void testRetakePasswordError() {
		User user = userService.findByEmail("16259903@qq.com");
		userService.retakePassword(user, Message.Type.email);
		
		try {
			userService.retakePassword(user, "000000", "passw0rd");
		} catch(Exception e) {
		}
		
		user = userService.findByEmail("16259903@qq.com");
		Assert.assertNotNull(user.getRetakeCount());
		Assert.assertEquals(user.getRetakeCount().intValue(), 1);
		
		try {
			userService.retakePassword(user, "000000", "passw0rd");
		} catch(Exception e) {
		}
		
		try {
			userService.retakePassword(user, "000000", "passw0rd");
		} catch(Exception e) {
			
		}
		user = userService.findByEmail("16259903@qq.com");
		Assert.assertEquals(user.getRetakeCount(), null);
	} 
	
    /**
     * 成功修改后,retake信息会被清空
     */
	@Test
	public void testRetakePasswordSuccess() {
		User user = userService.findByEmail("110486432@qq.com");
		Assert.assertEquals(user.getRetakeCode(), "888888");

		try {
			userService.retakePassword(user, "888888", "passw0rdtest");
		} catch(Exception e) {
			
		}
		
		user = userService.findByEmail("110486432@qq.com");

		Assert.assertEquals(user.getRetakeCode(), null);
		Assert.assertEquals(user.getRetakeDate(), null);
		Assert.assertEquals(user.getRetakeCount(), null);
	}
}
