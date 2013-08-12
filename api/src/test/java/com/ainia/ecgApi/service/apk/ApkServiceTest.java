package com.ainia.ecgApi.service.apk;

import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
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
import com.ainia.ecgApi.domain.apk.Apk;
import com.ainia.ecgApi.domain.sys.User;

/**
 * <p>Apk Service test</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ApkServiceTest.java
 * @author pq
 * @createdDate 2013-6-27
 * @version 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class ApkServiceTest {

    @Autowired
    private ApkService apkService;

    @Mock
    private AuthenticateService authenticateService;

    private static Apk apk;
    
    public void setApkService(ApkService apkService) {
        this.apkService = apkService;
    }
    
    
    
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
        apk = new Apk();
        apk.setVersion("1121321");
        apk.setReleased(true);
        apk.setEnabled(true);
    }
    
    @Test
    public void testFind() {
        Query<Apk> query = new Query<Apk>();
        List<Apk> apks = apkService.findAll(query);
        
    }

    @Test
    public void testUpload() throws Exception{
    	
    	when(authenticateService.getCurrentUser()).thenReturn(new AuthUserImpl(2L , "test" , "13700230001" , User.class.getSimpleName()));
    	
    	Resource resource = new ClassPathResource("apk/sample");
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	int b = -1;
    	InputStream input = resource.getInputStream();
    	while ((b = input.read()) != -1) {
    		out.write(b);
    	}
    	byte[] bytes = out.toByteArray();
    	
    	Apk apk = new Apk();
        apk.setVersion("1");
        apk.setEnabled(true);
       
    	apkService.upload(apk , bytes);
    	Thread.sleep(5000);
    	input.close();
    	out.close();
    	
    }


    @Test
    public void testCreate() {
        apkService.create(apk);
        
        Assert.assertTrue(apk.getId() != null);
        
        apkService.delete(apk);
    }
    
    @Test
    public void testUpdate() {
        apkService.create(apk);
        Apk _apk = apkService.update(apk);
        
        apkService.delete(_apk);
    }
    
    @Test
    public void testPatch() {
        apkService.create(apk);
        
        Apk _apk = apkService.update(apk);
        
        apkService.delete(_apk);
    }
    
    
}
