package com.ainia.ecgApi.core.security;

import java.util.Arrays;

import com.ainia.ecgApi.domain.sys.Chief;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.domain.sys.Expert;
import com.ainia.ecgApi.domain.sys.Operator;
import com.ainia.ecgApi.domain.sys.User;
/**
 * <p>default auth user</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * AuthUserImpl.java
 * @author pq
 * @createdDate 2013-6-29
 * @version
 */
public class AuthUserImpl implements AuthUser {
	
	public static final String ROLE_CHIEF = "chief";

	private Long id;
	private String name;
	private String username;
	private String type;
	private String[] roles;
	
	
	
	public AuthUserImpl(Long id, String name , String username, String type , String...roles) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.type = type;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getType() {
		return type;
	}

	public String[] getRoles() {
		return roles;
	}

	public boolean isSuperAdmin() {
		return this.id != null && new Long(1).equals(this.getId());
	}
	
	public boolean isChief() {
		if (this.roles != null) {
			for (String role : roles) {
				if (ROLE_CHIEF.equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "AuthUserImpl [id=" + id + ", username=" + username
				+ ", type=" + type + ", roles=" + Arrays.toString(roles) + "]";
	}

	public boolean isUser() {
		return User.class.getSimpleName().equals(type);
	}

	public boolean isEmployee() {
		return Employee.class.getSimpleName().equals(type) ||
				Chief.class.getSimpleName().equals(type) ||
				Operator.class.getSimpleName().equals(type) ||
				Expert.class.getSimpleName().equals(type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
