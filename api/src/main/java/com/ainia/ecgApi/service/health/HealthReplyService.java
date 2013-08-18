package com.ainia.ecgApi.service.health;

import java.util.List;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.health.HealthReply;

/**
 * <p>HealthReply Service interface </p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthReplyService.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
public interface HealthReplyService extends BaseService<HealthReply , Long> {
    

	/**
	 * <p>获得所有健康监测回复</p>
	 * @param examinationId
	 * @return
	 * List<HealthReply>
	 */
	public List<HealthReply> findAllReplyByExamination(Long examinationId);
}
