package com.ainia.ecgApi.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
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

}
