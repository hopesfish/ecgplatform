package com.ainia.ecgApi.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.sys.Expert;
import com.ainia.ecgApi.domain.sys.Operator;
import com.ainia.ecgApi.service.sys.ExpertService;
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
	@Autowired
	private ExpertService expertService;

	/**
	 * <p>add expert to operator</p>
	 * @param id
	 * @param expertId
	 * @return
	 */
	@RequestMapping(value = "{id}/expert/{expertId}" , method = RequestMethod.POST ,
										produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> addExpert(@PathVariable("id") Long id , @PathVariable("expertId") Long expertId) {
		Operator operator = operatorService.get(id);
		if (operator == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		operator.addExpert(expertService.get(expertId));
		operatorService.update(operator);
		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * <p>remove expert from operator</p>
	 * @param id
	 * @param expertId
	 * @return
	 * ResponseEntity<?>
	 */
	@RequestMapping(value = "{id}/expert/{expertId}" , method = RequestMethod.DELETE ,
										produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> removeExpert(@PathVariable("id") Long id , @PathVariable("expertId") Long expertId) {
		Operator operator = operatorService.get(id);
		Expert     expert = expertService.get(expertId);
		if (operator.getExperts() == null || !operator.getExperts().contains(expert)) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		operator.getExperts().remove(expert);
		operatorService.update(operator);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@Override
	public BaseService<Operator, Long> getBaseService() {
		return operatorService;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}

	public void setExpertService(ExpertService expertService) {
		this.expertService = expertService;
	}

}
