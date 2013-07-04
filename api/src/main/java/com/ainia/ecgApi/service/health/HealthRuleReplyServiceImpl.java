package com.ainia.ecgApi.service.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ainia.ecgApi.core.crud.BaseDao;
import com.ainia.ecgApi.core.crud.BaseServiceImpl;
import com.ainia.ecgApi.dao.health.HealthRuleReplyDao;
import com.ainia.ecgApi.domain.health.HealthRuleReply;

/**
 * <p>HealthRuleReply Service Impl</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthRuleReplyServiceImpl.java
 * @author pq
 * @createdDate 2013-07-04
 * @version 0.1
 */
@Service
public class HealthRuleReplyServiceImpl extends BaseServiceImpl<HealthRuleReply , Long> implements HealthRuleReplyService {
    
    @Autowired
    private HealthRuleReplyDao healthRuleReplyDao;
    
    @Override
    public BaseDao<HealthRuleReply , Long> getBaseDao() {
        return healthRuleReplyDao;
    }

}
