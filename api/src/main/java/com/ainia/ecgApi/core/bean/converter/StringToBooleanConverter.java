package com.ainia.ecgApi.core.bean.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * <p>String to Boolean Converter</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * StringToBooleanConverter.java
 * @author pq
 * @createdDate 2013-6-25
 * @version
 */
public class StringToBooleanConverter implements Converter<String, Boolean> {

	public Boolean convert(String text) {
		if (text == null || "".equals(text)) {
			return false;
		}
		if ("1".equals(text) || "true".equals(text)) {
			return true;
		}
		return false;
	}

}
