package com.ainia.ecgApi.core.utils;

/**
 * <p>字符串工具类</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * StringUtils.java
 * @author pq
 * @createdDate 2013-8-17
 * @version
 */
public class StringUtils {

	public static String valueOf(Object value) {
		return valueOf(value , "");
	}
	/**
	 * <p>将指定数据转换字符串, 并提供默认值功能</p>
	 * @param value
	 * @param defaultValue
	 * @return
	 * String
	 */
	public static String valueOf(Object value , String defaultValue) {
		return value == null?defaultValue  : String.valueOf(value);
	}
}
