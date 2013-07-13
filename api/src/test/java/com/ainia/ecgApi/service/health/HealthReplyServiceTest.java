package com.ainia.ecgApi.service.health;

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
import com.ainia.ecgApi.domain.health.HealthReply;

/**
 * <p>HealthReply Service test</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthReplyServiceTest.java
 * @author pq
 * @createdDate 2013-6-27
 * @version 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class HealthReplyServiceTest {

    @Autowired
    private HealthReplyService healthReplyService;

    private static HealthReply healthReply;
    
    public void setHealthReplyService(HealthReplyService healthReplyService) {
        this.healthReplyService = healthReplyService;
    }
    
    
    
    @Before
    public void setUp() {
        healthReply = new HealthReply();
        healthReply.setType("auto");
        healthReply.setReason("title");
        healthReply.setResult("result");
        healthReply.setContent("content");
        healthReply.setEmployeeId(1L);
        healthReply.setExaminationId(1L);
    }
    
    @Test
    public void testFind() {
        Query<HealthReply> query = new Query<HealthReply>();
        List<HealthReply> healthReplys = healthReplyService.findAll(query);
        
    }

    @Test
    public void testCreate() {
        healthReplyService.create(healthReply);
        
        Assert.assertTrue(healthReply.getId() != null);
        
        healthReplyService.delete(healthReply);
    }
    
    @Test
    public void testUpdate() {
        healthReplyService.create(healthReply);
        HealthReply _healthReply = healthReplyService.update(healthReply);
        
        healthReplyService.delete(_healthReply);
    }
    
    @Test
    public void testPatch() {
        healthReplyService.create(healthReply);
        
        HealthReply _healthReply = healthReplyService.update(healthReply);
        
        healthReplyService.delete(_healthReply);
    }
    
    
}
