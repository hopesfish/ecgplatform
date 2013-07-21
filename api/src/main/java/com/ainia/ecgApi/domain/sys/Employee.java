package com.ainia.ecgApi.domain.sys;

import java.text.SimpleDateFormat;
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
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.ainia.ecgApi.core.bean.Domain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type" , discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("employee")
public class Employee implements Domain {
	
	public static final String PASSWORD = "password";
	public static final String STATUS = "status";
	
	private Long id;
	private String name;
	private String username;
	@JsonIgnore
	private String password;
	private Status status;
	private boolean enabled;
	private boolean dismissed;
	private int gender;
	private Date    expire;
	private Date    birthday;
	private String  idCard;
	private String  mobile;
	private Date    createdDate;
	private Date    lastUpdated;
	private String  roles;
	private String company;
	private String title;
	private Integer version;
	
	public enum Status {
		ONLINE,
		AWAY,
		OFFLINEING,
		OFFLINE
	}

	@PrePersist
	public void onCreate() {
		this.createdDate = new Date();
		this.lastUpdated = new Date();
	}
	
	@PreUpdate
	public void onUpdate() {
		this.lastUpdated = new Date();
	}
	@Transient
	public boolean isSuperAdmin() {
		return new Long(1).equals(this.getId());
	}

	@Transient
	@JsonIgnore
	public String[] getRolesArray() {
		if (roles == null) {
			return null;
		}
		//TODO the sign ',' to be into Role Class
		return StringUtils.split(roles , ",");
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@NotBlank
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@JsonIgnore
	@NotBlank
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isDismissed() {
		return dismissed;
	}

	public void setDismissed(boolean dismissed) {
		this.dismissed = dismissed;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}
	@JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+08:00")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,  timezone = "GMT+08:00")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,  timezone = "GMT+08:00")
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	
	public void setId(Long id) {
		this.id = id;
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
		Employee other = (Employee) obj;
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
