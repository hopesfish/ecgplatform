package com.ainia.ecgApi.dto.common;

import java.io.Serializable;

/**
 * <p>消息对象</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * Message.java
 * @author pq
 * @createdDate 2013-8-1
 * @version
 */
public class Message implements Serializable {

	private String from;
	private String to;
	private String title;
	private String content;
	
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
