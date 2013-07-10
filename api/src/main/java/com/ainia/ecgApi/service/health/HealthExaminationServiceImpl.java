package com.ainia.ecgApi.service.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.health.HealthExaminationDao;
import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthReply;
import com.ainia.ecgApi.domain.task.ExaminationTask;
import com.ainia.ecgApi.domain.task.Task.Status;
import com.ainia.ecgApi.service.task.ExaminationTaskService;
import com.ainia.ecgApi.service.task.TaskService;

/**
 * <p>HealthExamination Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthExaminationServiceImpl.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
@Service
public class HealthExaminationServiceImpl extends BaseServiceImpl<HealthExamination , Long> implements HealthExaminationService {
    
    @Autowired
    private HealthExaminationDao healthExaminationDao;
    @Autowired
    private HealthReplyService healthReplyService;
    @Autowired
    private ExaminationTaskService examinationTaskService;
    @Autowired
    private TaskService taskService;
    
    @Override
    public BaseDao<HealthExamination , Long> getBaseDao() {
        return healthExaminationDao;
    }

	public void reply(Long id , HealthReply reply) {
		healthReplyService.create(reply);
		ExaminationTask task = examinationTaskService.getByExaminationId(id);
		taskService.complete(task);
	}

	public void upload(byte[] uploadData) {
		
	}

}
