package com.ainia.ecgApi.core.security;

import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.utils.DigestUtils;
import com.ainia.ecgApi.core.utils.EncodeUtils;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.service.sys.EmployeeService;
import com.ainia.ecgApi.service.sys.UserService;

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
	private static final String TOKEN_SPLIT = "_";
	
	protected final Logger log = LoggerFactory.getLogger("com.ainia.ecgApi.core.security");
	
	private ThreadLocal<AuthUser> currentUser = new ThreadLocal<AuthUser>();
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private UserService userService;

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

	public String generateToken(String username , String userType , Date tokenDate , String salt) {
		String code = username + TOKEN_SPLIT + userType;
		String token = code + TOKEN_SPLIT + new DateTime(tokenDate).monthOfYear() + TOKEN_SPLIT +
										EncodeUtils.encodeHex(DigestUtils.md5(code.getBytes() , EncodeUtils.decodeHex(salt)));
		return  EncodeUtils.encodeBase64(token.getBytes());
	}
	
	public AuthUser loadUserByToken(String tokenStr) {
		String token = new String(EncodeUtils.decodeBase64(tokenStr));
		if (token.indexOf(TOKEN_SPLIT) == -1) {
			throw new ServiceException("exception.token.invalid");
		}
		
		
		log.debug("token is " + token);
		String[] value = token.split(TOKEN_SPLIT);
		String username = value[0];
		String userType = value[1];
		AuthUser authUser = null;
		if (User.class.getSimpleName().equals(userType)) {
			User user = userService.findByUsername(username);
			Date tokenDate = user.getTokenDate();
			if (tokenDate == null || new DateTime().minusDays(30).isAfter(tokenDate.getTime())) {
				throw new ServiceException("exception.auth.token.invalid");
			}
			if (user != null) {
				String salt = user.getSalt();
				if (!generateToken(username , userType ,  user.getTokenDate() , salt).equals(tokenStr)) {
					throw new ServiceException("exception.auth.token.invalid");
				}
				authUser = new AuthUserImpl(user.getId() ,user.getName() , user.getUsername() , User.class.getSimpleName());
			}
		}
		else if (Employee.class.getSimpleName().equals(userType)) {
			Employee employee = employeeService.findByUsername(username);
			Date tokenDate = employee.getTokenDate();
			if (tokenDate == null || new DateTime().minusDays(30).isAfter(tokenDate.getTime())) {
				throw new ServiceException("exception.auth.token.invalid");
			}
			if (employee != null) {
				String salt = employee.getSalt();
				if (!generateToken(username , userType ,  employee.getTokenDate() , salt).equals(tokenStr)) {
					throw new ServiceException("exception.auth.token.invalid");
				}
				authUser =  new AuthUserImpl(employee.getId() , employee.getName() , employee.getUsername() , Employee.class.getSimpleName() , employee.getRolesArray());
			}
		}
		if (authUser == null) {
			throw new ServiceException("auth.error.unknown");
		}
		log.debug("load the user " + authUser + " by token " + token);
		return authUser;
	}

	public void setCurrentUser(AuthUser authUser) {
		currentUser.set(authUser);
	}

	public AuthUser getCurrentUser() {
		return currentUser.get();
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
