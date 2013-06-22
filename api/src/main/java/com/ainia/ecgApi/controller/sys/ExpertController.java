package com.ainia.ecgApi.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.sys.Expert;
import com.ainia.ecgApi.service.sys.ExpertService;

/**
 * <p>Expert Controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ExpertController.java
 * @author pq
 * @createdDate 2013-6-22
 * @version 0.1
 */
@Controller
@RequestMapping("api/expert")
public class ExpertController extends BaseController<Expert, Long> {

	@Autowired
	private ExpertService expertService;
	
	@Override
	public BaseService<Expert, Long> getBaseService() {
		return expertService;
	}

	public void setExpertService(ExpertService expertService) {
		this.expertService = expertService;
	}
	
}
