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
  
   @Query(nativeQuery = true , value = "select avg(d.blood_Pressure_Low) , avg(d.blood_Pressure_High) , avg(d.heart_Rhythm) , avg(d.blood_Oxygen), " +
    		" avg(d.breath) , avg(d.body_Temp) , avg(d.pulserate) , d.day from (select h.* , to_char(h.created_date , 'yyyy-MM-dd') day from Health_Examination h where h.blood_Pressure_Low >= 0 and h.blood_Pressure_High >= 0 and h.heart_Rhythm >= 0 and h.user_Id = ? and h.created_Date >= ? and h.created_Date < ? order by  h.created_Date asc) d " +
    		"	 group by d.day order by d.day asc")
   public List<Object[]> statisticsByUserAndDay(Long userId , Date start , Date end);
}
