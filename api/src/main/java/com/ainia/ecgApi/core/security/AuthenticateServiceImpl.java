package com.ainia.ecgApi.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.utils.DigestUtils;
import com.ainia.ecgApi.core.utils.EncodeUtils;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.service.sys.EmployeeService;

/**
 * <p>用户权限服务</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * AuthenticateServiceImpl.java
 * @author pq
 * @createdDate 2013-6-29
 * @version
 */
@Service
public class AuthenticateServiceImpl implements AuthenticateService {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	
	protected final Logger log = LoggerFactory.getLogger("com.ainia.ecgApi.core.security");
	
	private ThreadLocal<AuthUser> currentUser = new ThreadLocal<AuthUser>();
	@Autowired
	private EmployeeService employeeService;

	public String encodePassword(String password , byte[] salt) {
		byte[] hashPassword = DigestUtils.sha1(password.getBytes() , salt, HASH_INTERATIONS);
		return EncodeUtils.encodeHex(hashPassword);
	}

	public boolean checkPassword(String target, String source , byte[] salt) {
		String hashPassword = this.encodePassword(source , null);
		if (log.isDebugEnabled()) {
			log.debug(String.format("compare the password %s %s" , hashPassword , target));
		}
		return hashPassword.equals(target);
	}

	public String generateToken(String username , String userType) {
		//TODO 测试阶段 token 使用明文用户名
		return username;
	}
	
	public AuthUser loadUserByToken(String token) {
		//TODO 测试阶段 token 使用明文用户名 且固定为employee 用户
		Employee employee = employeeService.findByUsername(token);
		return new AuthUserImpl(employee.getId() , employee.getUsername() , Employee.class.getSimpleName() , employee.getRolesArray());
	}

	public void setCurrentUser(AuthUser authUser) {
		if (currentUser.get() == null) {
			currentUser.set(authUser);
		}
	}

	public AuthUser getCurrentUser() {
		return currentUser.get();
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
