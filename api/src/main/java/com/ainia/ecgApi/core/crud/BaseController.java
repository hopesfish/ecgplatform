package com.ainia.ecgApi.core.crud;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.core.bean.Domain;
import com.ainia.ecgApi.core.web.AjaxResult;

/**
 * <p>CRUD BaseController with rest</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * BaseController.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.3
 */
public abstract class BaseController<T extends Domain , ID extends Serializable> {

	protected final Logger log = Logger.getLogger(this.getClass());
	
	protected Class<T> clazz;
	protected String domainName;
	
	public abstract BaseService<T , ID> getBaseService();
	
	@SuppressWarnings("unchecked")
	public BaseController() {
		this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.domainName = StringUtils.uncapitalize(clazz.getSimpleName());
	}
	/**
	 * <p>load the domain list</p>
	 * Page<T>
	 * @param query
	 * @return
	 */
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<T> index(Query<T> query) {
		long total = this.getBaseService().count(query);
		query.getPage().setTotal(total);
		query.getPage().setDatas(this.getBaseService().findAll(query));
		
		return query.getPage();
	}
	/**
	 * <p>create new Domain Object</p>
	 * AjaxResult
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "create" , method = RequestMethod.POST ,
									   produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AjaxResult> create(@Valid T domain) {
		T newDomain = this.getBaseService().save(domain);
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.addParam(Domain.ID , newDomain.getId().toString());
		return new ResponseEntity<AjaxResult>(HttpStatus.OK);
	}
	/**
	 * <p>get Domain Object by id</p>
	 * AjaxResult
	 * @return
	 */
	@RequestMapping(value = "/{id}" , method = RequestMethod.GET , 
									  produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public T get(@PathVariable("id") ID id) {
		return this.getBaseService().get(id);
	}
	
	/**
	 * <p>get Domain Object by query</p>
	 * AjaxResult
	 * @return
	 */
	@RequestMapping(value = "/get" , method = RequestMethod.GET , 
									 produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public T get(Query<T> query) {
		return this.getBaseService().get(query);
	}
	/**
	 * <p>update Domain Object</p>
	 * AjaxResult
	 * @param id
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "update/{id}" , method = RequestMethod.PUT , 
											produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AjaxResult> update(@PathVariable("id") ID id , @Valid T domain) {
		T t = this.getBaseService().get(id);
		if (t == null) {
			return new ResponseEntity<AjaxResult>(HttpStatus.NOT_FOUND);
		}
		this.getBaseService().update(domain);
		return new ResponseEntity<AjaxResult>(HttpStatus.OK);
	}
	/**
	 * <p>patch Domain Object</p>
	 * AjaxResult
	 * @param id
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "update/{id}" , method = RequestMethod.PATCH , 
										produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AjaxResult> patch(@PathVariable("id") ID id , @Valid T domain) {
		T t = this.getBaseService().get(id);
		if (t == null) {
			return new ResponseEntity<AjaxResult>(HttpStatus.NOT_FOUND);
		}
		this.getBaseService().patch(domain);
		return new ResponseEntity<AjaxResult>(HttpStatus.OK);
	}
	/**
	 * <p>delete Domain Object</p>
	 * AjaxResult
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete/{id}" , method = RequestMethod.DELETE , 
										produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AjaxResult> delete(@PathVariable("id") ID id) {
		T t = this.getBaseService().get(id);
		if (t == null) {
			return new ResponseEntity<AjaxResult>(HttpStatus.NOT_FOUND);
		}
		this.getBaseService().delete(t);
		return new ResponseEntity<AjaxResult>(HttpStatus.OK);		
	}
	
}
