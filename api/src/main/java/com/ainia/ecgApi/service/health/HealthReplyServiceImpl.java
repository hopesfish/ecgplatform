package com.ainia.ecgApi.service.health;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.crud.Query.OrderType;
import com.ainia.ecgApi.dao.health.HealthReplyDao;
import com.ainia.ecgApi.domain.health.HealthReply;

/**
 * <p>HealthReply Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthReplyServiceImpl.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
@Service
public class HealthReplyServiceImpl extends BaseServiceImpl<HealthReply , Long> implements HealthReplyService {
    
    @Autowired
    private HealthReplyDao healthReplyDao;
    
    @Override
    public BaseDao<HealthReply , Long> getBaseDao() {
        return healthReplyDao;
    }

	@Override
	public List<HealthReply> findAllReplyByExamination(Long examinationId) {
		Query<HealthReply> query = new Query();
		query.eq(HealthReply.EXAMINATION_ID , examinationId);
		query.addOrder(HealthReply.CREATED_DATE , OrderType.desc);
		return this.findAll(query);
	}

}
