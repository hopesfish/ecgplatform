package com.ainia.ecgApi.service.sys;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.domain.sys.Employee.Status;

/**
 * <p>employee service test</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * EmployeeServiceTest.java
 * @author pq
 * @createdDate 2013-7-30
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class EmployeeServiceTest {

	@Autowired
	private EmployeeService employeeService;
	
	@Test
	public void testCheckLastLiveDate() {
		DateTime now = new DateTime();
		Employee employee = employeeService.get(1L);
		employee.setStatus(Status.ONLINE);
		employee.setLastLiveDate(now.minusMinutes(30).toDate());
		employeeService.update(employee);
		
		employeeService.checkLive();
		
		employee = employeeService.get(1L);
		
		Assert.assertTrue(employee.getStatus().equals(Status.OFFLINE));
	}
}
