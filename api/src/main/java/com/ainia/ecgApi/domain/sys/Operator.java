package com.ainia.ecgApi.domain.sys;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	private Set<Expert> experts;

	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER)  
	@JoinTable(name="expert_operator"  , joinColumns={@JoinColumn(name="operator_id")}  
        						, inverseJoinColumns={@JoinColumn(name="expert_id")}  
    )  
	public Set<Expert> getExperts() {
		return experts;
	}

	public void setExperts(Set<Expert> experts) {
		this.experts = experts;
	}
	@Transient
	public void addExpert(Expert expert) {
		if (experts == null) {
			experts = new HashSet();
		}
		experts.add(expert);
	}
	

}
