package com.ainia.ecgApi.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.sys.SystemConfig;
import com.ainia.ecgApi.service.sys.SystemConfigService;

/**
 * <p>SystemConfig controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * SystemConfigController.java
 * @author pq
 * @createdDate 2013-07-17
 * @version 0.1
 */
@Controller
@RequestMapping("/api/sysconfig")
public class SystemConfigController extends BaseController<SystemConfig , Long> {

    @Autowired
    private SystemConfigService systemConfigService;
    
    @Override
    public BaseService<SystemConfig , Long> getBaseService() {
        return systemConfigService;
    }

}
