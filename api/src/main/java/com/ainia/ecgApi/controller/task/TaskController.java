package com.ainia.ecgApi.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.core.crud.Page;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.crud.Query.OrderType;
import com.ainia.ecgApi.core.web.AjaxResult;
import com.ainia.ecgApi.domain.task.Task;
import com.ainia.ecgApi.service.task.TaskService;

/**
 * <p>Task controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * TaskController.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
@Controller
@RequestMapping("/api/task")
public class TaskController extends BaseController<Task , Long> {

    @Autowired
    private TaskService taskService;
    
    @Override
    public BaseService<Task , Long> getBaseService() {
        return taskService;
    }
    

	@Override
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Page<Task> index(Query<Task> query) {
		query.addOrder(Task.CREATED_DATE , OrderType.desc);
		return super.index(query);
	}



	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = "application/json")
	@ResponseBody
	public ResponseEntity<AjaxResult> patch(Long id, Task domain) {
		return new ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED);
	}

}
