package com.ainia.ecgApi.controller.sys;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.ainia.ecgApi.core.bean.Domain;

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
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml",   "classpath:spring-mvc.xml"})
@ActiveProfiles("test")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,  
			TransactionalTestExecutionListener.class}) 
public class ChiefControllerTest {

	@Autowired
	private WebApplicationContext wac;

    @Autowired
    public RequestMappingHandlerAdapter handlerAdapter;
    @Autowired
    public ChiefController chiefController;
    
    private static MockHttpServletRequest request;

    private static MockHttpServletResponse response;
    
    private MockMvc mockMvc;
    
    @Before
    public void before() {
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        response = new MockHttpServletResponse();
        this.mockMvc = webAppContextSetup(this.wac).build();

      //  mockMvc = standaloneSetup(chiefController).build();
    }
    
    @Test
    public void testIndex() throws NoSuchMethodException, Exception {
        mockMvc.perform(get("/api/chief").contentType(MediaType.APPLICATION_FORM_URLENCODED)
        	   .accept(MediaType.APPLICATION_JSON))
        	   .andExpect(status().isOk());
        	   				

     //   handlerAdapter.handle(request, response, new HandlerMethod(chiefController, "index" , Query.class));
     //   Assert.isTrue(200 == response.getStatus());
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
//        request.setRequestURI("/api/chief/{id}");
//        request.setMethod(HttpMethod.PUT.name());
//        request.addParameter("status" , "ONLINE");
//        HashMap pathvars = new HashMap();
//        pathvars.put("id", "1");
//        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, pathvars);
//        handlerAdapter.handle(request, response, new HandlerMethod(chiefController, "update" ,Serializable.class ,  Domain.class));
//        System.out.println("=========== " + response.getStatus());
//        Assert.isTrue(200 == response.getStatus());
        mockMvc.perform(put("/api/chief/1").contentType(MediaType.APPLICATION_FORM_URLENCODED)
        	   .param("status" , "ONLINE")
         	   .accept(MediaType.APPLICATION_JSON))
         	   .andExpect(status().isOk());
    }
    
	public void setChiefController(ChiefController chiefController) {
		this.chiefController = chiefController;
	}
    
    
}
