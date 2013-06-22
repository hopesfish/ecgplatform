package com.ainia.ecgApi.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.sys.Operator;
import com.ainia.ecgApi.service.sys.OperatorService;

/**
 * <p>Operator Controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * OperatorController.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
@Controller
@RequestMapping("api/operator")
public class OperatorController extends BaseController<Operator, Long> {

	@Autowired
	private OperatorService operatorService;
	
	@Override
	public BaseService<Operator, Long> getBaseService() {
		return operatorService;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}

	
}
