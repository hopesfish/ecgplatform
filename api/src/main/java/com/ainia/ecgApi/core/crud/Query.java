package com.ainia.ecgApi.core.crud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>CRUD Query Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * Query.java
 * @author pq
 * @createdDate 2013-6-21
 * @version 0.3
 */
public class Query {

	public static final String CONDITION_SPLIT = ":";
	/**
	 * The Query Domain class
	 */
	private Class clazz;
	private List<Condition> conds = new ArrayList();
	private Page page = new Page();
	private Map<String , OrderType> orders = new HashMap(3);
	
	public Query addCondition(Condition condition) {
		this.conds.add(condition);
		return this;
	}
	
	public Query addOrder(String field , OrderType order) {
		this.orders.put(field, order);
		return this;
	}
	
	
	public enum OrderType {
		asc ,
		desc
	}


	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public List<Condition> getConds() {
		return conds;
	}

	public void setConds(List<Condition> conds) {
		this.conds = conds;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Map<String, OrderType> getOrders() {
		return orders;
	}

	public void setOrders(Map<String, OrderType> orders) {
		this.orders = orders;
	}

}
