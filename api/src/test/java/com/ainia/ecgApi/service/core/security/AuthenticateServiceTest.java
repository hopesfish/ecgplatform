package com.ainia.ecgApi.service.core.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

import com.ainia.ecgApi.core.security.AuthenticateService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
public class AuthenticateServiceTest {
	@Autowired
	private AuthenticateService authenticateService;

	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}

	@Test
	public void testCheckPassword() {
		String password = "passw0rd";
		String encodePassword = authenticateService.encodePassword(password, null);
		System.out.println(String.format(">>> encode password:%s to " + encodePassword , password));
		Assert.isTrue(encodePassword.equals(authenticateService.encodePassword(password, null)));
	}
}
