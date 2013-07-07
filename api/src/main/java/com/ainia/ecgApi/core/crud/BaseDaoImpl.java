package com.ainia.ecgApi.core.crud;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ReflectionUtils;

import com.ainia.ecgApi.core.bean.Domain;
import com.ainia.ecgApi.core.utils.JPAUtils;

/**
 * <p>add custome jpaRepository method</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * BaseDaoImpl.java
 * @author pq
 * @createdDate 2013-6-21
 * @version 0.5
 */
public abstract class BaseDaoImpl<T extends Domain , ID extends Serializable> implements BaseDao<T, ID> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager em;
	private Class<T> clazz;
	@Autowired
	private ConversionService conversionService;
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		this.clazz      = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public long count(Query<T> query) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery   criteria = builder.createQuery();
		Root<T> root = criteria.from(clazz);
		
		criteria.select(builder.count(root));
		generateCondition(query , builder , criteria , root);
		//构造查询条件
	    Long counts =  (Long)em.createQuery(criteria).getSingleResult();	
	    return counts;
	}

	public List<T> findAll(Query<T> query) {
		if (query.getClazz() == null) {
			query.setClazz(clazz);
		}
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery criteria  = builder.createQuery();
		Root<T> root = criteria.from(query.getClazz());
		criteria.select(root);
		generateCondition(query , builder , criteria , root);
        TypedQuery typedQuery = em.createQuery(criteria);
        //检查是否分页
        Page page = query.getPage();
        if (page.isPageable()) {
        	typedQuery.setMaxResults(page.getMax());
        	typedQuery.setFirstResult(page.getOffset());
        }
	    return typedQuery.getResultList();
	}
	
	protected  void generateCondition(Query<T> query , CriteriaBuilder builder , CriteriaQuery criteria , Root root) {
		Predicate[] predicates = new Predicate[query.getConds().size()];
		int i =0;
		for (Condition condition : query.getConds()) {
			try {
				if (condition.getValue() != null && !(condition.getValue() instanceof Collection)) {
					Class targetClass = ReflectionUtils.findField(clazz , condition.getField()).getType();
					Class sourceClass = condition.getValue().getClass();
					if (targetClass != sourceClass && conversionService.canConvert(sourceClass, targetClass)) {
						condition.setValue(conversionService.convert(condition.getValue(), targetClass));
					}
				}
				predicates[i++] = JPAUtils.resolverCondition(root , builder, condition);
			}catch(Throwable t){
				t.printStackTrace();
				if (log.isWarnEnabled()) {
					log.warn("can not resolver field " + condition.getField() + " for " + query.getClazz());
				}
			}
		}		
		criteria.where(builder.and(predicates));
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

}
