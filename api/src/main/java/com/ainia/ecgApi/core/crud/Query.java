package com.ainia.ecgApi.core.crud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ainia.ecgApi.core.bean.Domain;
import com.ainia.ecgApi.core.crud.Condition.Type;

/**
 * <p>CRUD Query Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * Query.java
 * @author pq
 * @createdDate 2013-6-21
 * @version 0.3
 */
public class Query<T extends Domain> {

	public static final String CONDITION_SPLIT = ":";
	/**
	 * The Query Domain class
	 */
	private Class<T> clazz;
	private List<Condition> conds = new ArrayList<Condition>();
	private Page<T> page = new Page<T>();
	private Map<String , OrderType> orders = new HashMap<String , OrderType>(3);
	
	public Query<T> addCondition(Condition condition) {
		this.conds.add(condition);
		return this;
	}
	
	public Query<T> addOrder(String field , OrderType order) {
		this.orders.put(field, order);
		return this;
	}
	
	
	public enum OrderType {
		asc ,
		desc
	}

	public Query<T> eq(String field , Object value) {
		this.addCondition(Condition.eq(field , value));
		return this;
	}
	
	public Query<T> ne(String field , Object value) {
		this.addCondition(Condition.ne(field , value));
		return this;
	}
	
	public Query<T> ge(String field , Object value) {
		this.addCondition(Condition.ge(field , value));
		return this;
	}
	
	public Query<T> gt(String field , Object value) {
		this.addCondition(Condition.gt(field , value));
		return this;
	}
	
	public Query<T> le(String field , Object value) {
		this.addCondition(Condition.le(field , value));
		return this;
	}
	
	public Query<T> lt(String field , Object value) {
		this.addCondition(Condition.lt(field , value));
		return this;
	}
	
	public Query<T> like(String field , Object value) {
		this.addCondition(Condition.like(field , value));
		return this;
	}
	
	public Query<T> llike(String field , Object value) {
		this.addCondition(Condition.llike(field , value));
		return this;
	}
	
	public Query<T> rlike(String field , Object value) {
		this.addCondition(Condition.rlike(field , value));
		return this;
	}
	
	public Query<T> in(String field , Object value) {
		this.addCondition(Condition.in(field , value));
		return this;
	}
	
	public Query<T> noIn(String field , Object... value) {
		this.addCondition(Condition.noIn(field , value));
		return this;
	}
	
	public Query<T> isNull(String field) {
		this.addCondition(Condition.isNull(field));
		return this;
	}
	
	public Query<T> isNotNull(String field) {
		this.addCondition(Condition.isNotNull(field));
		return this;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public List<Condition> getConds() {
		return conds;
	}

	public void setConds(List<Condition> conds) {
		this.conds = conds;
	}

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public Map<String, OrderType> getOrders() {
		return orders;
	}

	public void setOrders(Map<String, OrderType> orders) {
		this.orders = orders;
	}

}
