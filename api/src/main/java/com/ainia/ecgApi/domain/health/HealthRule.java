package com.ainia.ecgApi.domain.health;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;

import com.ainia.ecgApi.core.bean.Domain;
import com.ainia.ecgApi.core.utils.PropertyUtil;
import com.ainia.ecgApi.core.utils.ServiceUtils;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.service.health.HealthRuleService;
import com.ainia.ecgApi.service.sys.EmployeeService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>健康规则</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthRule.java
 * @author pq
 * @createdDate 2013-7-4
 * @version 0.1
 */
@Entity
public class HealthRule implements Domain {
	
	public static final String RULE_ID = "ruleId";
	public static final String USAGE = "usage";
	
	public static final String USAGE_GROUP = "group";
	public static final String USAGE_FILTER = "filter";

	private Long id;
	private String name;
	private String code;
	private String type;
	private String usage;
	private Long  groupId;
	private Long  employeeId;
	private String unit;
	private Level  level;
	private Float  min;
	private Float  max;
	private Date   createdDate;
	private Date   lastUpdated;
	private String remark;
	private Integer version;
	private boolean canReply;
	private List<HealthRuleReply> replys;
	private Set<User> users;
 	
	
	
	public enum Usage {
		group,
		filter ,
		reply
	}
	
	public enum Level {
		danger,
		warning,
		success,
		outside
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
	@PreRemove
	public void onDelete() {
		HealthRuleService healthRuleService = (HealthRuleService)ServiceUtils.getService(HealthRuleService.class);
		healthRuleService.deleteByGroup(this.id);	
	}
	/**
	 * <p>判断规则是否满足健康测试</p>
	 * @param examination
	 * @return
	 * boolean
	 */
	@Transient
	public boolean isMatch(HealthExamination examination) {
		if (PropertyUtil.hasReadableProperty(examination,  this.code)) {
			Float value = (Float)PropertyUtil.getProperty(examination, this.code);
			if ((this.min == null || value > this.min) && (this.max == null || value < this.max)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * <p>使用自动回复设置</p>
	 * @param reply
	 * void
	 */
	public void autoReply(HealthReply reply) {
		List<HealthRuleReply> ruleReplys = this.getReplys();
		if (ruleReplys != null && !ruleReplys.isEmpty()) {
			HealthRuleReply ruleReply = ruleReplys.get(0);
			reply.setResult(ruleReply.getResult());
			reply.setReason(ruleReply.getTitle());
			reply.setContent(ruleReply.getContent());
		}
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@NotBlank
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}

	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	@Transient
	public String getEmployeeName() {
		//TODO  user the @Formula  replace the method
		if (employeeId == null) {
			return null;
		}
		EmployeeService employeeService = (EmployeeService)ServiceUtils.getService(EmployeeService.class);
		Employee employee = employeeService.get(employeeId);
		return employee == null ? null : employee.getName();
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	@Enumerated(EnumType.STRING)
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Float getMin() {
		return min;
	}

	public void setMin(Float min) {
		this.min = min;
	}

	public Float getMax() {
		return max;
	}

	public void setMax(Float max) {
		this.max = max;
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

	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public boolean isCanReply() {
		return canReply;
	}
	public void setCanReply(boolean canReply) {
		this.canReply = canReply;
	}
	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	@JsonIgnore
	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(orphanRemoval = true , targetEntity = HealthRuleReply.class)
	@JoinColumn(name = "ruleId")
	public List<HealthRuleReply> getReplys() {
		return replys;
	}
	private void setReplys(List<HealthRuleReply> replys) {
		this.replys = replys;
	}
	
	@JsonIgnore
	@Fetch(FetchMode.JOIN)
	@ManyToMany
	@JoinTable(name="health_rule_user"  , joinColumns={@JoinColumn(name="rule_id")}  
        						, inverseJoinColumns={@JoinColumn(name="user_id")}  
    )  
	public Set<User> getUsers() {
		return users;
	}
	private void setUsers(Set<User> users) {
		this.users = users;
	}
	@JsonIgnore
	public void addUser(User user) {
		if (users == null) {
			users = new HashSet<User>();
		}
		users.add(user);
	}
	
	@JsonIgnore
	public void removeUser(User user) {
		if (users == null) {
			return;
		}
		users.remove(user);
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
		HealthRule other = (HealthRule) obj;
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
