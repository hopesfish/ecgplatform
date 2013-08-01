package com.ainia.ecgApi.dao.health;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.health.HealthExamination;


/**
 * <p>HealthExamination Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthExaminationDao.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
public interface HealthExaminationDao extends JpaRepository<HealthExamination , Long>, BaseDao<HealthExamination , Long> { 
    
    @Query("select avg(bloodPressureLow) , avg(bloodPressureHigh) , avg(heartRhythm) , avg(bloodOxygen), " +
    		" avg(breath) , avg(bodyTemp) , avg(pulserate), YEAR(createdDate)||'-'||MONTH(createdDate)||'-'||DAY(createdDate) from HealthExamination where userId = ? and createdDate >= ? and createdDate < ?" +
    		"	group by YEAR(createdDate)||'-'||MONTH(createdDate)||'-'||DAY(createdDate) order by  YEAR(createdDate)||'-'||MONTH(createdDate)||'-'||DAY(createdDate) asc")
	public List<Object[]> statisticsByUserAndDay(Long userId , Date start , Date end);
}
