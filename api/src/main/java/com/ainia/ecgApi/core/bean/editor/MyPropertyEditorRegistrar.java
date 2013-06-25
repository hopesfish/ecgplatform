package com.ainia.ecgApi.core.bean.editor;

import java.sql.Date;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/**
 * <p></p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * MyPropertyEditorRegistrar.java
 * @author pq
 * @createdDate 2013-6-25
 * @version
 */
public class MyPropertyEditorRegistrar implements PropertyEditorRegistrar {

	public void registerCustomEditors(PropertyEditorRegistry registry) {
		registry.registerCustomEditor(Date.class , new DatePropertyEditor());

	}

}
