package com.ainia.ecgApi.domain.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.ainia.ecgApi.core.bean.Domain;

/**
 * <p>User Domain Object</p>
 * User.java
 * @author pq
 * @createdDate 2013-06-21
 * @version 0.1
 */
@Entity
@Table(name = "user")
public class User implements Domain {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long    id;
	private String  code;
	private String  name;
	private String  username;
	private String  password;
	private boolean sex;
	private String  type;
	private Date birthday;
	private String address;
	private Float  stature;
	private Float  weight;
	private String fnPlace;
	private String city;
	private String tel;
	private String emContact1;
	@Column(name="em_contact1_tel")
	private String emContact1Tel;
	private String emContact2;
	@Column(name="em_contact2_tel")
	private String emContact2Tel;
	private String badHabits;
	private String anamnesis;
	private Date   createdDate;
	private Date   lastUpdated;
	private String remark;
	private String mobileNum;
	private Boolean isFree;
	private Integer version;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Float getStature() {
		return stature;
	}

	public void setStature(Float stature) {
		this.stature = stature;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getFnPlace() {
		return fnPlace;
	}

	public void setFnPlace(String fnPlace) {
		this.fnPlace = fnPlace;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmContact1() {
		return emContact1;
	}

	public void setEmContact1(String emContact1) {
		this.emContact1 = emContact1;
	}

	public String getEmContact1Tel() {
		return emContact1Tel;
	}

	public void setEmContact1Tel(String emContact1Tel) {
		this.emContact1Tel = emContact1Tel;
	}

	public String getEmContact2() {
		return emContact2;
	}

	public void setEmContact2(String emContact2) {
		this.emContact2 = emContact2;
	}

	public String getEmContact2Tel() {
		return emContact2Tel;
	}

	public void setEmContact2Tel(String emContact2Tel) {
		this.emContact2Tel = emContact2Tel;
	}

	public String getBadHabits() {
		return badHabits;
	}

	public void setBadHabits(String badHabits) {
		this.badHabits = badHabits;
	}

	public String getAnamnesis() {
		return anamnesis;
	}

	public void setAnamnesis(String anamnesis) {
		this.anamnesis = anamnesis;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public Boolean getIsFree() {
		return isFree;
	}

	public void setIsFree(Boolean isFree) {
		this.isFree = isFree;
	}
	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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
		User other = (User) obj;
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
