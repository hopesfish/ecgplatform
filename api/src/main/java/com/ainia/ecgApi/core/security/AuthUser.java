package com.ainia.ecgApi.core.security;

import java.util.List;

/**
 * <p>auth User</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * AuthUser.java
 * @author pq
 * @createdDate 2013-6-29
 * @version
 */
public interface AuthUser {

	/**
	 * <p>user identity</p>
	 * @return
	 * Long
	 */
	public Long   getId();
	/**
	 * <p>get username</p>
	 * @return
	 * String
	 */
	public String getUsername();
	/**
	 * <p>get user type</p>
	 * @return
	 * String
	 */
	public String getType();
	/**
	 * <p>get user roles</p>
	 * @return
	 * String[]
	 */
	public String[] getRoles();
	/**
	 * <p>the user is super admin</p>
	 * @return
	 * boolean
	 */
	public boolean isSuperAdmin();
	
	/**
	 * <p>the user is chief</p>
	 * @return
	 * boolean
	 */
	public boolean isChief();
}
