package com.ainia.ecgApi.service.https;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ainia.ecgApi.core.web.AjaxResult;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>Chief Service test</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HttpsTest.java
 * @author pq
 * @createdDate 2013-6-27
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class HttpsTest {
	
	private  HttpClient httpClient;
	
	@Before
	public void setUp() throws NoSuchAlgorithmException, KeyManagementException {
		 SSLContext ctx = SSLContext.getInstance("TLS");
         X509TrustManager tm = new X509TrustManager() {
             public X509Certificate[] getAcceptedIssuers() {
                 return null;
             }
             public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
             public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
         };
         ctx.init(null, new TrustManager[] { tm }, null);
         SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
         SchemeRegistry registry = new SchemeRegistry();
         registry.register(new Scheme("https", 443, ssf));
         ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
		
         httpClient = new DefaultHttpClient(mgr);  
	}

	@Test
	public void testHttpConnect() throws Exception{
        HttpGet get = new HttpGet("https://115.28.52.3:8443/web/index.html");
        HttpResponse response = httpClient.execute(get);  
        
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        	String line = null;
        	while ((line = reader.readLine()) != null) {
        		System.out.println("============ " + line);
        	}
        }
	}
	@Test
	public void testHttpsGizpUpload() throws NoSuchAlgorithmException, KeyManagementException, ClientProtocolException, IOException {
        //登录
		HttpPost loginPost = new HttpPost("https://115.28.52.3:8443/web/api/user/auth");
		
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("username","13911111111"));
		formparams.add(new BasicNameValuePair("password", "passw0rd"));
		UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
	    loginPost.setEntity(uefEntity);
		
		HttpResponse loginResponse = httpClient.execute(loginPost);
    	BufferedReader loginReader = new BufferedReader(new InputStreamReader(loginResponse.getEntity().getContent()));
    	String loginLine = null;
    	String token = null;
    	if (loginResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	    	while ((loginLine = loginReader.readLine()) != null) {
	    		ObjectMapper mapper = new ObjectMapper();
	    		Map<String, String> map  = mapper.readValue(loginLine ,  HashMap.class);
	    		token = map.get("token");
	    		System.out.println("============ " + map.get("token"));
	    	}
    	}
		
		HttpPost post = new HttpPost("https://localhost:8443/api/user/1/examination");
        //获取gzip 字节数组
    	Resource resource = new ClassPathResource("health/sample3-gzip");
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	InputStream input = resource.getInputStream();
    	int b = -1;
    	while ((b = input.read()) != -1) {
    		out.write(b);
    	}
    	out.flush();
    	byte[] bytes = out.toByteArray();
    	out.close();
    	input.close();
        ByteArrayBody byteBody = new ByteArrayBody(bytes , "file");
        MultipartEntity  entity = new MultipartEntity();
        
        entity.addPart("file", byteBody);
        entity.addPart("isGziped" , new StringBody("true"));
        entity.addPart("apkId" , new StringBody("100211"));
        
        post.addHeader(AjaxResult.AUTH_HEADER , token);
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);  
        System.out.println(">>>>>>>>>>>>>> " + response.getStatusLine().getStatusCode());
    	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    	String line = null;
    	while ((line = reader.readLine()) != null) {
    		System.out.println("============ " + line);
    	}
	}
	
}
