package com.ainia.ecgApi.core.utils;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

import org.springframework.util.Assert;

/**
 * <p>加密 解密 工具类</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * DigestUtils.java
 * @author pq
 * @createdDate 2013-6-29
 * @version
 */
public final class DigestUtils {

	public static final String SHA1 = "sha-1";
	public static final String MD5  = "MD5";
	public static final String DES  = "DES";
	
	private static SecureRandom random = new SecureRandom();
	
	public static byte[] sha1(byte[] bytes) {
		return digest(bytes , SHA1 , null , 1);
	}
	
	public static byte[] sha1(byte[] bytes , byte[] salt){
		return digest(bytes , SHA1 , salt , 1);
	}
	
	public static byte[] sha1(byte[] bytes , byte[] salt ,int iterations){
		return digest(bytes , SHA1 , salt , iterations);
	}
	
	public static byte[] md5(byte[] bytes) {
		return digest(bytes , MD5 , null , 1);
	}
	
	public static byte[] md5(byte[] bytes , byte[] salt){
		return digest(bytes , MD5 , salt , 1);
	}
	
	public static byte[] md5(byte[] bytes , byte[] salt ,int iterations){
		return digest(bytes , MD5 , salt , iterations);
	}
	
	/**
	 * <p>对字符串进行散列  支持MD5 和  SHA1</p>
	 * @param input
	 * @param algorithm
	 * @param salt
	 * @param iterations
	 * @return
	 * byte[]
	 */
	private static byte[] digest(byte[] input , 
								String algorithm ,
								byte[] salt ,
								int iterations
			) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			
			if (salt != null) {
				digest.update(salt);
			}
			
			byte[] result = digest.digest(input);
			
			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;
		}catch(GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	/**
	 * <p>生成随机 salt</p>
	 * @param num byte 数组大小
	 * @return
	 * byte[]
	 */
	public static byte[] generateSalt(int num) {
		Assert.isTrue(num > 0 , "salt num must be larger than 0");
		byte[] bytes = new byte[num];
		random.nextBytes(bytes);
		return bytes;
	}
}
