package com.ainia.ecgApi.core.crud;

import java.io.Serializable;
import java.util.List;

import com.ainia.ecgApi.domain.Domain;

/**
 * <p>BaseService interface</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * BaseService.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.1
 */
public interface BaseService<T extends Domain, ID extends Serializable> {

	/**
	 * <p>find all domain list by query</p>
	 * List<T>
	 * @param query
	 * @return List<T>
	 */
	public List<T> findAll(Query query);
	/**
	 * <p>find domain list by ids</p>
	 * Iterable<T>
	 * @param ids
	 * @return
	 */
	public Iterable<T> findAll(Iterable<ID> ids);
	/**
	 * 
	 * <p>get total of query</p>
	 * long
	 * @param query
	 * @return
	 */
	public long count(Query query);
	/**
	 * <p>save Domain Object</p>
	 * S
	 * @param domain
	 * @return
	 */
	public <S extends T> S save(S domain);
	/**
	 * <p>save Domain Object</p>
	 * Iterable<S>
	 * @param domains
	 * @return
	 */
	public <S extends T> List<S> save(Iterable<S> domains);
	/**
	 * <p>Update Domain Object</p>
	 * <pre>
	 * 	The update method will update all field(null field)
	 * </pre>
	 * S
	 * @param domain
	 * @return
	 */
	public <S extends T> S update(S domain);
	/**
	 * <p>Update Domains</p>
	 * Iterable<S>
	 * @param domains
	 * @return
	 */
	public <S extends T> List<S> update(Iterable<S> domains);
	/**
	 * <p>Update Domain without null field</p>
	 * S
	 * @param domain
	 * @return
	 */
	public <S extends T> S patch(S domain);
	/**
	 * <p>Update Domains without null field</p>
	 * Iterable<S>
	 * @param domains
	 * @return
	 */
	public <S extends T> List<S> patch(Iterable<S> domains);
	/**
	 * <p>get Domain ByID</p>
	 * T
	 * @param id
	 * @return
	 */
	public T get(ID id);
	/**
	 * <p>get Domain By Query Object</p>
	 * T
	 * @param query
	 * @return
	 */
	public T get(Query query);
	/**
	 * <p>check Domain is exists by id</p>
	 * boolean
	 * @param id
	 * @return
	 */
	public boolean exists(ID id);
	/**
	 * <p>delete Domain Object by id</p>
	 * void
	 * @param id
	 */
	public void delete(ID id);
	/**
	 * <p>delete Domain Object</p>
	 * void
	 * @param domain
	 */
	public void delete(T domain);
	/**
	 * <p>delete domain in batch</p>
	 * void
	 * @param domains
	 */
	public void deleteInBatch(Iterable<T> domains);
	/**
	 * <p>delete all Domain Object</p>
	 * void
	 */
	public void deleteAll();
}
