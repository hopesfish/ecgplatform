package com.ainia.ecgApi.core.utils;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.core.convert.ConversionService;

import com.ainia.ecgApi.core.crud.Condition;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.crud.Query.OrderType;

/**
 * <p>jpa query utils</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * JPAUtils.java
 * @author pq
 * @createdDate 2013-6-21
 * @version
 */
public class JPAUtils {
		
	/**
	 * @param root
	 * @param builder
	 * @param query
	 * @param conversionService
	 * @return
	 */
	public static Predicate[] resolverConditions(Root root , CriteriaBuilder builder  ,  Query query , ConversionService conversionService) {
		Class entityClass         = query.getClazz();
		List<Condition> conditions = query.getConds();
		Predicate[] predicates = new Predicate[conditions.size()];
		int i =0;
		for (Condition condition : conditions) {
			try {
				predicates[i++] = resolverCondition(root , builder , condition);
			} catch (Exception e) {
				throw ExceptionUtils.unchecked(e);
			}
		}	
		return predicates;
	}
	
	/**
	 * @Method Name  : resolverCondition
	 * @Description  : 将条件对象转换为jpa 查询对象
	 * @param        : @param root
	 * @param        : @param builder
	 * @param        : @param condition
	 * @param        : @return
	 * @Return Type  : Predicate
	 * @Author       : pp
	 * @Create Date  : 2012-10-6-下午2:32:24
	 * @throws
	 */
	public static Predicate resolverCondition(Root root , CriteriaBuilder builder , Condition condition) {
		if (condition.isGroup()) {
			Condition[] conditions = (Condition[])condition.getValue();
			Predicate[] predicates = new Predicate[conditions.length];
			int i =0;
			for (Condition subCondition : conditions) {
				predicates[i++] = resolverSimpleCondition(root , builder , subCondition);
			}
			switch (condition.getLogic()) {
			case or:
				return builder.or(predicates);
			default:
				return builder.and(predicates);
			}			
		}
		else {
			return resolverSimpleCondition(root , builder , condition);
		}
	}
	
	
	/**
	 * @Method Name  : resolverCondition
	 * @Description  : 将单条件对象转换为jpa 查询对象
	 * @param        : @param root
	 * @param        : @param builder
	 * @param        : @param condition
	 * @param        : @return
	 * @Return Type  : Predicate
	 * @Author       : pp
	 * @Create Date  : 2012-10-6-下午2:32:24
	 * @throws
	 */
	public static Predicate resolverSimpleCondition(Root root , CriteriaBuilder builder , Condition condition) {
		//判断是实体基本属性 还是关联属性
		Expression exp = null;
		String field = condition.getField();
		int relationIndex = field.indexOf(".");
		if (relationIndex != -1) {
			String relationShip = field.substring(0 , relationIndex);
			String relationField= field.substring(relationIndex+1);
			Join join           = root.join(relationShip);
			exp                 = join.get(relationField);
		}
		else {
			exp                 = root.get(field);
		}
		return resolverExpression(exp , builder , condition);
	}
	
	/**
	 * @MethodName   : resolverExpression
	 * @Author       : pq
	 * @Create Date  : 2012-11-19
	 * @Last Modified: 
	 * @Description  : 将expression 转换条件
	 */
	public static Predicate resolverExpression(Expression exp , CriteriaBuilder builder , Condition condition) {
		Predicate predicate = null;
		Object value = condition.getValue();
		//如果为空 则直接采用 isNull 条件
		switch(condition.getType()) {
		case eq:
			if (value == null) {
				predicate = builder.isNull(exp);
			}
			else {
				predicate = builder.equal(exp , value);
			}
			break;
		case ne:
			if (value == null) {
				predicate = builder.isNotNull(exp);
			}
			else {
				predicate = builder.notEqual(exp , value);
			}
			break;
		case notNull:
			predicate = builder.isNotNull(exp);
			break;
		case isNull:
			predicate = builder.isNull(exp);
			break;
		case ge:
			predicate = builder.ge(exp , (Number)value);
			break;
		case le:
			predicate = builder.le(exp , (Number)value);
			break;
		case gt:
			predicate = builder.gt(exp , (Number)value);
			break;
		case lt:
			predicate = builder.lt(exp , (Number)value);
			break;
		case like:
			predicate = builder.like(exp , "%" + value + "%");
			break;
		case llike:
			predicate = builder.like(exp , value + "%");
			break;
		case rlike:
			predicate = builder.like(exp , "%" + value);
			break;
		case gth:
			predicate = builder.greaterThan(exp , (Date)value);
			break;
		case geth:
			predicate = builder.greaterThanOrEqualTo( exp , (Date)value);
			break;
		case lth:
			predicate = builder.lessThan(exp , (Date)value);
			break;
		case leth:
			predicate = builder.lessThanOrEqualTo(exp , (Date)value);
			break;
		case in:
			predicate = builder.in(exp).value(value);
			break;
		case notIn:
			predicate = builder.not(builder.in(exp).value(value));
			break;
		case exists:
			predicate = builder.exists((Subquery)value);
			break;
		}
	    //判断逻辑类型
		switch (condition.getLogic()) {
		case or:
			return builder.or(predicate);
		default:
			return builder.and(predicate);
		}
	}
	
	public static Order resolverOrder(Root root , CriteriaBuilder builder , String field , OrderType orderType) {
		switch(orderType)  {
		case desc:
			return builder.desc(root.get(field));
		case asc:
			return builder.asc(root.get(field));
		default:
			return null;
		}
	}
}
