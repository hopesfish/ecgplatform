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
	

	private Set<Operator> operators;
	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER)  
	@JoinTable(name="expert_operator"  , joinColumns={@JoinColumn(name="expert_id")}  
        						, inverseJoinColumns={@JoinColumn(name="operator_id")}  
    )  
	public Set<Operator> getOperators() {
		return operators;
	}

	public void setOperators(Set<Operator> operators) {
		this.operators = operators;
	}
	@Transient
	public void addOperator (Operator operator) {
		if (operators == null)
		{	
			operators = new HashSet();
		}
		operators.add(operator);
	}
		
}
