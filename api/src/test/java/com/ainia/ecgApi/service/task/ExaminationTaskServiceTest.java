package com.ainia.ecgApi.service.task;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.domain.task.ExaminationTask;
import com.ainia.ecgApi.domain.task.Task.Status;

/**
 * <p>ExaminationTask Service test</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ExaminationTaskServiceTest.java
 * @author pq
 * @createdDate 2013-6-27
 * @version 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class ExaminationTaskServiceTest {

    @Autowired
    private ExaminationTaskService examinationTaskService;

    private static ExaminationTask examinationTask;
    
    public void setExaminationTaskService(ExaminationTaskService examinationTaskService) {
        this.examinationTaskService = examinationTaskService;
    }
    
    
    
    @Before
    public void setUp() {
        examinationTask = new ExaminationTask();
        examinationTask.setExaminationId(1L);
        examinationTask.setStatus(Status.pending);
        examinationTask.setOperatorId(4L);
        examinationTask.setAuto(false);
    }
    
    @Test
    public void testFind() {
        Query<ExaminationTask> query = new Query<ExaminationTask>();
        List<ExaminationTask> examinationTasks = examinationTaskService.findAll(query);
        
        Assert.assertNotEquals(examinationTasks.size() , 0);
    }

    @Test
    public void testCreate() {
        examinationTaskService.create(examinationTask);
        
        Assert.assertTrue(examinationTask.getId() != null);
        
        examinationTaskService.delete(examinationTask);
    }
    
    @Test
    public void testUpdate() {
        examinationTaskService.create(examinationTask);
        ExaminationTask _examinationTask = examinationTaskService.update(examinationTask);
        
        examinationTaskService.delete(_examinationTask);
    }
    
    @Test
    public void testPatch() {
        examinationTaskService.create(examinationTask);
        
        ExaminationTask _examinationTask = examinationTaskService.update(examinationTask);
        
        examinationTaskService.delete(_examinationTask);
    }
    
    
}
