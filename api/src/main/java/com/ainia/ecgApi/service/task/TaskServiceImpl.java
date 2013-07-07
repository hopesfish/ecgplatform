package com.ainia.ecgApi.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.task.TaskDao;
import com.ainia.ecgApi.domain.task.Task;

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
    
    @Autowired
    private TaskDao taskDao;
    
    @Override
    public BaseDao<Task , Long> getBaseDao() {
        return taskDao;
    }

}
