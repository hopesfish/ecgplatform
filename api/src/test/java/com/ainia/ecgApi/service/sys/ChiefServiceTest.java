package com.ainia.ecgApi.service.sys;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.domain.sys.Chief;

/**
 * <p>Chief Service test</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ChiefServiceTest.java
 * @author pq
 * @createdDate 2013-6-27
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
public class ChiefServiceTest {

	@Autowired
	private ChiefService chiefService;

	private Chief chief;
	
	public void setChiefService(ChiefService chiefService) {
		this.chiefService = chiefService;
	}
	
	
	
	@Before
	public void setUp() {
		chief = new Chief();
		chief.setName("chief1");
		chief.setUsername("test");
		chief.setPassword("test");
		chief.setBirthday( new DateTime(1983, 2, 1, 0, 0, 0, 0).toDate());
		chief.setExpire(new DateTime(2099, 2, 1, 0, 0, 0, 0).toDate());
		chief.setGender(1);
		chief.setDismissed(false);
		chief.setCompany("健康中心");
		chief.setEnabled(true);
		chief.setStatus("ONLINE");
		chief.setIdCard("430203198302011518");
	}
	@After
	public void tearDown() {
		chiefService.delete(chief);
	}
	
	@Test
	public void testFind() {
		Query<Chief> query = new Query<Chief>();
		List<Chief> chiefs = chiefService.findAll(query);
		
		Assert.assertNotEquals(chiefs.size() , 0);
	}

	@Test
	public void testCreate() {
		chiefService.create(chief);
		
		Assert.assertTrue(chief.getId() != null);
		

	}
	
	@Test
	public void testUpdate() {
//		chiefService.create(chief);
//		
//		chief.setEnabled(false);
//		chief.setCompany("test");
//		chief.setPassword(null);
//		Chief _chief = chiefService.update(chief);
//		
//		Assert.assertNotNull(_chief.getPassword());
	}
	
	@Test
	public void testPatch() {
		chiefService.create(chief);
		
		chief.setCompany(null);
		chief.setPassword(null);
		
		Chief _chief = chiefService.update(chief);
		
		Assert.assertNotNull(_chief.getCompany());
	}
	
	
}
