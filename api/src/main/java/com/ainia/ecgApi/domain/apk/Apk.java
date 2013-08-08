package com.ainia.ecgApi.domain.apk;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.ainia.ecgApi.core.bean.Domain;
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
	
	private Long id;
	private String version;
	private boolean isReleased;
	private boolean enabled;

	/**
	 * <p>返回APK 存储名称</p>
	 * @return
	 * String
	 */
	@Transient
	public String getName() {
		 return APK_NAME +  this.getVersion()  + APK_SUFFIX;
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	public Long getId() {
		return id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isReleased() {
		return isReleased;
	}

	public void setReleased(boolean isReleased) {
		this.isReleased = isReleased;
	}

	public void setId(Long id) {
		this.id = id;
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
	

}
