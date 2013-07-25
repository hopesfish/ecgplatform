package com.ainia.ecgApi.service.task;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.security.AuthUserImpl;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.domain.task.Task;
import com.ainia.ecgApi.domain.task.Task.Status;

/**
 * <p>Task Service test</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * TaskServiceTest.java
 * @author pq
 * @createdDate 2013-6-27
 * @version 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;
    @Mock
    private AuthenticateService authenticateService;

    private static Task task;
    
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
    
    
    
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
        task = new Task();
        task.setStatus(Status.pending);
        task.setOperatorId(4L);
        task.setAuto(false);
    }
    
    @Test
    public void testFind() {
        Query<Task> query = new Query<Task>();
        List<Task> tasks = taskService.findAll(query);
        
        Assert.assertNotEquals(tasks.size() , 0);
    }

    @Test
    public void testCreate() {
        taskService.create(task);
        
        Assert.assertTrue(task.getId() != null);
        
        taskService.delete(task);
    }
    
    @Test
    public void testUpdate() {
        taskService.create(task);
        Task _task = taskService.update(task);
        
        taskService.delete(_task);
    }
    
    @Test
    public void testPatch() {
        taskService.create(task);
        
        Task _task = taskService.update(task);
        
        taskService.delete(_task);
    }
    
    @Test
    public void testDeleteAllByUserId() {
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(2L ,"test", "13700230001" , User.class.getSimpleName() , new String[]{}));
    	((TaskServiceImpl)taskService).setAuthenticateService(authenticateService);
    	Query query = new Query();
    	query.eq(Task.USER_ID , 2L);
    	long count = taskService.count(query);
    	Assert.assertTrue(count > 0);
    	
    	taskService.deleteAllByUser(2L);
    
    	count = taskService.count(query);
    	Assert.assertEquals(count , 0);
    }
    
    
}
