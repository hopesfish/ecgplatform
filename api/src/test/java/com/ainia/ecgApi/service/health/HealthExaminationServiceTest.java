package com.ainia.ecgApi.service.health;

import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
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
import com.ainia.ecgApi.domain.health.HealthReply;
import com.ainia.ecgApi.domain.health.HealthRule.Level;
import com.ainia.ecgApi.domain.sys.SystemConfig;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.service.sys.SystemConfigService;
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
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private HealthReplyService healthReplyService;
    @Mock
    private AuthenticateService authenticateService;
    
    private static HealthExamination healthExamination;
    
    public void setHealthExaminationService(HealthExaminationService healthExaminationService) {
        this.healthExaminationService = healthExaminationService;
    }

    public void setSystemConfigService(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }

    public void setHealthReplyService(HealthReplyService healthReplyService) {
        this.healthReplyService = healthReplyService;
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
    public void testUngzipUpload() throws IOException, DataException, InterruptedException {
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(2L , "test" , "13700230001" , User.class.getSimpleName()));
    	Resource resource = new ClassPathResource("health/sample2");
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	int b = -1;
    	InputStream input = resource.getInputStream();
    	while ((b = input.read()) != -1) {
    		out.write(b);
    	}
    	byte[] bytes = out.toByteArray();
    	((HealthExaminationServiceImpl)healthExaminationService).setAuthenticateService(authenticateService);
    	HealthExamination examination = new HealthExamination();
    	healthExaminationService.upload(examination , bytes , null);
    	Thread.sleep(5000);
    	input.close();
    	out.close();
    	
    	Assert.assertTrue(examination.getLevel() != null);
    	Query<HealthReply> query1 = new Query();
    	query1.eq(HealthReply.EXAMINATION_ID , examination.getId());
    	Assert.assertTrue(healthReplyService.findAll(query1).size() == 0);
    }

    @Test
    public void testUpload() throws IOException, DataException, InterruptedException {
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(2L , "test" , "13700230001" , User.class.getSimpleName()));
    	Resource resource = new ClassPathResource("health/sample3");
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	int b = -1;
    	InputStream input = resource.getInputStream();
    	while ((b = input.read()) != -1) {
    		out.write(b);
    	}
    	byte[] bytes = out.toByteArray();
    	((HealthExaminationServiceImpl)healthExaminationService).setAuthenticateService(authenticateService);
    	HealthExamination examination = new HealthExamination();
    	examination.setIsGziped(true);
    	healthExaminationService.upload(examination , bytes , null);
    	Thread.sleep(5000);
    	input.close();
    	out.close();
    	
    	Assert.assertTrue(examination.getLevel() != null);
    	Query<HealthReply> query1 = new Query();
    	query1.eq(HealthReply.EXAMINATION_ID , examination.getId());
    	Assert.assertTrue(healthReplyService.findAll(query1).size() == 0);
    }
    
    @Test
    public void testExportUpload() throws IOException, InterruptedException {
    	Long userId = 2L;
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(userId , "test" , "13700230001" , User.class.getSimpleName()));
    	Resource resource = new ClassPathResource("health/sample3");
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	int b = -1;
    	InputStream input = resource.getInputStream();
    	while ((b = input.read()) != -1) {
    		out.write(b);
    	}
    	byte[] bytes = out.toByteArray();
    	((HealthExaminationServiceImpl)healthExaminationService).setAuthenticateService(authenticateService);
    	HealthExamination examination = new HealthExamination();
    	examination.setIsGziped(true);
    	healthExaminationService.upload(examination , bytes , null);
    	Thread.sleep(5000);
    	input.close();
    	out.close();
    	String exportPath = "c:/upload/user/" + userId + "/examination/" + examination.getId() + "/export.pdf";
    	OutputStream output = new FileOutputStream(exportPath);
    	System.out.println("export pdf file " + exportPath);
    	healthExaminationService.exportPDF(examination,  output);
    }
    


    @Test
    public void testUploadAndAutoReply() throws IOException, DataException, InterruptedException {
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(1L , "test" , "13700230001" , User.class.getSimpleName()));
    	Resource resource = new ClassPathResource("health/sample4.dat");
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	int b = -1;
    	InputStream input = resource.getInputStream();
    	while ((b = input.read()) != -1) {
    		out.write(b);
    	}
    	byte[] bytes = out.toByteArray();
    	
    	SystemConfig config = systemConfigService.get(2l);
    	config.setValue("true");
    	systemConfigService.update(config);
    	
    	if (!System.getProperties().getProperty("os.name").startsWith("Windows")) {
    		config = systemConfigService.get(1l);
        	config.setValue("/tmp/");
        	systemConfigService.update(config);
    	}

    	((HealthExaminationServiceImpl)healthExaminationService).setAuthenticateService(authenticateService);
    	HealthExamination examination = new HealthExamination();
    	examination.setIsGziped(false);
    	healthExaminationService.upload(examination , bytes , null);
    	Thread.sleep(5000);
    	input.close();
    	out.close();
    	
    	config.setValue("false");
    	systemConfigService.update(config);
 
    	Assert.assertTrue(examination.getLevel() != null);
    	Query<HealthReply> query2 = new Query();
    	query2.eq(HealthReply.EXAMINATION_ID , examination.getId());
    	
    	if (System.getProperties().getProperty("os.name").startsWith("Windows")) {
    		Assert.assertTrue(healthReplyService.findAll(query2).size() > 0);
    	}
    }

    @Test
    public void testMockUpload() throws IOException, DataException, InterruptedException {
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(2L , "test" , "13700230001" , User.class.getSimpleName()));
    	((HealthExaminationServiceImpl)healthExaminationService).setAuthenticateService(authenticateService);
    	HealthExamination examination = new HealthExamination();
    	examination.setIsTest(true);
    	healthExaminationService.upload(examination , null , null);
    }

    @Test
    public void testAutoReply() throws IOException, DataException, InterruptedException {
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(2L , "test" , "13700230001" , User.class.getSimpleName()));
    	((HealthExaminationServiceImpl)healthExaminationService).setAuthenticateService(authenticateService);
    	
    	SystemConfig config = systemConfigService.get(2l);
    	config.setValue("true");
    	systemConfigService.update(config);
    	
    	// 心率正常范围
    	HealthExamination examination = new HealthExamination();
    	examination.setIsTest(true);
    	examination.setHeartRhythm(70);
    	healthExaminationService.upload(examination , null , null);
    	Assert.assertTrue(examination.getLevel().equals(Level.success));
    	
    	// 心率警告范围
    	examination = new HealthExamination();
    	examination.setIsTest(true);
    	examination.setHeartRhythm(95);
    	healthExaminationService.upload(examination , null , null);
    	Assert.assertTrue(examination.getLevel().equals(Level.warning));
    	Query<HealthReply> query1 = new Query();
    	query1.eq(HealthReply.EXAMINATION_ID , examination.getId());
    	Assert.assertTrue(healthReplyService.findAll(query1).size() == 1);
    	
    	// 心率危险范围
    	examination = new HealthExamination();
    	examination.setIsTest(true);
    	examination.setHeartRhythm(30);
    	healthExaminationService.upload(examination , null , null);
    	Assert.assertTrue(examination.getLevel().equals(Level.danger));	

    	// 心率异常范围
    	examination = new HealthExamination();
    	examination.setIsTest(true);
    	examination.setHeartRhythm(-100);
    	healthExaminationService.upload(examination , null , null);
    	Assert.assertTrue(examination.getLevel().equals(Level.outside));

    	// 心率异常范围
    	examination = new HealthExamination();
    	examination.setIsTest(true);
    	examination.setHeartRhythm(300);
    	healthExaminationService.upload(examination , null , null);
    	Assert.assertTrue(examination.getLevel().equals(Level.outside));
 
    	// 心率正常 呼吸警告
    	examination = new HealthExamination();
    	examination.setIsTest(true);
    	examination.setHeartRhythm(70);
    	examination.setBreath(95);
    	healthExaminationService.upload(examination , null , null);
    	Assert.assertTrue(examination.getLevel().equals(Level.warning));
    	Query<HealthReply> query2 = new Query();
    	query2.eq(HealthReply.EXAMINATION_ID , examination.getId());
    	Assert.assertTrue(healthReplyService.findAll(query2).size() == 2);

    	// 心率危险 呼吸警告
    	examination = new HealthExamination();
    	examination.setIsTest(true);
    	examination.setHeartRhythm(30);
    	examination.setBreath(95);
    	healthExaminationService.upload(examination , null , null);
    	Assert.assertTrue(examination.getLevel().equals(Level.danger));
   
    	config.setValue("false");
    	systemConfigService.update(config);
    }
    
    @Test
    public void testCustomAutoReply() throws IOException, DataException, InterruptedException {
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(1L , "13911111111" , "13911111111" , User.class.getSimpleName()));
    	((HealthExaminationServiceImpl)healthExaminationService).setAuthenticateService(authenticateService);
    	
    	SystemConfig config = systemConfigService.get(2l);
    	config.setValue("true");
    	systemConfigService.update(config);
    	
    	// 心率正常范围
    	HealthExamination examination = new HealthExamination();
    	examination.setUserId(1l);
    	examination.setIsTest(true);
    	examination.setBreath(95);
    	healthExaminationService.upload(examination , null , null);
    	Assert.assertTrue(examination.getLevel().equals(Level.success));
    	
    	config.setValue("false");
    	systemConfigService.update(config);
    }

    @Test
    public void testDelete() {
        healthExaminationService.create(healthExamination);
        
        HealthExamination _healthExamination = healthExaminationService.update(healthExamination);
        
        healthExaminationService.delete(_healthExamination);
    }
    @Test
    public void testStatisticsByUserAndDay(){
    	DateTime now = new DateTime(2013 , 7 , 1 , 0 , 0 ,0);
    	List<Map> results =  healthExaminationService.statisticsByUserAndDay(1L , now.toDate() , now.plusYears(1).toDate());
    	for (Map map : results) {
    		//System.out.println(map.get(HealthExamination.CREATED_DATE));
    	}
    	Assert.assertTrue(results.size() > 0);
    }

    @Test
    public void testUploadAndCharge() throws IOException, DataException, InterruptedException {
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(1L , "测试用户1" , "13911111111" , User.class.getSimpleName()));

    	// 自动回复
    	SystemConfig config = systemConfigService.get(2l);
    	config.setValue("true");
    	systemConfigService.update(config);

    	// 不能免费使用
    	SystemConfig config1 = systemConfigService.get(4l);
    	config1.setValue("false");
    	systemConfigService.update(config1);
   
    	((HealthExaminationServiceImpl)healthExaminationService).setAuthenticateService(authenticateService);
    	HealthExamination examination = new HealthExamination();
    	examination.setIsTest(true);
    	examination.setHeartRhythm(70);
    	healthExaminationService.upload(examination , null , null);
    	
    	config.setValue("false");
    	systemConfigService.update(config);
    	
    	config1.setValue("true");
    	systemConfigService.update(config1);
 
    	Assert.assertTrue(examination.getLevel() != null);
    }

    /**
     * 必须放到最后，否则相关config无法reset
     * @throws Exception
     */
    @Test
    public void testUploadAndChargeFailed() throws Exception {
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(2L , "测试用户2" , "13922222222" , User.class.getSimpleName()));

    	// 自动回复
    	SystemConfig config = systemConfigService.get(2l);
    	config.setValue("true");
    	systemConfigService.update(config);

    	// 不能免费使用
    	SystemConfig config1 = systemConfigService.get(4l);
    	config1.setValue("false");
    	systemConfigService.update(config1);
   
    	((HealthExaminationServiceImpl)healthExaminationService).setAuthenticateService(authenticateService);
    	HealthExamination examination = new HealthExamination();
    	examination.setIsTest(true);
    	examination.setHeartRhythm(70);
    	try {
    		healthExaminationService.upload(examination , null , null);
    	} catch(Exception e) {
    		
    	}
    	
    	config.setValue("false");
    	systemConfigService.update(config);
    	
    	config1.setValue("true");
    	systemConfigService.update(config1);
 
    	Assert.assertTrue(examination.getLevel() == null);
    	
    }
}
