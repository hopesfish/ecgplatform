package com.ainia.ecgApi.service.health;

import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.security.AuthUserImpl;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthRule.Level;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.utils.DataException;

/**
 * <p>HealthExamination Service test</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthExaminationServiceTest.java
 * @author pq
 * @createdDate 2013-6-27
 * @version 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class HealthExaminationServiceTest {

    @Autowired
    private HealthExaminationService healthExaminationService;
    @Mock
    private AuthenticateService authenticateService;
    
    private static HealthExamination healthExamination;
    
    public void setHealthExaminationService(HealthExaminationService healthExaminationService) {
        this.healthExaminationService = healthExaminationService;
    }
    
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
        healthExamination = new HealthExamination();
        healthExamination.setUserId(1L);
        healthExamination.setTestItem("PHONE");
        healthExamination.setUserType("VIP");
        healthExamination.setLevel(Level.danger);
        healthExamination.setBloodPressureLow(80);
        healthExamination.setBloodPressureHigh(100);
        healthExamination.setHeartRhythm(1);
        healthExamination.setBloodOxygen(1);
        healthExamination.setBreath(1);
        healthExamination.setBodyTemp(1.0F);
        healthExamination.setPulserate(1);
        healthExamination.setHeartData("");
        healthExamination.setLatitude(0.0);
        healthExamination.setAltitude(1);
        healthExamination.setTemp(27.0F);
        healthExamination.setHumidity(1);
        healthExamination.setPressure(1);
        healthExamination.setChargeType("");
        healthExamination.setHeartFeatures("");
        healthExamination.setAlgorithmVersion(1.0F);
    }
    
    @Test
    public void testFind() {
        Query<HealthExamination> query = new Query<HealthExamination>();
        List<HealthExamination> healthExaminations = healthExaminationService.findAll(query);
        
        Assert.assertNotEquals(healthExaminations.size() , 0);
    }

    @Test
    public void testCreate() {
        healthExaminationService.create(healthExamination);
        
        Assert.assertTrue(healthExamination.getId() != null);
        
        healthExaminationService.delete(healthExamination);
    }
    
    @Test
    public void testUpdate() {
        healthExaminationService.create(healthExamination);
        HealthExamination _healthExamination = healthExaminationService.update(healthExamination);
        
        healthExaminationService.delete(_healthExamination);
    }
    
    @Test
    public void testPatch() {
        healthExaminationService.create(healthExamination);
        
        HealthExamination _healthExamination = healthExaminationService.update(healthExamination);
        
        healthExaminationService.delete(_healthExamination);
    }
    @Test
    public void testUpload() throws IOException, DataException, InterruptedException {
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(2L , "13700230001" , User.class.getSimpleName() , null));
    	Resource resource = new ClassPathResource("health/sample2");
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	int b = -1;
    	InputStream input = resource.getInputStream();
    	while ((b = input.read()) != -1) {
    		out.write(b);
    	}
    	byte[] bytes = out.toByteArray();
    	((HealthExaminationServiceImpl)healthExaminationService).setAuthenticateService(authenticateService);
    	healthExaminationService.upload(bytes);
    	Thread.sleep(5000);
    	input.close();
    	out.close();
    }
    
}
