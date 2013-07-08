package com.ainia.ecgApi.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/api/examinationTask")
public class ExaminationTaskController extends BaseController<ExaminationTask , Long> {

    @Autowired
    private ExaminationTaskService examinationTaskService;
    
    @Override
    public BaseService<ExaminationTask , Long> getBaseService() {
        return examinationTaskService;
    }


}
