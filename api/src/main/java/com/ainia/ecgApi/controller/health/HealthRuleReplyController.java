package com.ainia.ecgApi.controller.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.health.HealthRuleReply;
import com.ainia.ecgApi.service.health.HealthRuleReplyService;

/**
 * <p>HealthRuleReply controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthRuleReplyController.java
 * @author pq
 * @createdDate 2013-07-04
 * @version 0.1
 */
@Controller
@RequestMapping("/api/healthRuleReply")
public class HealthRuleReplyController extends BaseController<HealthRuleReply , Long> {

    @Autowired
    private HealthRuleReplyService healthRuleReplyService;
    
    @Override
    public BaseService<HealthRuleReply , Long> getBaseService() {
        return healthRuleReplyService;
    }

}
