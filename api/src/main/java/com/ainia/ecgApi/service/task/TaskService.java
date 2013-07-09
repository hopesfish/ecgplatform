package com.ainia.ecgApi.service.task;

import java.util.List;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.task.Task;

/**
 * <p>Task Service interface </p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * TaskService.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
public interface TaskService extends BaseService<Task , Long> {
    

	/**
	 * <p>创建任务</p>
	 * <pre>
	 * 创建任务时会找到当前任务量最少的接线员接收任务
	 * </pre>
	 * @param task
	 * void
	 */
	public void pending(Task task);
	/**
	 * <p>将任务提交处理</p>
	 * <pre>
	 * //将任务转交给专家, 转交规则为当前任务最少的专家
	 * </pre>
	 * @param task
	 * void
	 */
	public void proceeding(Task task);
	
	/**
	 * <p>完成任务</p>
	 * @param task
	 * void
	 */
	public void complete(Task task);
	/**
	 * <p>获得接线员现有的任务</p>
	 * @param operatorId
	 * @return
	 * List<Task>
	 */
	public List<Task> findAllByOperator(Long operatorId);
	
	/**
	 * <p>获得专家现有的任务</p>
	 * @param operatorId
	 * @return
	 * List<Task>
	 */
	public List<Task> findAllByExpert(Long expertId);
}
