package com.ainia.ecgApi.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.sys.OperatorDao;
import com.ainia.ecgApi.domain.sys.Expert;
import com.ainia.ecgApi.domain.sys.Operator;

/**
 * <p>Operator ServiceImpl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * OperatorServiceImpl.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.1
 */
@Service
public class OperatorServiceImpl extends BaseServiceImpl<Operator, Long>
		implements OperatorService {

	@Autowired
	private OperatorDao operatorDao;
	@Autowired
	private EmployeeService employeeService;
	@Override
	public BaseDao<Operator, Long> getBaseDao() {
		return operatorDao;
	}
	public void setOperatorDao(OperatorDao operatorDao) {
		this.operatorDao = operatorDao;
	}

	
	@Override
	public Operator save(Operator domain) {
		return employeeService.save(domain);
	}

	@Override
	public List<Operator> save(Iterable domains) {
		return employeeService.save(domains);
	} 
	
	@Override
	public Operator update(Operator operator) {
		return employeeService.update(operator);
	}

	@Override
	public List<Operator> update(Iterable operators) {
		return employeeService.update(operators);
	}

	@Override
	public Operator patch(Operator operator) {
		return employeeService.patch(operator);
	}

	@Override
	public List<Operator> patch(Iterable operators) {
		return employeeService.patch(operators);
	} 
}
