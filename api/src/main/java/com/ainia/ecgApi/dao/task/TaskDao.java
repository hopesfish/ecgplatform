package com.ainia.ecgApi.dao.task;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.task.Task;


/**
 * <p>Task Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * TaskDao.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
public interface TaskDao extends JpaRepository<Task , Long>, BaseDao<Task , Long> { 
    
    
}
