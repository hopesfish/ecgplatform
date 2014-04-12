package com.ainia.ecgApi.core.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPOutputStream;

import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ainia.ecgApi.service.common.MessageService;
import com.ainia.ecgApi.service.sys.UserService;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.dto.common.Message;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class UnzipTest {

    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    }

    @Test
	public void testUnzip() throws NoSuchAlgorithmException, KeyManagementException, ClientProtocolException, IOException {
    	Resource resource = new ClassPathResource("health/errorzip");
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	GZIPOutputStream gzip = new GZIPOutputStream(out);
    	InputStream input = resource.getInputStream();
    	int b = -1;
    	while ((b = input.read()) != -1) {
    		gzip.write(b);
    	}
	    gzip.finish();
	    out.flush();
	    byte[] bytes = out.toByteArray();
	    
	    System.out.println(bytes.length);
	}
}
