package com.ainia.ecgApi.service.health;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
	
	/**
	 * <p>上传健康监测数据</p>
	 * <pre>
	 *  文件生成规则为 rootPath/{type}/{userId}/{now|yyyy-MM-dd}-{examinationId}
	 * </pre>
	 * @param uploadData
	 * void
	 */
	public void upload(HealthExamination examination , byte[] uploadData , String md5);
	
	/**
	 * <p>统计指定用户指定时间段的测试平均值</p>
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 * List<Map>
	 */
	public List<Map> statisticsByUserAndDay(Long userId , Date start , Date end);
}
