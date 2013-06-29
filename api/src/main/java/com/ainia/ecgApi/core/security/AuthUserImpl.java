package com.ainia.ecgApi.core.security;

import java.util.List;
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

	private Long id;
	private String usernmame;
	private String type;
	private String[] roles;
	
	
	
	public AuthUserImpl(Long id, String usernmame, String type , String...roles) {
		super();
		this.id = id;
		this.usernmame = usernmame;
		this.type = type;
		this.roles = roles;
	}

	public Long getId() {
		return null;
	}

	public String getUsername() {
		return null;
	}

	public String getUsernmame() {
		return usernmame;
	}

	public String getType() {
		return type;
	}

	public String[] getRoles() {
		return roles;
	}

}
