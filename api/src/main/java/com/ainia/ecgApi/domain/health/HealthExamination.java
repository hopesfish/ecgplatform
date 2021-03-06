package com.ainia.ecgApi.domain.health;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.ainia.ecgApi.core.bean.Domain;
import com.ainia.ecgApi.domain.health.HealthRule.Level;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>健康监测实体</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthExamination.java
 * @author pq
 * @createdDate 2013-7-7
 * @version 0.1
 */
@Entity
@SequenceGenerator(name="SEQ_HEALTH_EXAMINATION",sequenceName="SEQ_HEALTH_EXAMINATION", initialValue=1, allocationSize=100)
public class HealthExamination implements Domain {
	
	public static final String USER_ID = "userId";
	public static final String CREATED_DATE = "createdDate";
	public static final String BLOOD_PRESSURE_LOW = "bloodPressureLow";
	public static final String BLOOD_PRESSURE_HIGH = "bloodPressureHigh";
	public static final String HEART_RHYTHM = "heartRhythm";
	public static final String BLOOD_OXYGEN = "bloodOxygen";
	public static final String BLOOD_SUGAR = "bloodSugar";
	public static final String BREATH = "breath";
	public static final String BODY_TEMP = "bodyTemp";
	public static final String PULSERATE = "pulserate";
	
	private Long id;
	private Long userId;
	private String userName;
	private String testItem;
	private String userType;
	private Boolean isTest;
	private Boolean isGziped = false;
	private Level  level;
	private String apkId;
	private Integer bloodPressureLow;
	private Integer bloodPressureHigh;
	private Integer heartRhythm;
	private Integer bloodOxygen;
	private Integer breath;
	private Float bodyTemp;
	private Integer pulserate;
	private Boolean hasDataError;
	private String heartData;
	private Float bloodSugar; 
	private String medicine;
	private Double  latitude;
	private Integer  altitude;
	private Float temp;
	private Integer humidity;
	private Integer pressure;
	private String chargeType;
	private String heartFeatures;
	private Float  algorithmVersion;
	private Date createdDate;
	private Integer version;
	private Integer imgcount; // 用户自行上传图片数目
	
	@PrePersist
	public void onCreate() {
		this.createdDate = new Date();
	}
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_HEALTH_EXAMINATION")
	public Long getId() {
		return id;
	}
	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@NotBlank
	public String getTestItem() {
		return testItem;
	}

	public void setTestItem(String testItem) {
		this.testItem = testItem;
	}
	@NotBlank
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "health_level")
	public Level getLevel() {
		return level;
	}

	public Boolean getHasDataError() {
		return hasDataError;
	}
	public void setHasDataError(Boolean hasDataError) {
		this.hasDataError = hasDataError;
	}

	public String getMedicine() {
		return medicine;
	}

	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Integer getBloodPressureLow() {
		return bloodPressureLow;
	}

	public void setBloodPressureLow(Integer bloodPressureLow) {
		this.bloodPressureLow = bloodPressureLow;
	}


	public Integer getBloodPressureHigh() {
		return bloodPressureHigh;
	}



	public void setBloodPressureHigh(Integer bloodPressureHigh) {
		this.bloodPressureHigh = bloodPressureHigh;
	}



	public Integer getHeartRhythm() {
		return heartRhythm;
	}



	public void setHeartRhythm(Integer heartRhythm) {
		this.heartRhythm = heartRhythm;
	}
	public Boolean getIsTest() {
		return isTest;
	}


	public void setIsTest(Boolean isTest) {
		this.isTest = isTest;
	}

	public Integer getBloodOxygen() {
		return bloodOxygen;
	}



	public void setBloodOxygen(Integer bloodOxygen) {
		this.bloodOxygen = bloodOxygen;
	}



	public Integer getBreath() {
		return breath;
	}



	public void setBreath(Integer breath) {
		this.breath = breath;
	}



	public Float getBodyTemp() {
		return bodyTemp;
	}



	public void setBodyTemp(Float bodyTemp) {
		this.bodyTemp = bodyTemp;
	}



	public Integer getPulserate() {
		return pulserate;
	}



	public void setPulserate(Integer pulserate) {
		this.pulserate = pulserate;
	}



	public String getHeartData() {
		return heartData;
	}



	public void setHeartData(String heartData) {
		this.heartData = heartData;
	}


	@JsonIgnore
	public Double getLatitude() {
		return latitude;
	}



	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	@JsonIgnore
	public Integer getAltitude() {
		return altitude;
	}



	public void setAltitude(Integer altitude) {
		this.altitude = altitude;
	}



	public Float getTemp() {
		return temp;
	}



	public void setTemp(Float temp) {
		this.temp = temp;
	}


	@JsonIgnore
	public Integer getHumidity() {
		return humidity;
	}



	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}


	@JsonIgnore
	public Integer getPressure() {
		return pressure;
	}



	public void setPressure(Integer pressure) {
		this.pressure = pressure;
	}



	public String getChargeType() {
		return chargeType;
	}



	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}


	@JsonIgnore
	public String getHeartFeatures() {
		return heartFeatures;
	}



	public void setHeartFeatures(String heartFeatures) {
		this.heartFeatures = heartFeatures;
	}


	@JsonIgnore
	public Float getAlgorithmVersion() {
		return algorithmVersion;
	}



	public void setAlgorithmVersion(Float algorithmVersion) {
		this.algorithmVersion = algorithmVersion;
	}


	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Float getBloodSugar() {
		return bloodSugar;
	}


	public void setBloodSugar(Float bloodSugar) {
		this.bloodSugar = bloodSugar;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApkId() {
		return apkId;
	}

	public void setApkId(String apkId) {
		this.apkId = apkId;
	}

	public Integer getImgcount() {
		return imgcount;
	}

	public void setImgcount(Integer imgcount) {
		this.imgcount = imgcount;
	}

	@Transient
	public Boolean getIsGziped() {
		return isGziped;
	}

	public void setIsGziped(Boolean isGziped) {
		this.isGziped = isGziped;
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
		HealthExamination other = (HealthExamination) obj;
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
	
	public String getPropNameByCode (String code) {
		String prop = "unknown";
		if ("1".equals(code)) {
			prop =  HealthExamination.BLOOD_PRESSURE_LOW;
		} else if ("2".equals(code)) {
			prop =  HealthExamination.BLOOD_PRESSURE_HIGH;
		} else if ("3".equals(code)) {
			prop =  HealthExamination.HEART_RHYTHM;
		} else if ("4".equals(code)) {
			prop =  HealthExamination.BLOOD_OXYGEN;
		} else if ("5".equals(code)) {
			prop =  HealthExamination.BREATH;
		} else if ("6".equals(code)) {
			prop =  HealthExamination.BODY_TEMP;
		} else if ("7".equals(code)) {
			prop =  HealthExamination.PULSERATE;
		} else if ("8".equals(code)) {
			prop =  HealthExamination.BLOOD_SUGAR;
		}
		return prop;
	}


}
