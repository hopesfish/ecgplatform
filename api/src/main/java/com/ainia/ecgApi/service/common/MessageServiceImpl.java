package com.ainia.ecgApi.service.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
public class MessageServiceImpl implements MessageService {

	@Override
	public void sendEmail(Message message) {
		HttpGet get = new HttpGet("http://ecgnotify.sinaapp.com/email.php?to=16259903@qq.com&code=888888");  
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
		// TODO Auto-generated method stub

	}

}
