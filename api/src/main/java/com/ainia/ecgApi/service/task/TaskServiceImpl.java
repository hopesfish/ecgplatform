package com.ainia.ecgApi.service.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.core.utils.PropertyUtil;
import com.ainia.ecgApi.dao.task.TaskDao;
import com.ainia.ecgApi.domain.sys.Expert;
import com.ainia.ecgApi.domain.sys.Operator;
import com.ainia.ecgApi.domain.task.ExaminationTask;
import com.ainia.ecgApi.domain.task.Task;
import com.ainia.ecgApi.domain.task.Task.Status;
import com.ainia.ecgApi.service.health.HealthExaminationService;
import com.ainia.ecgApi.service.sys.OperatorService;

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
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private HealthExaminationService healthExaminationService;
    
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

	// 没有绑定专家的不能获得任务
	private List <Operator> getOperators() {
		List <Operator> returnOperators = new ArrayList <Operator> ();
		List <Operator> operators = operatorService.findAll(new Query());
		for (Operator operator  : operators) {
			if (operator.getExperts().size() > 0) {
				returnOperators.add(operator);
			}
		}
		return returnOperators;
	}

	public Task pending(Task task) {
		List<Operator> operators = getOperators();
		Operator selectedOperator = null;
		int selectedOperatorTask = Integer.MAX_VALUE;
		for (Operator operator  : operators) {
			List<Task> tasks = this.findAllByOperator(operator.getId());
			if (tasks != null && tasks.size() < selectedOperatorTask) {
				selectedOperator = operator;
				selectedOperatorTask = tasks.size();
			}
		}
		if (selectedOperator == null) {
			throw new ServiceException("task.error.no.operator.is.assigned");
		}
		task.setOperatorId(selectedOperator.getId());
		task.setStatus(Status.pending);
		return super.update(task);
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
		int  selectedExpertTask = Integer.MAX_VALUE;    //选中的专家所拥有的任务
		for (Expert expert : experts) {
			List<Task> tasks = this.findAllByExpert(expert.getId());
			if (tasks !=null && tasks.size() < selectedExpertTask) {
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
		query.eq(Task.OPERATOR_ID , operatorId).isNull(Task.EXPERT_ID);
		
		return this.findAll(query);
	}

	public List<Task> findAllByExpert(Long expertId) {
		Query<Task> query = new Query();
		query.eq(Task.EXPERT_ID , expertId);
		return this.findAll(query);
	}

    public void deleteAllByUser(Long userId) {
    	AuthUser currentUser = authenticateService.getCurrentUser();
    	if (!currentUser.isSuperAdmin() && !currentUser.isChief() && !currentUser.getId().equals(userId)) {
    		throw new ServiceException("exception.method.notAllow");
    	}
    	taskDao.deleteAllByUserId(userId); 
    }
    
    

	@Override
	public void delete(Long id) {
		// TODO delete the examination by application event
		super.delete(this.get(id));
	}

	@Override
	public void delete(Task task) {
		// TODO delete the examination by application event
		if (task instanceof ExaminationTask) {
			Long examinationId = ((ExaminationTask)task).getExaminationId();
			healthExaminationService.delete(examinationId);
		}
		super.delete(task);
	}

	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}
    
}
