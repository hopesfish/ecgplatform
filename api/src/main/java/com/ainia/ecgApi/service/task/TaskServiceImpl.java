package com.ainia.ecgApi.service.task;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.utils.PropertyUtil;
import com.ainia.ecgApi.dao.task.TaskDao;
import com.ainia.ecgApi.domain.sys.Expert;
import com.ainia.ecgApi.domain.sys.Operator;
import com.ainia.ecgApi.domain.task.Task;
import com.ainia.ecgApi.domain.task.Task.Status;
import com.ainia.ecgApi.service.sys.OperatorService;
import com.ainia.ecgApi.service.sys.SystemConfigService;

/**
 * <p>Task Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * TaskServiceImpl.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
@Service
public class TaskServiceImpl extends BaseServiceImpl<Task , Long> implements TaskService {
	
	public static final int OPERATOR_MAX_TASK_COUNT = 10;//每日接线员可接的最大任务量
	public static final int EXPERT_MAX_TASK_COUNT = 10; //每日专家可接的最大任务量
    
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private OperatorService operatorService;
    
    @Override
    public BaseDao<Task , Long> getBaseDao() {
        return taskDao;
    }

	@Override
	public Task  update(Task task) {
		Task old = this.get(task.getId());
		PropertyUtil.copyProperties(old , task);
		Status status = old.getStatus();
		if (Status.pending.equals(status)) {
			return this.pending(old);
		}
		else if (Status.proceeding.equals(status)) {
			return this.proceeding(old);
		}
		else if (Status.completed.equals(status)) {
			return this.complete(old);
		}
		throw new ServiceException("task.error.status.notNull");
	}

	public Task pending(Task task) {
		List<Operator> operators = operatorService.findAllByWork(new Query());
		Operator selectedOperator = null;
		int selectedOperatorTask = -1;
		for (Operator operator  : operators) {
			List<Task> tasks = this.findAllByOperator(operator.getId());
			if (selectedOperator == null || (tasks != null && tasks.size() < selectedOperatorTask)) {
				selectedOperator = operator;
				selectedOperatorTask = tasks.size();
			}
		}
		task.setOperatorId(selectedOperator.getId());
		task.setStatus(Status.pending);
		return super.create(task);
	}

	public Task proceeding(Task task) {
		Long operatorId = task.getOperatorId();
		if (operatorId == null) {
			throw new ServiceException("task.error.operator.notNull");
		}
		Operator operator = operatorService.get(operatorId);
		Set<Expert> experts = operator.getExperts();
		if (experts == null || experts.isEmpty()) {
			throw new ServiceException("task.error.expert.notFound");
		}
		Expert selectedExpert = null;   //选中的专家
		int  selectedExpertTask = -1;    //选中的专家所拥有的任务
		for (Expert expert : experts) {
			List<Task> tasks = this.findAllByExpert(expert.getId());
			if ((selectedExpert == null || (tasks !=null && tasks.size() < selectedExpertTask))
						&& tasks.size() < EXPERT_MAX_TASK_COUNT) {
				selectedExpert = expert;
				selectedExpertTask = tasks.size();
			}
		}
		//如果无法找到对应的专家
		if (selectedExpert == null) {
			throw new ServiceException("task.error.expert.notFound");
		}
		task.setExpertId(selectedExpert.getId());
		task.setStatus(Status.proceeding);
		
		return super.update(task);
	}

	public Task complete(Task task) {
		task.setStatus(Status.completed);
		return super.update(task);
	}

	public List<Task> findAllByOperator(Long operatorId) {
		Query<Task> query = new Query();
		query.eq(Task.OPERATOR_ID , operatorId).isNull(Task.EXPERT_ID).eq(Task.STATUS, Status.pending);
		
		return this.findAll(query);
	}

	public List<Task> findAllByExpert(Long expertId) {
		Query<Task> query = new Query();
		query.eq(Task.EXPERT_ID , expertId).eq(Task.STATUS, Status.proceeding);
		return this.findAll(query);
	}

    

}
