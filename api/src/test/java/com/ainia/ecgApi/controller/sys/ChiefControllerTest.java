package com.ainia.ecgApi.controller.sys;


import java.io.Serializable;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.ainia.ecgApi.core.bean.Domain;
import com.ainia.ecgApi.core.crud.Query;

/**
 * <p>Chief Controller test</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ChiefControllerTest.java
 * @author pq
 * @createdDate 2013-6-27
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml",   "classpath:spring-mvc.xml"})
@ActiveProfiles("test")
public class ChiefControllerTest {

    @Autowired
    public RequestMappingHandlerAdapter handlerAdapter;
    @Autowired
    public ChiefController chiefController;
    
    private static MockHttpServletRequest request;

    private static MockHttpServletResponse response;
    
    @BeforeClass
    public static void before() {
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        response = new MockHttpServletResponse();
    }
    
    @Test
    public void testIndex() throws NoSuchMethodException, Exception {
        request.setRequestURI("/api/chief");
        request.setMethod(HttpMethod.GET.name());
        
        handlerAdapter.handle(request, response, new HandlerMethod(chiefController, "index" , Query.class));

        Assert.isTrue(200 == response.getStatus());
    }
    
    @Test
    public void testCreate() throws NoSuchMethodException, Exception {
        request.setRequestURI("/api/chief");
        request.setMethod(HttpMethod.POST.name());
        request.addParameter("name" , "test");
        request.addParameter("username" , "test");
        request.addParameter("password" , "test");
        request.addParameter("hospital" , "健康中心");
        request.addParameter("birthday" , "1983-02-03");
        request.addParameter("expire" , "");
        request.addParameter("status" , "OFFLINE");
        
        handlerAdapter.handle(request, response, new HandlerMethod(chiefController, "create" , Domain.class));

        Assert.isTrue(201 == response.getStatus());
    }
    
    public void testUpdate() throws NoSuchMethodException, Exception {
        request.setRequestURI("/api/chief/1");
        request.setMethod(HttpMethod.PUT.name());
        request.addParameter("status" , "ONLINE");
        
        handlerAdapter.handle(request, response, new HandlerMethod(chiefController, "update" , Serializable.class , Domain.class));

        Assert.isTrue(201 == response.getStatus());
    }

	public void setChiefController(ChiefController chiefController) {
		this.chiefController = chiefController;
	}
    
    
}
