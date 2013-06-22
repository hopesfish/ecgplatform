package com.ainia.ecgApi.domain.sys;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * <p>Operator Domain</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * Operator.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
@Entity
@DiscriminatorValue("operator")
public class Operator extends Employee {

	private static final long serialVersionUID = 1L;

	

}
