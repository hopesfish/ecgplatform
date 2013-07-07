package com.ainia.ecgApi.domain.task;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

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

	@NotNull
	public Long getExaminationId() {
		return examinationId;
	}

	public void setExaminationId(Long examinationId) {
		this.examinationId = examinationId;
	}
	
	
}
