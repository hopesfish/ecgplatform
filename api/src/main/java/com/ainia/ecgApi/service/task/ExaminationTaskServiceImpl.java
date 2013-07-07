package com.ainia.ecgApi.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.task.ExaminationTaskDao;
import com.ainia.ecgApi.domain.task.ExaminationTask;

/**
 * <p>ExaminationTask Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ExaminationTaskServiceImpl.java
 * @author pq
 * @createdDate 2013-07-08
 * @version 0.1
 */
@Service
public class ExaminationTaskServiceImpl extends BaseServiceImpl<ExaminationTask , Long> implements ExaminationTaskService {
    
    @Autowired
    private ExaminationTaskDao examinationTaskDao;
    
    @Override
    public BaseDao<ExaminationTask , Long> getBaseDao() {
        return examinationTaskDao;
    }

	public ExaminationTask getByExaminationId(Long examinationId) {
		return examinationTaskDao.findByExaminationId(examinationId);
	}

}
