package com.ainia.ecgApi.core.crud;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.ainia.ecgApi.core.utils.ExceptionUtils;

/**
 * <p>add custome jpaRepository method</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * BaseDaoImpl.java
 * @author pq
 * @createdDate 2013-6-21
 * @version 0.5
 */
public class BaseDaoImpl<T , ID extends Serializable> implements BaseDao<T, ID> {

	@PersistenceContext
	private EntityManager em;
	private Class clazz;
	
	public BaseDaoImpl() {
		this.clazz      = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public long count(Query query) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery   criteria = builder.createQuery();
		Root<T> root = criteria.from(clazz);
		
		criteria.select(builder.count(root));
		//构造查询条件
		Predicate[] predicates = new Predicate[query.getConds().size()];
		int i =0;
		for (Condition condition : query.getConds()) {
			try {
				predicates[i++] = JPAUtils.resolverCondition(root , builder, condition);
			} catch (Exception e) {
				throw ExceptionUtils.unchecked(e);
			}
		}		
		criteria.where(builder.and(predicates));
	    Long counts =  (Long)em.createQuery(criteria).getSingleResult();	
	    return counts;
	}

	public List<T> findAll(Query query) {
		if (query.getClazz() == null) {
			query.setClazz(clazz);
		}
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery criteria  = builder.createQuery();
		Root<T> root = criteria.from(query.getClazz());
		
		Predicate[] predicates = new Predicate[query.getConds().size()];
		int i =0;
		for (Condition condition : query.getConds()) {
			predicates[i++] = JPAUtils.resolverCondition(root , builder , condition);
		}
		criteria.select(root);
		criteria.where(predicates);
        TypedQuery typedQuery = em.createQuery(criteria);
        //检查是否分页
        Page page = query.getPage();
        if (page.isPageable()) {
        	typedQuery.setMaxResults(page.getMax());
        	typedQuery.setFirstResult(page.getOffset());
        }
	    return typedQuery.getResultList();
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

}
