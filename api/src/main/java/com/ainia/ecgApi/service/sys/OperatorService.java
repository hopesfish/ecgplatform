package com.ainia.ecgApi.service.sys;

import java.util.List;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.domain.sys.Operator;

/**
 * <p>Operator Service</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * OperatorService.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.1
 */
public interface OperatorService extends BaseService<Operator, Long> {

	/**
	 * <p>获得所有可以接受任务的</p>
	 * @return
	 * List<Operator>
	 */
	public List<Operator> findAllByWork(Query query);
}
