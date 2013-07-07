package com.ainia.ecgApi.service.task;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.task.ExaminationTask;

/**
 * <p>ExaminationTask Service interface </p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ExaminationTaskService.java
 * @author pq
 * @createdDate 2013-07-08
 * @version 0.1
 */
public interface ExaminationTaskService extends BaseService<ExaminationTask , Long> {
    
	/**
	 * <p>根据健康监测ID 获得任务</p>
	 * @param examinationId
	 * @return
	 * ExaminationTask
	 */
	public ExaminationTask getByExaminationId(Long examinationId);
}
