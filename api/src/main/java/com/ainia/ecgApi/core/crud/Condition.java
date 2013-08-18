package com.ainia.ecgApi.core.crud;

import org.springframework.util.Assert;

/**
 * <p>Query Condition Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * Condition.java
 * @author pq
 * @createdDate 2013-6-21
 * @version
 */
public class Condition {

	private String field;
	private Type type = Type.eq;
	private Logic logic = Logic.and;
	private Object value;
	private boolean group;
	
	
	public Condition(String field , Object value) {
		this(field , Type.eq  ,value , Logic.and);
	}
	
	public Condition(String field ,Type type , Object value) {
		this(field , type , value  , Logic.and);
	}
	
	public Condition(String field , Type type ,Object value , Logic logic) {
		this.field = field;
		this.type  = type;
		this.value = value;
		this.logic = logic;
	}
	
	public Condition(Condition...conditions) {
		Assert.isTrue(conditions.length > 1 , "the condition group must latest two");
		this.logic = Logic.and;
		this.group = true;
		this.value = conditions;
	}
	
	public Condition(Logic logic , Condition...conditions) {
		Assert.isTrue(conditions.length > 1 , "the condition group must latest two");
		this.logic = logic;
		this.value = conditions;
	}
	
	public static Condition eq(String field , Object value) {
		return new Condition(field , Type.eq , value);
	}
	
	public static Condition ne(String field , Object value) {
		return new Condition(field , Type.ne , value);
	}
	
	public static Condition ge(String field , Object value) {
		return new Condition(field , Type.ge , value);
	}
	
	public static Condition gt(String field , Object value) {
		return new Condition(field , Type.gt , value);
	}
	
	public static Condition le(String field , Object value) {
		return new Condition(field , Type.le , value);
	}
	
	public static Condition lt(String field , Object value) {
		return new Condition(field , Type.lt , value);
	}
	
	public static Condition like(String field , Object value) {
		return new Condition(field , Type.like , value);
	}
	
	public static Condition llike(String field , Object value) {
		return new Condition(field , Type.llike , value);
	}
	
	public static Condition rlike(String field , Object value) {
		return new Condition(field , Type.rlike , value);
	}
	
	public static Condition in(String field , Object value) {
		return new Condition(field , Type.in , value);
	}
	
	public static Condition noIn(String field , Object... value) {
		return new Condition(field ,Type.notIn , value);
	}
	
	public static Condition isNull(String field) {
		return new Condition(field , Type.isNull , null);
	}
	
	public static Condition isNotNull(String field) {
		return new Condition(field , Type.notNull , null);
	}
	
	public enum Type {
		eq ,
		ne ,
		ge ,
		le ,
		gth ,
		geth ,
		lth ,
		leth ,
		gt ,
		lt ,
		in ,
		notIn ,
		like ,
		llike ,
		rlike ,
		isNull ,
		notNull ,
		isBlank ,
		notBlank ,
		exists
	}
	
	public enum Logic {
		and ,
		or
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Logic getLogic() {
		return logic;
	}

	public void setLogic(Logic logic) {
		this.logic = logic;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isGroup() {
		return group;
	}

	public void setGroup(boolean group) {
		this.group = group;
	}
	
	
}
