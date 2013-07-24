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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.ainia.ecgApi.core.bean.Domain;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type" , discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("task")
public class Task implements Domain {
	
	public static final String USER_ID   = "userId";
	public static final String EXPERT_ID = "expertId";
	public static final String OPERATOR_ID = "operatorId";
	public static final String AUTO = "auto";
	public static final String CREATED_DATE = "createdDate";
	public static final String STATUS = "status";
	
	private Long id;
	private Status status;
	private String apkId;
	private Long userId;
	private String userName;
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
	public String getApkId() {
		return apkId;
	}
	public void setApkId(String apkId) {
		this.apkId = apkId;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
	
}
