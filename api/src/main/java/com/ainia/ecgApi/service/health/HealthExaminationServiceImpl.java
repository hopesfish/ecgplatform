package com.ainia.ecgApi.service.health;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.dao.health.HealthExaminationDao;
import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthReply;
import com.ainia.ecgApi.domain.task.ExaminationTask;
import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadServiceImpl.Type;
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
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private UploadService uploadService;
    
    @Override
    public BaseDao<HealthExamination , Long> getBaseDao() {
        return healthExaminationDao;
    }

	public void reply(Long id , HealthReply reply) {
		healthReplyService.create(reply);
	}

	public void upload(byte[] uploadData) {
		//save the file
		try {
			HealthExamination examination = new HealthExamination();

			AuthUser authUser = authenticateService.getCurrentUser();	
			examination.setUserId(authUser.getId());
		
			examination.setUserName(authUser.getUsername());
			examination.setUserType(authUser.getType());
			examination.setTemp(37.5F);
			examination.setBodyTemp(37.5F);
			examination.setTestItem("ipad");
			examination.setHeartRhythm(1);
			examination.setBreath(50);
			examination.setBloodOxygen(6);
			
			this.create(examination);
			String fileAccessUri = uploadService.save(Type.heart_img , String.valueOf(examination.getId()) , uploadData);
			examination.setHeartData(fileAccessUri);
			this.update(examination);
			
			ExaminationTask task = new ExaminationTask();
			task.setExaminationId(examination.getId());
			taskService.pending(task);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("upload.file.error");
		}
	}

}
