package com.ainia.ecgApi.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.task.ExaminationTask;
import com.ainia.ecgApi.service.task.ExaminationTaskService;

/**
 * <p>ExaminationTask controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ExaminationTaskController.java
 * @author pq
 * @createdDate 2013-07-08
 * @version 0.1
 */
@Controller
@RequestMapping("/examinationTask")
public class ExaminationTaskController extends BaseController<ExaminationTask , Long> {

    @Autowired
    private ExaminationTaskService examinationTaskService;
    
    @Override
    public BaseService<ExaminationTask , Long> getBaseService() {
        return examinationTaskService;
    }

}
