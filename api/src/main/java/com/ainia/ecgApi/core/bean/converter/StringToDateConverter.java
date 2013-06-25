package com.ainia.ecgApi.core.bean.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * <p>String to Date Converter</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * StringToDateConverter.java
 * @author pq
 * @createdDate 2013-6-25
 * @version
 */
public class StringToDateConverter implements Converter<String, Date> {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public Date convert(String text) {
		if (text == null) return null;
		try {
			return timestampFormat.parse(text);
		}catch(Exception e){
			try {
				return dateFormat.parse(text);
			} catch (ParseException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}

}
