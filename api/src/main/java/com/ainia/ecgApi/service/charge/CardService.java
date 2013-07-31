package com.ainia.ecgApi.service.charge;

import java.util.Date;

import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.charge.Card;
import com.ainia.ecgApi.domain.sys.User;

/**
 * <p>Card Service interface </p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * CardService.java
 * @author pq
 * @createdDate 2013-07-29
 * @version 0.1
 */
public interface CardService extends BaseService<Card , Long> {
	
	/**
	 * <p>根据卡号查询卡</p>
	 * @param serial
	 * @return
	 * Card
	 */
	public Card findBySerial(String serial);
    
	/**
	 * <p>卡充值</p>
	 * @param serial
	 * @param activeDate
	 * @param employeeId
	 * void
	 */
	public void charge(String serial , String password , Date activeDate , Long employeeId , User user);
}
