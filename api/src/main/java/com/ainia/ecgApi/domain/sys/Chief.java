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
	
	private static final long serialVersionUID = 1L;

}
