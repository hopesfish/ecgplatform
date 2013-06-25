package com.ainia.ecgApi.core.bean.editor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p>Date Property Editor</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * DatePropertyEditor.java
 * @author pq
 * @createdDate 2013-6-25
 * @version
 */
public class DatePropertyEditor extends PropertyEditorSupport {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		System.out.println("-----------------------");
		if (text == null || "".equals(text)) {
			super.setValue(null);
		}
		try {
			super.setValue(dateFormat.parse(text));
		}
		catch(Exception e) {
			try {
				super.setValue(timestampFormat.parse(text));
			} catch (ParseException e1) {
				e1.printStackTrace();
				super.setValue(null);
			}
		}
	}

	
}
