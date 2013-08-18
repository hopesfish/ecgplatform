package com.ainia.ecgApi.dao.apk;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.apk.Apk;


/**
 * <p>Apk Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ApkDao.java
 * @author pq
 * @createdDate 2013-08-07
 * @version 0.1
 */
public interface ApkDao extends JpaRepository<Apk , Long>, BaseDao<Apk , Long> { 
    
    
}
