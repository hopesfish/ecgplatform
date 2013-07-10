package com.ainia.ecgApi.core.bean.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dozer.DozerConverter;

public class DozerStringDateConverter extends DozerConverter<Date , String> {

	public DozerStringDateConverter() {
		super(Date.class , String.class);
	}

	@Override
	public Date convertFrom(String text, Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(text);
		}catch(Exception e) {
			format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return format.parse(text);
			} catch (ParseException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public String convertTo(Date date, String text) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.format(date);
		}catch(Exception e) {
			format = new SimpleDateFormat("yyyy-MM-dd");
			return format.format(date);
		}
	}

}
