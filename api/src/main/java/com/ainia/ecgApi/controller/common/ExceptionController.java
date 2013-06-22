package com.ainia.ecgApi.controller.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ainia.ecgApi.core.web.AjaxResult;

/**
 * <p>exception handler controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ExceptionController.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
@Controller
@RequestMapping("exception")
public class ExceptionController {

	/**
	 * <p>handler exception </p>
	 * ResponseEntity<AjaxResult>
	 * @return
	 */
	public ResponseEntity<AjaxResult> handler() {
		return new ResponseEntity<AjaxResult>(new AjaxResult() , HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
