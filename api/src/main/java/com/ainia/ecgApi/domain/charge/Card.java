package com.ainia.ecgApi.domain.charge;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.ainia.ecgApi.core.bean.Domain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>充值卡实体</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * Card.java
 * @author pq
 * @createdDate 2013-7-29
 * @version
 */
@Entity
public class Card implements Domain {
	
	public static String ENCODED_SERIAL = "encodedSerial";
	public static String ENCODED_PASSWORD = "encodedPassword";
	public static String DAYS = "days";
	public static String EXPIRED_DATE = "expireDate";
	public static String ACTIVED_DATE = "activedDate";
	public static String USER_ID = "userId";

	private Long id;
	private String encodedSerial;
	private String serial;
	private String encodedPassword;
	private Integer days;
	private Date createdDate;
	private Integer createdBatch;
	private Date expireDate;
	private Date activedDate;
	private Long userId;
	private String userName;
	private Date chargedDate;
	private String chargeType;
	private Long employeeId;
	private String employeeName;
	
	
	
	@PrePersist
	public void onCreate() {
		this.createdDate = new Date();
	}
	
	public enum ChargeType {
		MOBILE , 
		WEB
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	public Long getId() {
		return id;
	}

	@JsonIgnore
	public String getEncodedSerial() {
		return encodedSerial;
	}

	@NotBlank
	public void setEncodedSerial(String encodedSerial) {
		this.encodedSerial = encodedSerial;
	}


	public String getSerial() {
		return serial;
	}


	public void setSerial(String serial) {
		this.serial = serial;
	}
	@NotBlank
	@JsonIgnore
	public String getEncodedPassword() {
		return encodedPassword;
	}


	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}
	@NotNull
	@Max(365)
	public Integer getDays() {
		return days;
	}


	public void setDays(Integer days) {
		this.days = days;
	}
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public Integer getCreatedBatch() {
		return createdBatch;
	}


	public void setCreatedBatch(Integer createdBatch) {
		this.createdBatch = createdBatch;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
	public Date getExpireDate() {
		return expireDate;
	}


	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
	public Date getChargedDate() {
		return chargedDate;
	}


	public void setChargedDate(Date chargedDate) {
		this.chargedDate = chargedDate;
	}


	public String getChargeType() {
		return chargeType;
	}


	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}


	public Long getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}


	public String getEmployeeName() {
		return employeeName;
	}


	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}


	public void setId(Long id) {
		this.id = id;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
	public Date getActivedDate() {
		return activedDate;
	}

	public void setActivedDate(Date activedDate) {
		this.activedDate = activedDate;
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
		Card other = (Card) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Card [id=" + id + ", encodedSerial=" + encodedSerial
				+ ", serial=" + serial + ", encodedPassword=" + encodedPassword
				+ ", days=" + days + ", createdDate=" + createdDate
				+ ", createdBatch=" + createdBatch + ", expireDate="
				+ expireDate + ", userId=" + userId + ", userName=" + userName
				+ ", chargedDate=" + chargedDate + ", chargeType=" + chargeType
				+ ", employeeId=" + employeeId + ", employeeName="
				+ employeeName + "]";
	}
	
}
