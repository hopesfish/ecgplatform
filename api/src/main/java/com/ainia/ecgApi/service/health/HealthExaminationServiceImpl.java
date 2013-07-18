package com.ainia.ecgApi.service.health;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.domain.task.ExaminationTask;
import com.ainia.ecgApi.dto.health.ProcessData;
import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadService.Type;
import com.ainia.ecgApi.service.task.ExaminationTaskService;
import com.ainia.ecgApi.service.task.TaskService;
import com.ainia.ecgApi.utils.DataProcessor;
import com.ainia.ecgApi.utils.ECGChart;

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
	private String rawPath ;
    
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
	    	DataProcessor processor = new DataProcessor();
	    	ProcessData processData = processor.process(uploadData , uploadData.length);
			
			HealthExamination examination = new HealthExamination();

			AuthUser authUser = authenticateService.getCurrentUser();	
			if (authUser == null) {
				throw new ServiceException("authUser.error.notFound");
			}
			if (!User.class.getSimpleName().equals(authUser.getType())) {
				throw new ServiceException("examination.error.upload.notAllowed");
			}
			//TODO 暂时固定
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
			//生成文件相对路径
			String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
			//TODO 文件后缀名固定
			String ecg1Path = String.valueOf(2L) + "/" + dateStr + "-" + examination.getId() + "/ecg1.jpg";
			byte[] ecg1 = ECGChart.createChart( processData.getEcg1() , 0 ,  processData.getEcg1().length);
			String ecg1Uri = uploadService.save(Type.heart_img , ecg1Path , ecg1);
			
			String ecg2Path = String.valueOf(2L) + "/" + dateStr + "-" + examination.getId() + "/ecg2.jpg";
			byte[] ecg2 = ECGChart.createChart( processData.getEcg2() , 0 ,  processData.getEcg1().length);
			String ecg2Uri = uploadService.save(Type.heart_img , ecg2Path , ecg2);
			
			String ecg3Path = String.valueOf(2L) + "/" + dateStr + "-" + examination.getId() + "/ecg3.jpg";
			byte[] ecg3 = ECGChart.createChart( processData.getEcg3() , 0 ,  processData.getEcg1().length);
			String ecg3Uri = uploadService.save(Type.heart_img , ecg3Path , ecg3);
			//存储原始文件
			String rawPath = String.valueOf(2L) + "/" + dateStr + "-" + examination.getId() + "/raw";
			String rawUri = uploadService.save(Type.heart_img , rawPath , uploadData);
			
			examination.setHeartData(rawUri);
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
