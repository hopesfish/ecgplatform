package com.ainia.ecgApi.domain.task;

import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.ainia.ecgApi.core.bean.Domain;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type" , discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("task")
public class Task implements Domain {
	
	public static final String EXPERT_ID = "expertId";
	public static final String OPERATOR_ID = "operatorId";
	public static final String AUTO = "auto";
	public static final String CREATED_DATE = "createdDate";
	
	private Long id;
	private Status status;
	private Long expertId;
	private Long operatorId;
	private Boolean auto;
	private Date createdDate;
	private Date completedDate;
	private Integer version;
	
	@Transient
	public boolean isComplete() {
		return Status.completed.equals(status);
	}
	@PrePersist
	public void onCreate() {
		this.createdDate = new Date();
	}
	
	public enum Status {
		pending,
		proceeding,
		completed
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@NotNull
	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getExpertId() {
		return expertId;
	}

	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Boolean getAuto() {
		return auto;
	}

	public void setAuto(Boolean auto) {
		this.auto = auto;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}
