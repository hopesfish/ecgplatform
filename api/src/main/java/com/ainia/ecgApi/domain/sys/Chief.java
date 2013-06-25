package com.ainia.ecgApi.domain.sys;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * <p>ChiefDomain</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * Chief.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.1
 */
@Entity
@DiscriminatorValue("chief")
public class Chief extends Employee {
	
	
	private String hospital;
	private String title;

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
