package com.ainia.ecgApi.core.crud;

import java.io.Serializable;
import java.util.List;

/**
 * <p>CRUD Basic dao</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * BaseDao.java
 * @author pq
 * @createdDate 2013-6-21
 * @version
 */
public interface BaseDao<T , ID extends Serializable> {

	/**
	 * <p>get count by query object</p>
	 * long
	 * @param query
	 * @return
	 */
	public long count(Query query);
	/**
	 * <p>query all list by query object</p>
	 * List<T>
	 * @param query
	 * @return List<T> 对象集合
	 */
	public List<T> findAll(Query query);
	
}
