package com.ainia.ecgApi.service.common;

import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.dto.common.Message;

/**
 * <p>采用web接口发送短信及邮件</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * MessageServiceImpl.java
 * @author pq
 * @createdDate 2013-8-1
 * @version
 */
@Service
public class MessageServiceImpl implements MessageService {

	@Override
	public void sendEmail(Message message) {
		HttpGet get = new HttpGet(String.format("http://ecgnotify.sinaapp.com/email.php?to=%s&code=%s" ,
													message.getTo() , message.getCode()));  
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse response = httpclient.execute(get);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new ServiceException("exception.email.sendError");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("exception.email.sendError");
		} 
		finally {
            httpclient.getConnectionManager().shutdown();
        }
	}

	@Override
	public void sendSms(Message message) {
		HttpGet get = new HttpGet(String.format("http://ecgnotify.sinaapp.com/sms.php?to=%s&code=%s" ,
				message.getTo() , message.getCode()));  
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse response = httpclient.execute(get);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new ServiceException("exception.email.sendError");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("exception.email.sendError");
		} 
		finally {
			httpclient.getConnectionManager().shutdown();
		}

	}

}
