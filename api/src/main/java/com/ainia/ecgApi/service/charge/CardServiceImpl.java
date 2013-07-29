package com.ainia.ecgApi.service.charge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.charge.CardDao;
import com.ainia.ecgApi.domain.charge.Card;

/**
 * <p>Card Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * CardServiceImpl.java
 * @author pq
 * @createdDate 2013-07-29
 * @version 0.1
 */
@Service
public class CardServiceImpl extends BaseServiceImpl<Card , Long> implements CardService {
    
    @Autowired
    private CardDao cardDao;
    
    @Override
    public BaseDao<Card , Long> getBaseDao() {
        return cardDao;
    }

}
