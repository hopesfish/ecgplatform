package com.ainia.ecgApi.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * @File Name     : ServiceUtils.java
 * @Author        ： pp
 * @Create Date   : 2012-10-7 上午8:32:16
 * @Modified By   :   
 * @Modified Date : 
 * @Description   : spring bean 获取工具类,用于在非spring bean 环境 获取spring 服务
 * @Version       :
 */
public class ServiceUtils implements ApplicationContextAware{

	private static ApplicationContext application;
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		application = applicationContext;
	}
	
	public static Object getService(String serviceName) {
		Assert.notNull(application , "no application");
		return application.getBean(serviceName);
	}
	
	public static Object getService(Class clazz) {
		Assert.notNull(application , "no application");
		return application.getBean(clazz);
	}

}
