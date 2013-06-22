package com.ainia.ecgApi.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.sys.OperatorDao;
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
	@Override
	public BaseDao<Operator, Long> getBaseDao() {
		return operatorDao;
	}
	public void setOperatorDao(OperatorDao operatorDao) {
		this.operatorDao = operatorDao;
	}

}
