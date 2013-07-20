package com.ainia.ecgApi.service.sys;

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
import com.ainia.ecgApi.domain.sys.SystemConfig;
import com.ainia.ecgApi.domain.sys.SystemConfig.Type;

/**
 * <p>SystemConfig Service test</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * SystemConfigServiceTest.java
 * @author pq
 * @createdDate 2013-6-27
 * @version 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class SystemConfigServiceTest {

    @Autowired
    private SystemConfigService systemConfigService;

    private static SystemConfig systemConfig;
    
    public void setSystemConfigService(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }
    
    
    
    @Before
    public void setUp() {
        systemConfig = new SystemConfig();
        systemConfig.setType(Type.basic);
        systemConfig.setKey("root.upload");
        systemConfig.setValue("d:/upload");
    }
    
    @Test
    public void testFind() {
        Query<SystemConfig> query = new Query<SystemConfig>();
        List<SystemConfig> systemConfigs = systemConfigService.findAll(query);
        
        Assert.assertNotEquals(systemConfigs.size() , 0);
    }

    @Test
    public void testCreate() {
        systemConfigService.create(systemConfig);
        
        Assert.assertTrue(systemConfig.getId() != null);
        
        systemConfigService.delete(systemConfig);
    }
    
    @Test
    public void testUpdate() {
        systemConfigService.create(systemConfig);
        SystemConfig _systemConfig = systemConfigService.update(systemConfig);
        
        systemConfigService.delete(_systemConfig);
    }
    
    @Test
    public void testPatch() {
        systemConfigService.create(systemConfig);
        
        SystemConfig _systemConfig = systemConfigService.update(systemConfig);
        
        systemConfigService.delete(_systemConfig);
    }
    
    
}
