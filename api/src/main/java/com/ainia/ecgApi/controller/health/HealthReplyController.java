package com.ainia.ecgApi.controller.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.health.HealthReply;
import com.ainia.ecgApi.service.health.HealthReplyService;

/**
 * <p>HealthReply controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthReplyController.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
@Controller
@RequestMapping("/healthReply")
public class HealthReplyController extends BaseController<HealthReply , Long> {

    @Autowired
    private HealthReplyService healthReplyService;
    
    @Override
    public BaseService<HealthReply , Long> getBaseService() {
        return healthReplyService;
    }

}
