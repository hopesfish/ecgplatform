package com.ainia.ecgApi.domain.sys;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * <p>Expert Domain</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * Expert.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
@Entity
@DiscriminatorValue("expert")
public class Expert extends Employee {

	private static final long serialVersionUID = 1L;

}
