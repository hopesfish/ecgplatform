package com.ainia.ecgApi.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.sys.ExpertDao;
import com.ainia.ecgApi.domain.sys.Chief;
import com.ainia.ecgApi.domain.sys.Expert;

/**
 * <p>Expert ServiceImpl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ExpertServiceImpl.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
@Service
@SuppressWarnings("unchecked")
public class ExpertServiceImpl extends BaseServiceImpl<Expert , Long> implements
		ExpertService {
	@Autowired
	private ExpertDao expertDao;
	@Autowired
	private EmployeeService employeeService;

	@Override
	public BaseDao<Expert, Long> getBaseDao() {
		return expertDao;
	}

	public void setExpertDao(ExpertDao expertDao) {
		this.expertDao = expertDao;
	}

	@Override
	public Expert create(Expert domain) {
		return employeeService.create(domain);
	}

	@Override
	public List<Expert> create(Iterable domains) {
		return employeeService.create(domains);
	} 
	
	@Override
	public Expert update(Expert expert) {
		return employeeService.update(expert);
	}

	@Override
	public List<Expert> update(Iterable experts) {
		return employeeService.update(experts);
	}

	@Override
	public Expert patch(Expert expert) {
		return employeeService.patch(expert);
	}

	@Override
	public List<Expert> patch(Iterable experts) {
		return employeeService.patch(experts);
	} 
}
