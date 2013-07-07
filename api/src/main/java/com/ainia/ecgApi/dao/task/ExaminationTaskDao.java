package com.ainia.ecgApi.dao.task;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.task.ExaminationTask;


/**
 * <p>ExaminationTask Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ExaminationTaskDao.java
 * @author pq
 * @createdDate 2013-07-08
 * @version 0.1
 */
public interface ExaminationTaskDao extends JpaRepository<ExaminationTask , Long>, BaseDao<ExaminationTask , Long> { 
    
    /**
     * <p>根据健康监测ID 获得监测任务</p>
     * @param examinationId
     * @return
     * HealthExamination
     */
	public ExaminationTask findByExaminationId(Long examinationId);
}
