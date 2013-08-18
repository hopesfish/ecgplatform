package com.ainia.ecgApi.core.utils;

/**
 * <p>数据值转换工具类</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ValueUtils.java
 * @author pq
 * @createdDate 2013-7-30
 * @version
 */
public class ValueUtils {
	
	public static Integer asInt(String value , Integer defaultValue) {
		return value == null ? defaultValue : Integer.valueOf(value);
	}
	
	public static Long asLong(String value , Long defaultValue) {
		return value == null ? defaultValue : Long.valueOf(value);
	}
	
	public static Double asInt(String value , Double defaultValue) {
		return value == null ? defaultValue : Double.valueOf(value);
	}
	
	public static Boolean asBoolean(String value , Boolean defaultValue) {
		return value == null ? defaultValue : Boolean.valueOf(value);
	}
	
	public static Float asFloat(String value , Integer defaultValue) {
		return value == null ? defaultValue : Float.valueOf(value);
	}

}
