package com.ainia.ecgApi.core.security;

/**
 * <p>安全验证服务</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * AuthenticateService.java
 * @author pq
 * @createdDate 2013-6-29
 * @version
 */
public interface AuthenticateService {

	/**
	 * <p>对密码进行编码</p>
	 * @param password
	 * @oaran salt
	 * @return
	 * String
	 */
	public String encodePassword(String password , byte[] salt);
	/**
	 * <p>check the password</p>
	 * boolean
	 * @param target input password
	 * @param source saved password
	 * @param salt
	 * @return
	 */
	public boolean checkPassword(String target , String source , byte[] salt);
	/**
	 * <p>generate rest token</p>
	 * boolean
	 * @param username
	 * @return
	 */
	public String generateToken(String username , String userType);
	/**
	 * <p>decode toke get the username</p>
	 * @param token
	 * @return
	 * String
	 */
	public AuthUser loadUserByToken(String token);
	/**
	 * 
	 * <p>set current user</p>
	 * @param authUser
	 * void
	 */
	public void setCurrentUser(AuthUser authUser);
	/**
	 * <p>get current user</p>
	 * @return
	 * AuthUser
	 */
	public AuthUser getCurrentUser();
}
