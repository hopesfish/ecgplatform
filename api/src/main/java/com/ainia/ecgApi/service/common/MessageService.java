package com.ainia.ecgApi.service.common;

import com.ainia.ecgApi.dto.common.Message;

/**
 * <p>消息发送服务</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * MessageService.java
 * @author pq
 * @createdDate 2013-8-1
 * @version
 */
public interface MessageService {
	
	/**
	 * <p>邮件方式发送消息</p>
	 * @param message
	 * void
	 */
	public void sendEmail(Message message);
	
	/**
	 * <p>短信方式发送消息</p>
	 * @param message
	 * void
	 */
	public void sendSms(Message message);
}
