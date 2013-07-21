package com.ainia.ecgApi.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.crud.Condition;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.crud.Condition.Logic;
import com.ainia.ecgApi.core.crud.Condition.Type;
import com.ainia.ecgApi.dao.sys.OperatorDao;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.domain.sys.Employee.Status;
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
	public Operator create(Operator domain) {
		return employeeService.create(domain);
	}

	@Override
	public List<Operator> create(Iterable domains) {
		return employeeService.create(domains);
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
	public List<Operator> findAllByWork(Query query) {
		query.addCondition(new Condition(Condition.eq(Employee.STATUS ,  Status.ONLINE) ,
						new Condition(Employee.STATUS ,  Type.eq , Status.ONLINE , Logic.or)));
		return this.findAll(query);
	} 
}
