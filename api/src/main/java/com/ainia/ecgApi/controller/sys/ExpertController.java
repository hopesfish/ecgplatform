package com.ainia.ecgApi.controller.sys;

import java.util.Set;

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
	@Autowired
	private OperatorService operatorService;
	
	@Override
	public BaseService<Expert, Long> getBaseService() {
		return expertService;
	}

	@RequestMapping(value = "{id}/operator" , method = RequestMethod.GET ,
										produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Set<Operator> getOperators(@PathVariable("id") Long id) {
		return expertService.get(id).getOperators();
	}
	
	/**
	 * <p>add operator to expert</p>
	 * Set<Operator>
	 * @param id
	 * @param operId
	 * @return
	 */
	@RequestMapping(value = "{id}/operator/{operId}" , method = RequestMethod.POST ,
										produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> addOperator(@PathVariable("id") Long id , @PathVariable("operId") Long operId) {
		Expert expert = expertService.get(id);
		expert.addOperator(operatorService.get(operId));
		expertService.update(expert);
		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * <p>remove operator from expert</p>
	 * @param id
	 * @param operId
	 * @return
	 * ResponseEntity<?>
	 */
	@RequestMapping(value = "{id}/operator/{operId}" , method = RequestMethod.DELETE ,
										produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> removeOperator(@PathVariable("id") Long id , @PathVariable("operId") Long operId) {
		Expert expert = expertService.get(id);
		Operator operator = operatorService.get(operId);
		if (expert.getOperators() == null || !expert.getOperators().contains(operator)) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		expert.getOperators().remove(operator);
		expertService.update(expert);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	
	public void setExpertService(ExpertService expertService) {
		this.expertService = expertService;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}
	
}
