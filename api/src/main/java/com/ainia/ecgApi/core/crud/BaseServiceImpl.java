package com.ainia.ecgApi.core.crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ainia.ecgApi.core.bean.Domain;
import com.ainia.ecgApi.core.exception.ServiceException;

/**
 * <p>the default baseService impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * BaseServiceImpl.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.3
 */
public abstract class BaseServiceImpl<T extends Domain, ID extends Serializable> implements BaseService<T, ID> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public abstract BaseDao<T , ID> getBaseDao();
	@Autowired
	private Mapper  mapper;
	
	public List<T> findAll(Query<T> query) {
		return this.getBaseDao().findAll(query);
	}

	@SuppressWarnings("unchecked")
	public Iterable<T> findAll(Iterable<ID> ids) {
		return ((JpaRepository<T , ID>)getBaseDao()).findAll(ids);
	}

	@SuppressWarnings("unchecked")
	public long count(Query<T> query) {
		return getBaseDao().count(query);
	}

	@SuppressWarnings("unchecked")
	public <S extends T> S create(S domain) {
		try {
			return ((JpaRepository<T , ID>)getBaseDao()).save(domain);
		}
		catch(ConstraintViolationException ce){
			throw ce;
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <S extends T> List<S> create(Iterable<S> domains) {
		try {
			return ((JpaRepository<T , ID>)getBaseDao()).save(domains);
		}catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <S extends T> S update(S domain) {
		try {
			JpaRepository<T , ID> dao = ((JpaRepository<T , ID>)getBaseDao());
			S old = (S) dao.findOne((ID)domain.getId());
			mapper.map(domain , old);
			//PropertyUtil.copyProperties(old , domain);
			return dao.save(old);
		}
		catch(ConstraintViolationException ce){
			throw ce;
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <S extends T> List<S> update(Iterable<S> domains) {
		try {
			List<S> list = new ArrayList<S>();
			for (S domain : domains) {
				this.update(domain);
				list.add(domain);
			}
			return list;
		}
		catch(ConstraintViolationException ce){
			throw ce;
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <S extends T> S patch(S domain) {
		try {
			JpaRepository<T , ID> dao = (JpaRepository<T , ID>)getBaseDao();
			return dao.save(domain);
		} 
		catch(ConstraintViolationException ce){
			throw ce;
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public <S extends T> List<S> patch(Iterable<S> domains) {
		try {
			List<S> list = new ArrayList<S>();
			for (S domain : domains) {
				this.patch(domain);
				list.add(domain);
			}
			return list;
		}
		catch(ConstraintViolationException ce){
			throw ce;
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public T get(ID id) {
		return ((JpaRepository<T , ID>)getBaseDao()).findOne(id);
	}

	public T get(Query<T> query) {
		List<T> list = this.findAll(query);
		return (list != null && !list.isEmpty())?list.get(0):null;
	}

	@SuppressWarnings("unchecked")
	public boolean exists(ID id) {
		return ((JpaRepository<T , ID>)getBaseDao()).exists(id);
	}

	@SuppressWarnings("unchecked")
	public void delete(ID id) {
		try {
			((JpaRepository<T , ID>)getBaseDao()).delete(id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void delete(T domain) {
		try {
			((JpaRepository<T , ID>)getBaseDao()).delete(domain);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}

	@SuppressWarnings("unchecked")
	public void deleteInBatch(Iterable<T> domains) {
		try {
			((JpaRepository<T , ID>)getBaseDao()).deleteInBatch(domains);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void deleteAll() {
		try {
			((JpaRepository<T , ID>)getBaseDao()).deleteAll();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
