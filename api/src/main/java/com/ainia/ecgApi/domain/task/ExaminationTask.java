package com.ainia.ecgApi.domain.task;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthRule.Level;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>健康监测任务</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ExaminationTask.java
 * @author pq
 * @createdDate 2013-7-7
 * @version
 */
@Entity
@DiscriminatorValue("examinationTask")
public class ExaminationTask extends Task {

	private Long examinationId;
	private HealthExamination examination;

	@NotNull
	public Long getExaminationId() {
		return examinationId;
	}

	public void setExaminationId(Long examinationId) {
		this.examinationId = examinationId;
	}
	@Transient
	public Long getUserId() {
		return examination.getUserId();
	}
	@Transient
	public String getUserName() {
		return examination.getUserName();
	}
	
	@Transient
	public Level getLevel() {
		return examination.getLevel();
	}
	@ManyToOne
	@JoinColumn(name="examinationId", nullable = false ,insertable = false , updatable = false)
	@JsonIgnore
	public HealthExamination getExamination() {
		return examination;
	}

	public void setExamination(HealthExamination examination) {
		this.examination = examination;
	}
	
	
}
