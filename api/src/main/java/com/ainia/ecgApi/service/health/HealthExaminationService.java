package com.ainia.ecgApi.service.health;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthReply;

/**
 * <p>HealthExamination Service interface </p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthExaminationService.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
public interface HealthExaminationService extends BaseService<HealthExamination , Long> {
    

	/**
	 * <p>回复健康测试</p>
	 * @param reply
	 * void
	 */
	public void reply(Long id , HealthReply reply);
}
