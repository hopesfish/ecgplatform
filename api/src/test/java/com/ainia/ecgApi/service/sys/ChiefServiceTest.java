package com.ainia.ecgApi.service.sys;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

	public void setChiefService(ChiefService chiefService) {
		this.chiefService = chiefService;
	}
	
	
}
