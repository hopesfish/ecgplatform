package com.ainia.ecgApi.core.utils;

/**
 * @Ddesc        : 异常工具类
 * @Module       :
 * @Author       : Administrator
 * Create Date   : 2012-9-26
 * Last Modified :
 */
public class ExceptionUtils {

	/**
	 * @Desc         : 将checked 异常转换为unchecked 异常 
	 * @Author       : Administrator
	 * @Create Date  :
	 * @Last Modified:
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException)e;
		}
		else {
			return new RuntimeException(e);
		}
	}
}
