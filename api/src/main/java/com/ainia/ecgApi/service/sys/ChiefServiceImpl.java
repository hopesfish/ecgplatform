package com.ainia.ecgApi.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.bean.Domain;
import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.sys.ChiefDao;
import com.ainia.ecgApi.domain.sys.Chief;

/**
 * <p>chief Domain Service</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ChiefServiceImpl.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.1
 */
@Service
public class ChiefServiceImpl extends BaseServiceImpl<Chief , Long> implements ChiefService
{
	@Autowired
	private ChiefDao chiefDao;
	@Autowired
	private EmployeeService employeeService;

	@Override
	public BaseDao<Chief, Long> getBaseDao() {
		return chiefDao;
	}

	@Override
	public Chief create(Chief domain) {
		return employeeService.create(domain);
	}

	@Override
	public List<Chief> create(Iterable domains) {
		return employeeService.create(domains);
	}

	@Override
	public Chief update(Chief chief) {
		return employeeService.update(chief);
	}

	@Override
	public List<Chief> update(Iterable chiefs) {
		return employeeService.update(chiefs);
	}

	@Override
	public Chief patch(Chief chief) {
		return employeeService.patch(chief);
	}

	@Override
	public List<Chief> patch(Iterable chiefs) {
		return employeeService.patch(chiefs);
	} 

	
	
}
