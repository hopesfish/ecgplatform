package com.ainia.ecgApi.dao.charge;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.domain.charge.Card;


/**
 * <p>Card Data Access Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * CardDao.java
 * @author pq
 * @createdDate 2013-07-29
 * @version 0.1
 */
public interface CardDao extends JpaRepository<Card , Long>, BaseDao<Card , Long> { 
    
	/**
	 * <p>根据卡号查询</p>
	 * @param serial
	 * void
	 */
    public Card findBySerial(String serial);
    
    /**
     * <p>根据加密卡号回去指定卡</p>
     * @param serial
     * @return
     * Card
     */
    public Card findByEncodedSerial(String serial);
}
