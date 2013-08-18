package com.ainia.ecgApi.service.task;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthRule.Level;
import com.ainia.ecgApi.domain.task.ExaminationTask;
import com.ainia.ecgApi.domain.task.Task;
import com.ainia.ecgApi.domain.task.Task.Status;
import com.ainia.ecgApi.service.health.HealthExaminationService;

/**
 * <p>
 * ExaminationTask Service test
 * </p>
 * Copyright: Copyright (c) 2013 Company: ExaminationTaskServiceTest.java
 * 
 * @author pq
 * @createdDate 2013-6-27
 * @version 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@ActiveProfiles("test")
@TransactionConfiguration(defaultRollback = true)
public class ExaminationTaskServiceTest {

	@Autowired
	private ExaminationTaskService examinationTaskService;
	@Autowired
	private HealthExaminationService healthExaminationService;
	@Autowired
	private TaskService taskService;

	private static ExaminationTask examinationTask;

	public void setExaminationTaskService(ExaminationTaskService examinationTaskService) {
		this.examinationTaskService = examinationTaskService;
	}

	@Before
	public void setUp() {
		HealthExamination healthExamination = new HealthExamination();
		healthExamination.setUserId(1L);
		healthExamination.setTestItem("PHONE");
		healthExamination.setUserType("VIP");
		healthExamination.setLevel(Level.danger);
		healthExamination.setBloodPressureLow(80);
		healthExamination.setBloodPressureHigh(100);
		healthExamination.setHeartRhythm(1);
		healthExamination.setBloodOxygen(1);
		healthExamination.setBreath(1);
		healthExamination.setBodyTemp(1.0F);
		healthExamination.setPulserate(1);
		healthExamination.setHeartData("");
		healthExamination.setLatitude(0.0);
		healthExamination.setAltitude(1);
		healthExamination.setTemp(27.0F);
		healthExamination.setHumidity(1);
		healthExamination.setPressure(1);
		healthExamination.setChargeType("");
		healthExamination.setHeartFeatures("");
		healthExamination.setAlgorithmVersion(1.0F);
		healthExaminationService.create(healthExamination);

		examinationTask = new ExaminationTask();
		examinationTask.setExaminationId(healthExamination.getId());
		examinationTask.setStatus(Status.pending);
		examinationTask.setOperatorId(4L);
		examinationTask.setAuto(false);
	}

	@Test
	public void testFind() {
		Query<ExaminationTask> query = new Query<ExaminationTask>();
		List<ExaminationTask> examinationTasks = examinationTaskService.findAll(query);

		Assert.assertNotEquals(examinationTasks.size(), 0);
	}

	@Test
	public void testCreate() {
		examinationTaskService.create(examinationTask);

		Assert.assertTrue(examinationTask.getId() != null);

		examinationTaskService.delete(examinationTask);
	}

	@Test
	public void testUpdate() {
		examinationTaskService.create(examinationTask);
		ExaminationTask _examinationTask = examinationTaskService.update(examinationTask);

		examinationTaskService.delete(_examinationTask);
	}

	@Test
	public void testPatch() {
		examinationTaskService.create(examinationTask);

		ExaminationTask _examinationTask = examinationTaskService.update(examinationTask);

		examinationTaskService.delete(_examinationTask);
	}

	@Test
	public void testDeleteCacade() {
		examinationTaskService.create(examinationTask);
		
		Long examinationId = examinationTask.getExaminationId();

		taskService.delete(examinationTask.getId());

		HealthExamination healthExamination = healthExaminationService.get(examinationId);
		Assert.assertEquals(healthExamination, null);
	}
}
