package com.ainia.ecgApi.core.crud;

import java.io.Serializable;
import java.util.List;

import com.ainia.ecgApi.core.bean.Domain;

/**
 * <p>CRUD Basic dao</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * BaseDao.java
 * @author pq
 * @createdDate 2013-6-21
 * @version
 */
public interface BaseDao<T extends Domain, ID extends Serializable> {

	/**
	 * <p>get count by query object</p>
	 * long
	 * @param query
	 * @return
	 */
	public long count(Query<T> query);
	/**
	 * <p>query all list by query object</p>
	 * List<T>
	 * @param query
	 * @return List<T> 对象集合
	 */
	public List<T> findAll(Query<T> query);
	
}
