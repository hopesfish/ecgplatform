package com.ainia.ecgApi.domain.apk;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.ainia.ecgApi.core.bean.Domain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * <p>apk 对象</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * Apk.java
 * @author pq
 * @createdDate 2013-8-7
 * @version
 */
@Entity
public class Apk implements Domain {
	
	public static final String APK_NAME = "healthApk";
	public static final String APK_SUFFIX = ".apk";
	public static final String VERSION = "version";
	public static final String ENABLED = "enabled";
	
	private Long id;
	private String version;
	private boolean isReleased;
	private boolean enabled = true;
	private Date createdDate;
	private String externalUrl;

	@PrePersist
	public void onCreate() {
		this.createdDate = new Date();
	}

	/**
	 * <p>返回APK 存储名称</p>
	 * @return
	 * String
	 */
	@Transient
	public String getName() {
		 return APK_NAME +  this.getId()  + APK_SUFFIX;
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	public Long getId() {
		return id;
	}

	@NotBlank
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@JsonIgnore
	public boolean isReleased() {
		return isReleased;
	}

	public void setReleased(boolean isReleased) {
		this.isReleased = isReleased;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm" , timezone = "GMT+08:00")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		Apk other = (Apk) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getExternalUrl() {
		return externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}
	

}
